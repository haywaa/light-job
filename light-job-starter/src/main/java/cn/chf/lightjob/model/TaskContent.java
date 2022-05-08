package cn.chf.lightjob.model;

import java.util.Date;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-01-28 19:38
 */
@Data
public class TaskContent {

    private Long taskId;

    private Long jobId;

    /**
     * 发送一次MQ，triggerIndex都会+1
     */
    private Integer triggerIndex;

    private String jobHandler;

    private Long logId;

    private String blockStrategy;

    // 消息过期时间，过期的消息请勿处理
    private Date expireTime;
}
