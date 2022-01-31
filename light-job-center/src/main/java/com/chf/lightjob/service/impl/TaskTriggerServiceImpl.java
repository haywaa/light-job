package com.chf.lightjob.service.impl;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chf.lightjob.dal.entity.TaskDO;
import com.chf.lightjob.service.DatabaseTimeService;
import com.chf.lightjob.service.TaskTriggerService;

/**
 * @description
 * @author: davy
 * @create: 2022-01-31 12:41
 */
@Service
public class TaskTriggerServiceImpl implements TaskTriggerService {

    @Autowired
    private DatabaseTimeService databaseTimeService;

    private LinkedBlockingQueue<List<TaskDO>> taskListQueue = new LinkedBlockingQueue(5);
    private ThreadPoolExecutor fastTriggerPool = null;

    @Override
    public void trigger(List<TaskDO> taskDOList) {
        try {
            taskListQueue.put(taskDOList);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @PostConstruct
    public void init() {
        this.fastTriggerPool = new ThreadPoolExecutor(
                10,
                200,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                r -> new Thread(r, "light-job-task-trigger-" + r.hashCode()));
        Thread dispatchThread = new Thread(() -> {
            while(true) {
                try {
                    List<TaskDO> taskDOList = taskListQueue.take();
                    if (taskDOList == null || taskDOList.isEmpty()) {
                        continue;
                    }

                    for (TaskDO taskDO : taskDOList) {
                        triggerTask(taskDO);
                    }
                } catch (Throwable t) {
                    if (t instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    // TODO log
                }
            }
        });
        dispatchThread.setName("light_job_task_dispatcher");
        dispatchThread.start();
    }

    private void triggerTask(TaskDO taskDO) {
        if (taskDO.getTriggerTime().after(databaseTimeService.currentTime())) {
            // TODO add to time ring
            return;
        }

        this.fastTriggerPool.execute(() -> {
            // TODO
        });
    }
}
