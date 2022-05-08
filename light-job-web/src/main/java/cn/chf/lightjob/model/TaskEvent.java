package cn.chf.lightjob.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/**
 * @description
 * @author: davy
 * @create: 2022-03-04 22:34
 */
@Getter
@Setter
public class TaskEvent {

    /**
     * 事件过期时间，已过期事件不要不要处理
     */
    private Date expireTime;

    private Long taskId;

    private String taskCode;

    private String params;
}
