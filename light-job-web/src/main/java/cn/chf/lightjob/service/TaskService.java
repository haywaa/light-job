package cn.chf.lightjob.service;

import java.util.List;

import cn.chf.lightjob.dal.entity.TaskDO;

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

    void markTaskSuccess(List<Long> taskIds);

    void markTaskFailure(List<Long> taskIds);
}
