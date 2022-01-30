package com.chf.lightjob.dal.entity;

import com.chf.lightjob.dal.base.BaseEntity;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 表名：pending_job
 * 表注释：延时任务队列表
 */
@Setter
@Getter
public class PendingJobDO extends BaseEntity {
    /**
     * Nullable:  false
     */
    private Long id;

    /**
     * 执行器主键ID
     * Nullable:  false
     */
    private String jobGroup;

    /**
     * Nullable:  false
     */
    private String jobDesc;

    /**
     * Nullable:  false
     */
    private Date addTime;

    /**
     * Nullable:  false
     */
    private Date updateTime;

    /**
     * 计划触发时间
     * Nullable:  false
     */
    private Date planTriggerTime;

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
     * 任务执行超时时间，单位秒
     * Nullable:  false
     */
    private Integer executorTimeout;

    /**
     * 失败重试次数
     * Nullable:  false
     */
    private Integer executorFailRetryCount;

    /**
     * 重试时间间隔, 单位秒, 逗号分隔
     * Nullable:  true
     */
    private String retryDuration;

    /**
     * 0-待触发， 1-已触发
     * Nullable:  true
     */
    private Integer triggerMark;
}