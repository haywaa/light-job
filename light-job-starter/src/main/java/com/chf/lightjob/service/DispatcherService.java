package com.chf.lightjob.service;

import com.chf.lightjob.model.TaskContent;

/**
 * @description
 * @author: davy
 * @create: 2022-03-04 22:21
 */
public interface DispatcherService {

    void dispatchTask(TaskContent taskContent) throws Exception;
}
