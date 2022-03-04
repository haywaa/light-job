package com.chf.lightjob.service;

import com.chf.lightjob.dal.entity.TaskDO;

/**
 * @description
 * @author: davy
 * @create: 2022-03-04 18:50
 */
public interface TaskService {

    void refreshExpireTime(TaskDO taskDO);
}
