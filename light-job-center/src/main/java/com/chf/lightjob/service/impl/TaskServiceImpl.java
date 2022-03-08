package com.chf.lightjob.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chf.lightjob.dal.entity.TaskDO;
import com.chf.lightjob.dal.mapper.TaskMapper;
import com.chf.lightjob.service.TaskService;
/**
 * @description
 * @author: davy
 * @create: 2022-03-04 18:50
 */
@Service
public class TaskServiceImpl implements TaskService {

    private static final long TRIGGER_OVERTIME = 10_000; // 10s

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public void refreshExpireTime(TaskDO taskDO) {
        Date fromTime = taskDO.getExpireTime() == null ? taskDO.getTriggerTime() : taskDO.getExpireTime();
        taskDO.setExpireTriggerTime(new Date(fromTime.getTime() + TRIGGER_OVERTIME));
        taskDO.setExpireTime(new Date(taskDO.getExpireTriggerTime().getTime() + taskDO.getExecutorTimeout()));
    }

    @Override
    public void discardTask(long taskId) {
        // TODO
    }

    @Override
    public void markTaskSuccess(List<Long> taskIds) {
        taskMapper.markTaskSuccess(taskIds);
    }

    @Override
    public void markTaskFailure(List<Long> taskIds) {
        // TODO
    }
}
