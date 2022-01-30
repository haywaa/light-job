package com.chf.lightjob.service;

import com.chf.lightjob.model.DelayTaskCreateRequest;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 00:29
 */
public interface DelayTaskService {

    Long createTask(DelayTaskCreateRequest request);

    <T> T execute(MainRunner<T> mainRunner, FollowRunner asyncRunner, DelayTaskCreateRequest request);

    <T> T execute(MainRunner<T> mainRunner, FollowRunner followRunner, FollowRunAsync needAsync, DelayTaskCreateRequest request);

    @FunctionalInterface
    interface MainRunner<T> {
        T execute();
    };

    @FunctionalInterface
    interface FollowRunner {
        void execute();
    }

    @FunctionalInterface
    interface FollowRunAsync {
        boolean needAsync();
    }
}
