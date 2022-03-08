package com.chf.lightjob.plugin.channel;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.chf.lightjob.dal.entity.TaskDO;
import com.chf.lightjob.plugin.ChannelPlugin;
import com.chf.lightjob.service.TaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * @description
 * @author: davy
 * @create: 2022-03-04 22:27
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "lightjob.channel", value = "redis")
public class RedisChannelPlugin implements ChannelPlugin, InitializingBean, DisposableBean, Runnable {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TaskService taskService;

    private Thread loopThread;

    @Override
    public void notifyExecutor(TaskDO taskDO) {
        // TODO 1. send MQ, add Redis event in list OR call executor by rpc
        // 2. update task trigger info
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // monitor resultEvent
        this.loopThread = new Thread(this);
        this.loopThread.setName("lightjob-result-monitor-s");
        this.loopThread.start();
    }

    @Override
    public void destroy() throws Exception {
        if (loopThread != null) {
            this.loopThread.interrupt();
            this.loopThread = null;
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                boolean hasTask = false;
                List<String> taskIds = redisTemplate.opsForList().leftPop("lightjob_success", 50);
                if (taskIds != null && !taskIds.isEmpty()) {
                    hasTask = true;
                    List<Long> taskIdList = taskIds.stream().map(Long::parseLong).sorted().collect(Collectors.toList());
                    taskService.markTaskSuccess(taskIdList);
                }

                taskIds = redisTemplate.opsForList().leftPop("lightjob_failure", 50);
                if (taskIds != null && !taskIds.isEmpty()) {
                    hasTask = true;
                    List<Long> taskIdList = taskIds.stream().map(Long::parseLong).sorted().collect(Collectors.toList());
                    taskService.markTaskFailure(taskIdList);
                }

                if (!hasTask) {
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Throwable t) {
                log.error("monitor error: {}", t.getMessage(), t);
            }
        }
    }
}
