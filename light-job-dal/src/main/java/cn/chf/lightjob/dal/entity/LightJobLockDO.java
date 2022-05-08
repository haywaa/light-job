package cn.chf.lightjob.dal.entity;

import cn.chf.lightjob.dal.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 表名：light_job_lock
 */
@Setter
@Getter
public class LightJobLockDO extends BaseEntity {
    /**
     * 锁名称
     * Nullable:  false
     */
    private String lockName;
}