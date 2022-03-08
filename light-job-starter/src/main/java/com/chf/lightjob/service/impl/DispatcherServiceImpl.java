package com.chf.lightjob.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.chf.lightjob.annotation.Executor;
import com.chf.lightjob.executor.TaskExecutor;
import com.chf.lightjob.model.TaskContent;
import com.chf.lightjob.service.DispatcherService;

/**
 * @description
 * @author: davy
 * @create: 2022-03-04 22:22
 */
@Service
public class DispatcherServiceImpl implements DispatcherService {

    private Map<String, TaskExecutor> executorMap;

    @Autowired
    public DispatcherServiceImpl(List<TaskExecutor> executorList) {
        if (executorList == null) {
            executorMap = Collections.emptyMap();
        } else {
            executorMap = executorList.stream().collect(Collectors.toMap(executor -> {
                return executor.handlerCode();
                //return executor.getClass().getDeclaredAnnotation(Executor.class).code();
            }, o -> o));
        }
    }

    @Override
    public void dispatchTask(TaskContent taskContent) throws Exception {
        TaskExecutor taskExecutor = executorMap.get(taskContent.getJobHandler());
        Assert.notNull(taskExecutor, "任务执行器不存在");
        taskExecutor.execute(taskContent);
    }
}
