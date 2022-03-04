package com.chf.lightjob.service;

import com.chf.lightjob.dal.entity.TaskDO;

/**
 * @description
 * @author: davy
 * @create: 2022-03-04 18:50
 */
public interface TaskService {

    /**
     * 刷新任务过期时间
     */
    void refreshExpireTime(TaskDO taskDO);

    /**
     * 忽略任务
     */
    void discardTask(long taskId);
}
