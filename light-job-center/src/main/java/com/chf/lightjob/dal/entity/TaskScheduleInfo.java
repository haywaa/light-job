package com.chf.lightjob.dal.entity;

import java.util.Date;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-01-30 22:28
 */
@Data
public class TaskScheduleInfo {

    private Long fromJobId;

    /**
     * 期望触发时间
     * Nullable:  false
     */
    private Date planTriggerTime;
}
