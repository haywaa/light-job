package cn.chf.lightjob.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.chf.lightjob.dal.entity.TaskDO;
import cn.chf.lightjob.dal.mapper.TaskMapper;
import cn.chf.lightjob.enums.BlockStrategyEnum;
import cn.chf.lightjob.enums.JobTypeEnum;
import cn.chf.lightjob.plugin.ChannelPlugin;
import cn.chf.lightjob.service.DatabaseTimeService;
import cn.chf.lightjob.service.LockService;
import cn.chf.lightjob.service.TaskService;
import cn.chf.lightjob.service.TaskTriggerService;

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

    @Autowired
    private TaskService taskService;

    @Autowired
    private ChannelPlugin channelPlugin;

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
                }
            }
        });
        dispatchThread.setName("light_job_task_dispatcher");
        dispatchThread.start();
    }

    private void triggerTask(TaskDO taskDO) {
        this.fastTriggerPool.execute(() -> {
            if (JobTypeEnum.PERIODIC_JOB.name().equals(taskDO.getJobType())
                    && !BlockStrategyEnum.CONCURRENT_EXECUTION.name().equals(taskDO.getBlockStrategy())) {
                lockService.lock("periodicjob_" + taskDO.getFromJobId(), () -> {
                    TaskDO firstUnfinshedTask = taskMapper.selectFirstUnfinishedTaskForJob(JobTypeEnum.PERIODIC_JOB.name(), taskDO.getFromJobId());
                    if (firstUnfinshedTask == null) {
                        // log warn
                        return null;
                    }
                    if (!firstUnfinshedTask.getId().equals(taskDO.getId())) {
                        if (firstUnfinshedTask.getId() < taskDO.getId()) {
                            if (BlockStrategyEnum.SERIAL_EXECUTION.name().equals(taskDO.getBlockStrategy())) {
                                taskDO.setExpireTime(new Date()); // ??????????????????????????????
                                taskService.refreshExpireTime(taskDO);
                                taskMapper.updateByPrimaryKey(taskDO);
                            } else if (BlockStrategyEnum.DISCARD_LATER.name().equals(taskDO.getBlockStrategy())) {
                                // ????????????
                                taskService.discardTask(taskDO.getId());
                                return null;
                            } else {
                                throw new RuntimeException("invalid block strategy:" + taskDO.getBlockStrategy());
                            }
                        } else {
                            // ???????????????taskDO???????????????finished???
                            return null;
                        }
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
        // 1. send MQ, add Redis event in list OR call executor by rpc
        channelPlugin.notifyExecutor(taskDO);
        // 2. log trigger info
    }
}
