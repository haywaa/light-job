package com.chf.lightjob.scheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.chf.lightjob.core.cron.CronExpression;
import com.chf.lightjob.dal.entity.PeriodicJobDO;
import com.chf.lightjob.dal.entity.TaskDO;
import com.chf.lightjob.dal.entity.TaskScheduleInfo;
import com.chf.lightjob.dal.mapper.LightJobMarkMapper;
import com.chf.lightjob.dal.mapper.PeriodicJobMapper;
import com.chf.lightjob.dal.mapper.TaskMapper;
import com.chf.lightjob.enums.JobTypeEnum;
import com.chf.lightjob.enums.MisfireStrategyEnum;
import com.chf.lightjob.enums.ScheduleTypeEnum;
import com.chf.lightjob.service.DatabaseTimeService;
import com.chf.lightjob.service.LockService;

/**
 * @description
 * @author: davy
 * @create: 2022-01-29 19:44
 */
@Component
public class PeriodicJobScheduler {

    private static Logger logger = LoggerFactory.getLogger(PeriodicJobScheduler.class);

    @Autowired
    private LockService lockService;

    @Autowired
    private PeriodicJobMapper periodicJobMapper;

    @Autowired
    private LightJobMarkMapper lightJobMarkMapper;

    @Autowired
    private DatabaseTimeService databaseTimeService;

    @Autowired
    private TaskMapper taskMapper;

    private Thread scheduleThread;

    private int preReadCount = 1000;

    private static int PRE_READ_MS = 5000;

    private int scheduleBatchIndex = 1;

    private List<TaskDO> notTriggeredTaskList = null;

    @PostConstruct
    public void start() {
        scheduleThread = new Thread(() -> {
            while(true) {
                long start = System.currentTimeMillis();
                int scanedCount = 0;
                try {
                    Thread.sleep(0);
                    scanedCount = lockService.lock("periodic_job_schedule", () -> {
                        return scanScheduleJob();
                    });

                    if (notTriggeredTaskList != null && !notTriggeredTaskList.isEmpty()) {
                        // TODO 触发任务（发MQ)
                    }

                } catch (Throwable t) {
                    if (t instanceof InterruptedException) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                    // TODO log
                } finally {
                    scheduleBatchIndex++;
                }

                long cost = System.currentTimeMillis()-start;
                // Wait seconds, align second
                if (cost < 1000) {  // scan-overtime, not wait
                    try {
                        // pre-read period: success > scan each second; fail > skip this period;
                        TimeUnit.MILLISECONDS.sleep((scanedCount > 0 ? 1000 : PRE_READ_MS) - System.currentTimeMillis()%1000);
                    } catch (InterruptedException e) {
                        //if (!scheduleThreadToStop) {
                        //    logger.error(e.getMessage(), e);
                        //}
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        scheduleThread.start();
    }

    private int scanScheduleJob() {
        long nowTime = databaseTimeService.currentTimeMillis();
        int scanedCount = 0;
        List<PeriodicJobDO> periodicJobDOList = periodicJobMapper.selectNowScheduleJob(PRE_READ_MS / 1000, preReadCount);
        if (periodicJobDOList != null && !periodicJobDOList.isEmpty()) {
            scanedCount = periodicJobDOList.size();
            List<TaskDO> taskDOList = new ArrayList<>(scanedCount * 105 / 100);
            String schdulerMark = null;// TODO localIp + scheduleBatchIndex;
            // 1. 构建Task列表，并刷新periodicJob下一次触发时间
            for (PeriodicJobDO periodicJobDO : periodicJobDOList) {
                try {
                    if (nowTime > periodicJobDO.getTriggerNextTime() + PRE_READ_MS) {
                        // 触发时长超过5s, 调度过期
                        // 1、misfire match
                        MisfireStrategyEnum misfireStrategyEnum = MisfireStrategyEnum.match(periodicJobDO.getMisfireStrategy(), MisfireStrategyEnum.DO_NOTHING);
                        if (MisfireStrategyEnum.FIRE_ONCE_NOW == misfireStrategyEnum) {
                            // FIRE_ONCE_NOW 》 trigger
                            //JobTriggerPoolHelper.trigger(jobInfo.getId(), TriggerTypeEnum.MISFIRE, -1, null, null, null);
                            taskDOList.add(buildTaskFromPeriodicJob(periodicJobDO, schdulerMark));
                            if (logger.isDebugEnabled()) {
                                logger.debug(">>>>>>>>>>> xxl-job, schedule push trigger : jobId = " + periodicJobDO.getId());
                            }
                        }
                        refreshNextValidTime(periodicJobDO, new Date());
                    } else if (nowTime >= periodicJobDO.getTriggerNextTime()) {
                        // 2.2、trigger-expire < 5s：direct-trigger && make next-trigger-time

                        // 1、trigger
                        //JobTriggerPoolHelper.trigger(jobInfo.getId(), TriggerTypeEnum.CRON, -1, null, null, null);
                        taskDOList.add(buildTaskFromPeriodicJob(periodicJobDO, schdulerMark));
                        if (logger.isDebugEnabled()) {
                            logger.debug(">>>>>>>>>>> xxl-job, schedule push trigger : jobId = " + periodicJobDO.getId());
                        }

                        // 2、fresh next
                        refreshNextValidTime(periodicJobDO, databaseTimeService.currentTime());

                        // next-trigger-time in 5s, pre-read again
                        if (periodicJobDO.getStatus() == 1 && nowTime + PRE_READ_MS > periodicJobDO.getTriggerNextTime()) {

                            // 1、make ring second
                            int ringSecond = (int) ((periodicJobDO.getTriggerNextTime() / 1000) % 60);

                            // 2、push time ring
                            //pushTimeRing(ringSecond, periodicJobDO.getId());
                            taskDOList.add(buildTaskFromPeriodicJob(periodicJobDO, schdulerMark));

                            // 3、fresh next
                            refreshNextValidTime(periodicJobDO, new Date(periodicJobDO.getTriggerNextTime()));
                        }

                    } else {
                        // 2.3、trigger-pre-read：time-ring trigger && make next-trigger-time

                        // 1、make ring second
                        int ringSecond = (int) ((periodicJobDO.getTriggerNextTime() / 1000) % 60);

                        // 2、push time ring
                        //pushTimeRing(ringSecond, periodicJobDO.getId());
                        taskDOList.add(buildTaskFromPeriodicJob(periodicJobDO, schdulerMark));

                        // 3、fresh next
                        refreshNextValidTime(periodicJobDO, new Date(periodicJobDO.getTriggerNextTime()));
                    }
                } catch (Exception e) {
                    logger.warn("scanScheduleJob error, jobId:[{}]", periodicJobDO.getId(), e);
                    periodicJobDO.setScheduleFailTimes(periodicJobDO.getScheduleFailTimes() + 1);
                    if (periodicJobDO.getScheduleFailTimes() > 10) {
                        periodicJobDO.setStatus(0);
                        // TODO 告警
                    }
                }
            }

            // 2. 针对上一次分配任务异常中断，导致任务已添加，periodicJob配置未刷新，需要在本次添加任务时进行忽略。同时刷新periodicJob配置信息
            String scheduler = lightJobMarkMapper.findMarkValue("periodic_job_scheduler");
            if (scheduler != null && !scheduler.isEmpty()) {
                // TODO 忽略上一次调度非正常中断已添加任务
                List<TaskScheduleInfo> scheduledTaskList = taskMapper.findAllTaskPlanTime(schdulerMark);
                // TODO 在scheulerMark变更前，更新上一次调度非正常退出periodicJob配置
            }

            // 3. 将新增任务与配置变更保存至持久数据库
            // 标记任务开始分配
            lightJobMarkMapper.updateMarkValue("periodic_job_scheduler", schdulerMark);
            // TODO addTaskDO to DB
            // TODO update periodicJob.nextFireTime
            // 标记任务分配完成
            lightJobMarkMapper.updateMarkValue("periodic_job_scheduler", null);
        }
        return scanedCount;
    }

    private TaskDO buildTaskFromPeriodicJob(PeriodicJobDO jobDO, String schdulerMark) {
        TaskDO taskDO = new TaskDO();
        //taskDO.setId();
        taskDO.setStatus(0);
        taskDO.setJobGroup(jobDO.getJobGroup());
        taskDO.setJobType(JobTypeEnum.PERIODIC_JOB.name());
        taskDO.setBizKey(schdulerMark);
        taskDO.setPlanTriggerTime(new Date(jobDO.getTriggerNextTime()));
        //taskDO.setExpireTime(); // TODO
        taskDO.setExecutorAddress(null);
        taskDO.setExecutorHandler(jobDO.getExecutorHandler());
        taskDO.setExecutorParam(jobDO.getExecutorParam());
        taskDO.setExecutorTimeout(jobDO.getExecutorTimeout());
        taskDO.setExecutorFailRetryCount(jobDO.getExecutorFailRetryCount());
        taskDO.setRetryDuration("20");
        taskDO.setFromJobId(jobDO.getId());
        //taskDO.setTriggerTime();
        //taskDO.setTriggerIndex();
        taskDO.setFailureTimes(0);
        //taskDO.setFinishTime();
        //taskDO.setParam();
        //taskDO.setTriggerLog();
        return taskDO;
    }

    private void refreshNextValidTime(PeriodicJobDO jobInfo, Date fromTime) throws Exception {
        Date nextValidTime = generateNextValidTime(jobInfo, fromTime);
        if (nextValidTime != null) {
            jobInfo.setTriggerLastTime(jobInfo.getTriggerNextTime());
            jobInfo.setTriggerNextTime(nextValidTime.getTime());
            jobInfo.setScheduleFailTimes(0);
        } else {
            jobInfo.setStatus(0);
            jobInfo.setTriggerLastTime(0L);
            jobInfo.setTriggerNextTime(0L);
            jobInfo.setScheduleFailTimes(0);
            logger.warn(">>>>>>>>>>> xxl-job, refreshNextValidTime fail for job: jobId={}, scheduleType={}, scheduleConf={}",
                    jobInfo.getId(), jobInfo.getScheduleType(), jobInfo.getScheduleConf());
        }
    }

    // ---------------------- tools ----------------------
    public static Date generateNextValidTime(PeriodicJobDO jobInfo, Date fromTime) throws Exception {
        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(jobInfo.getScheduleType(), null);
        if (ScheduleTypeEnum.CRON == scheduleTypeEnum) {
            Date nextValidTime = new CronExpression(jobInfo.getScheduleConf()).getNextValidTimeAfter(fromTime);
            return nextValidTime;
        } else if (ScheduleTypeEnum.FIX_RATE == scheduleTypeEnum /*|| ScheduleTypeEnum.FIX_DELAY == scheduleTypeEnum*/) {
            return new Date(fromTime.getTime() + Integer.valueOf(jobInfo.getScheduleConf()) * 1000 );
        }
        return null;
    }
}
