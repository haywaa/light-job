package com.chf.lightjob.plugin.impl;

import java.util.Date;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.chf.lightjob.model.TaskContent;
import com.chf.lightjob.service.DispatcherService;

import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.extern.slf4j.Slf4j;

/**
 * @description
 * @author: qingye
 * @create: 2020-05-13 14:17
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "lightjob.channel", value = "redis")
public class RedisEventChannel implements InitializingBean, Runnable, DisposableBean {

    @Autowired
    private DispatcherService dispatcherService;

    @Value("${lightjob.channel.redis.host:}")
    private String redisHost;
    @Value("${lightjob.channel.redis.port:6379}")
    private String redisPort;
    @Value("${lightjob.channel.redis.password:}")
    private String redisPassword;
    @Value("${lightjob.channel.redis.database:0}")
    private int redisDatabase = 0;
    @Value("${lightjob.group:#{null}}")
    private String groupCode;

    private volatile RedisClient redisClient;
    private volatile StatefulRedisConnection<String, String> connection;

    private volatile RedisCommands<String, String> commands;
    private volatile RedisAsyncCommands<String, String> asyncCommands;
    private volatile Thread loopThread;
    private ThreadPoolExecutor executorService;

    @Override
    public void afterPropertiesSet() throws Exception {
        ThreadFactory threadFactory = new ThreadFactory() {
            int i = 1;
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("lightjob-pool-" + i);
                i++;
                return t;
            }
        };
        executorService = new ThreadPoolExecutor(20, 0, 0L, TimeUnit.SECONDS, new SynchronousQueue<>(), threadFactory);
        restart();
        registerEvent();
    }

    @Override
    public void destroy() throws Exception {
        if (loopThread != null) {
            loopThread.interrupt();
        }
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private synchronized void restart() {
        commands = null;
        if (connection != null) {
            connection.close();
            connection = null;
        }
        if (redisClient != null) {
            redisClient.shutdown();
            redisClient = null;
        }
        Assert.hasText(redisHost, "lightjob redisHost is blank");
        Assert.hasText(redisPort, "lightjob redisPort is blank");
        Assert.hasText(groupCode, "lightjob groupCode is blank");
        RedisURI.Builder redisURI = RedisURI.builder()
                .withHost(redisHost.trim())
                .withPort(Integer.valueOf(redisPort))
                .withDatabase(redisDatabase);

        if (redisPassword != null) {
            redisURI.withPassword(redisPassword);
        }
        redisClient = RedisClient.create(redisURI.build());
        connection = redisClient.connect();
        commands = connection.sync();
        asyncCommands = connection.async();
    }

    private void registerEvent() {
        this.loopThread = new Thread(this);
        this.loopThread.setName("lightjob-loop");
        this.loopThread.start();
    }

    @Override
    public void run() {
        while (true) {
            TaskContent taskContent = null;
            try {
                if (asyncCommands == null || groupCode == null) {
                    TimeUnit.SECONDS.sleep(10);
                    continue;
                }

                // 避免高延时任务对低延时任务造成的阻塞，可进行线程池隔离优化
                if (executorService.getCorePoolSize() - executorService.getActiveCount() <= 0) {
                    TimeUnit.MILLISECONDS.sleep(50);
                    continue;
                }

                long startTime = System.currentTimeMillis();
                KeyValue<String, String> result = asyncCommands.brpop(20, "lightjobq_" + groupCode).get();
                if (result == null || !StringUtils.hasText(result.getValue())) {
                    if (System.currentTimeMillis() - startTime < 5_000) {
                        // 避免空轮询CPU损耗
                        TimeUnit.SECONDS.sleep(5);
                    }
                    continue;
                }

                taskContent = JSON.parseObject(result.getValue(), TaskContent.class);
                if (taskContent.getExpireTime() != null && taskContent.getExpireTime().before(new Date())) {
                    continue;
                }
                TaskContent taskContentF = taskContent;
                executorService.execute(() -> {
                    try {
                        dispatcherService.dispatchTask(taskContentF);
                        markTaskSuccess(taskContentF);
                    } catch (Throwable t) {
                        markTaskFailure(taskContentF, t);
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Throwable t) {
                if (taskContent != null) {
                    markTaskFailure(taskContent, t);
                    continue;
                }
                log.error("lightjob error: {}", t.getMessage(), t);
            }
        }
    }

    private void markTaskSuccess(TaskContent taskContent) {
        asyncCommands.lpush("lightjob_success", String.valueOf(taskContent.getTaskId()));
    }

    private void markTaskFailure(TaskContent taskContent, Throwable t) {
        asyncCommands.lpush("lightjob_failure", String.valueOf(taskContent.getTaskId()));
        log.error("lightjob execute failure, taskId:[{}], triggerIndex:[{}]", taskContent.getTaskId(), taskContent.getTriggerIndex(), t);
    }
}
