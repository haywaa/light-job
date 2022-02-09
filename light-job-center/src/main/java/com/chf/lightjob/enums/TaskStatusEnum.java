package com.chf.lightjob.enums;

/**
 * @description
 * @author: davy
 * @create: 2022-02-09 16:20
 */
public enum TaskStatusEnum {

    CREATED, //
    QUEUED, // 已排队 (周期性延时任务，支持阻塞策略会进行排队处理）
    EXECUTING, // 执行中
    FINISHED, // 已完成
    DISCARD, // 已忽略
}
