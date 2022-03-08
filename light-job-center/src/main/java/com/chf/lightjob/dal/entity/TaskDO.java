package com.chf.lightjob.dal.entity;

import com.chf.lightjob.dal.base.BaseEntity;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 表名：task
 */
@Setter
@Getter
public class TaskDO extends BaseEntity {
    /**
     * Nullable:  false
     */
    private Long id;

    /**
     * 状态：0-新记录（调度中），1-已调度(执行中), 2-已完成, 4-已丢弃
     * Nullable:  false
     */
    private Integer status;

    /**
     * 任务分组，等同于appName
     * Nullable:  false
     */
    private String jobGroup;

    /**
     * 任务类型：PERIODIC_JOB, DELAY_TASK
     * Nullable:  false
     */
    private String jobType;

    /**
     * 业务键，方便查询
     * Nullable:  false
     */
    private String bizKey;

    /**
     * 期望触发时间
     * Nullable:  false
     */
    private Date planTriggerTime;

    /**
     * 过期时间
     * Nullable:  false
     */
    private Date expireTime;

    /**
     * 触发过期时间，MQ消息的过期时间
     * Nullable:  false
     */
    private Date expireTriggerTime;

    /**
     * 阻塞处理策略
     * Nullable:  true
     */
    private String blockStrategy;

    /**
     * 执行器地址，本次执行的地址
     * Nullable:  true
     */
    private String executorAddress;

    /**
     * 执行器任务handler
     * Nullable:  true
     */
    private String executorHandler;

    /**
     * 执行器任务参数
     * Nullable:  true
     */
    private String executorParam;

    /**
     * 任务执行预估时长，单位秒
     * Nullable:  false
     */
    private Integer executorTimeout;

    /**
     * 失败重试次数
     * Nullable:  false
     */
    private Integer executorFailRetryCount;

    /**
     * 最大重试次数
     * Nullable:  true
     */
    private Integer maxRetryTimes;

    /**
     * periodic_job_id、pending_job_id等
     * Nullable:  true
     */
    private Long fromJobId;

    /**
     * 实际触发时间
     * Nullable:  true
     */
    private Date triggerTime;

    /**
     * 实际触发次数
     * Nullable:  true
     */
    private Integer triggerIndex;

    /**
     * 执行失败次数，执行器回调执行失败才算1次
     * Nullable:  true
     */
    private Integer failureTimes;

    /**
     * 实际完成时间
     * Nullable:  true
     */
    private Date finishTime;

    /**
     * 调度-日志
     * Nullable:  true
     */
    private String triggerLog;
}