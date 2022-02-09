package com.chf.lightjob.service.impl;

import org.springframework.stereotype.Service;

import com.chf.lightjob.model.CompensateTaskCreateRequest;
import com.chf.lightjob.service.CompensateTaskService;

/**
 * @description
 * @author: davy
 * @create: 2022-02-09 23:28
 */
@Service
public class CompensateTaskServiceImpl implements CompensateTaskService {

    @Override
    public <T> T execute(MainRunner<T> mainRunner, FollowRunner asyncRunner, CompensateTaskCreateRequest request) {
        return null;
    }

    @Override
    public <T> T execute(MainRunner<T> mainRunner, FollowRunner followRunner, FollowRunAsync needAsync, CompensateTaskCreateRequest request) {
        return null;
    }
}
