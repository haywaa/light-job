package com.chf.lightjob.plugin.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.chf.lightjob.dal.entity.TaskDO;
import com.chf.lightjob.plugin.ChannelPlugin;

/**
 * @description
 * @author: davy
 * @create: 2022-03-04 22:27
 */
@Component
@ConditionalOnProperty(name = "lightjob.channel", value = "redis")
public class RedisChannelPlugin implements ChannelPlugin {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public void notifyExecutor(TaskDO taskDO) {
        // TODO 1. send MQ, add Redis event in list OR call executor by rpc
        // 2. update task trigger info
    }
}
