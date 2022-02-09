package com.chf.lightjob.service.impl;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chf.lightjob.dal.entity.TaskDO;
import com.chf.lightjob.dal.mapper.TaskMapper;
import com.chf.lightjob.enums.BlockStrategyEnum;
import com.chf.lightjob.enums.JobTypeEnum;
import com.chf.lightjob.service.DatabaseTimeService;
import com.chf.lightjob.service.LockService;
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

    @Autowired
    private LockService lockService;

    @Autowired
    private TaskMapper taskMapper;

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
                        if (taskDO.getTriggerTime().after(databaseTimeService.currentTime())) {
                            // TODO add to time ring
                            return;
                        }



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
        this.fastTriggerPool.execute(() -> {
            if (JobTypeEnum.PERIODIC_JOB.name().equals(taskDO.getJobType())) {
                lockService.lock("periodicjob_" + taskDO.getFromJobId(), () -> {
                    TaskDO firstUnfinshedTask = taskMapper.selectFirstUnfinishedTaskForJob(JobTypeEnum.PERIODIC_JOB.name(), taskDO.getFromJobId());
                    if (firstUnfinshedTask == null) {
                        // log warn
                        return null;
                    }
                    if (!firstUnfinshedTask.getFromJobId().equals(taskDO.getFromJobId())) {
                        if (BlockStrategyEnum.SERIAL_EXECUTION.name().equals(taskDO.getBlockStrategy())) {
                            // TODO 延长过期时间
                        } else if (BlockStrategyEnum.DISCARD_LATER.name().equals(taskDO.getBlockStrategy())) {
                            // TODO 忽略任务
                        } else {
                            throw new RuntimeException("invalid block strategy:" + taskDO.getBlockStrategy());
                        }
                        return null;
                    }
                    notifyExecutor(taskDO);
                    return null;
                });
                return;
            }

            notifyExecutor(taskDO);
        });
    }


    private void notifyExecutor(TaskDO taskDO) {
        // TODO 1. send MQ OR call executor by rpc
        // 2. update task trigger info
    }
}
