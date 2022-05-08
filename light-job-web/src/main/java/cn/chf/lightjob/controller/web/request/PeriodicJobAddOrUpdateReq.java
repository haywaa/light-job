package cn.chf.lightjob.controller.web.request;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-03-08 22:58
 */
@Data
public class PeriodicJobAddOrUpdateReq {

    /**
     * Nullable:  false
     */
    private Long id;

    /**
     * 执行器主键ID
     */
    @NotBlank(message = "请选择执行器")
    private String jobGroup;

    private String jobDesc;

    ///**
    // * 报警邮件
    // * Nullable:  true
    // */
    //private String alarmEmail;

    /**
     * 调度类型
     */
    @NotBlank(message = "请选择调度类型")
    private String scheduleType;

    /**
     * 调度配置，值含义取决于调度类型
     */
    @NotBlank(message = "请填写调度配置")
    private String scheduleConf;

    /**
     * 调度过期策略
     * Nullable:  false
     */
    @NotBlank(message = "请选择过期调度策略")
    private String misfireStrategy;

    /**
     * 阻塞处理策略
     */
    @NotBlank(message = "请选择阻塞处理策略")
    private String blockStrategy;

    /**
     * 最大重试次数
     */
    @NotNull(message = "请填写最大重试次数")
    private Integer maxRetryTimes;

    /**
     * 执行器任务handler
     * Nullable:  true
     */
    @NotBlank(message = "请填写Handler Code")
    private String executorHandler;

    /**
     * 执行器任务参数
     */
    private String executorParam;

    /**
     * 任务执行预估时长，单位秒
     * Nullable:  false
     */
    @NotNull(message = "请填写预估最大执行时长")
    @Min(value = 1, message = "预估最大执行时长需大于1")
    private Integer executorTimeout;

    ///**
    // * 子任务ID，多个逗号分隔
    // * Nullable:  true
    // */
    //private String childJobid;

    /**
     * 状态：0-停止，1-运行
     * Nullable:  false
     */
    @NotNull(message = "请选择任务状态")
    private Integer status;
}
