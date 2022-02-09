package com.chf.lightjob.service;

import com.chf.lightjob.model.DelayTaskCreateRequest;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 00:29
 */
public interface DelayTaskService {

    Long createTask(DelayTaskCreateRequest request);
}
