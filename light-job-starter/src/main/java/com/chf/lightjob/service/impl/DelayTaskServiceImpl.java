package com.chf.lightjob.service.impl;

import org.springframework.stereotype.Service;

import com.chf.lightjob.model.DelayTaskCreateRequest;
import com.chf.lightjob.service.DelayTaskService;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 00:39
 */
@Service
public class DelayTaskServiceImpl implements DelayTaskService {

    @Override
    public Long createTask(DelayTaskCreateRequest request) {
        return null;
    }

    @Override
    public <T> T execute(MainRunner<T> mainRunner, FollowRunner asyncRunner, DelayTaskCreateRequest request) {
        return null;
    }

    @Override
    public <T> T execute(MainRunner<T> mainRunner, FollowRunner followRunner, FollowRunAsync needAsync, DelayTaskCreateRequest request) {
        return null;
    }
}
