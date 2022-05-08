package cn.chf.lightjob.dal.entity;

import java.util.Date;

import cn.chf.lightjob.dal.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 表名：periodic_job
 * 表注释：周期性作业配置表
 */
@Setter
@Getter
public class PeriodicJobDO extends BaseEntity {
    /**
     * Nullable:  false
     */
    private Long id;

    /**
     * 执行器code
     * Nullable:  false
     */
    private String jobGroup;

    /**
     * Nullable:  false
     */
    private String jobDesc;

    /**
     * Nullable:  true
     */
    private Date addTime;

    /**
     * Nullable:  true
     */
    private Date updateTime;

    /**
     * 作者
     * Nullable:  true
     */
    private String author;

    /**
     * 报警邮件
     * Nullable:  true
     */
    private String alarmEmail;

    /**
     * 调度类型
     * Nullable:  false
     */
    private String scheduleType;

    /**
     * 调度配置，值含义取决于调度类型
     * Nullable:  true
     */
    private String scheduleConf;

    /**
     * 调度过期策略
     * Nullable:  false
     */
    private String misfireStrategy;

    /**
     * 阻塞处理策略
     * Nullable:  true
     */
    private String blockStrategy;

    /**
     * 最大重试次数
     * Nullable:  true
     */
    private Integer maxRetryTimes;

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
     * 子任务ID，多个逗号分隔
     * Nullable:  true
     */
    private String childJobid;

    /**
     * 状态：0-停止，1-运行
     * Nullable:  false
     */
    private Integer status;

    /**
     * 上次调度时间
     * Nullable:  false
     */
    private Long triggerLastTime;

    /**
     * 下次调度时间
     * Nullable:  false
     */
    private Long triggerNextTime;

    /**
     * 分配失败次数
     * Nullable:  false
     */
    private Integer scheduleFailTimes;
}