package cn.chf.lightjob.dal.entity;

import cn.chf.lightjob.dal.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 表名：light_job_mark
 */
@Setter
@Getter
public class LightJobMarkDO extends BaseEntity {
    /**
     * 标记名称
     * Nullable:  false
     */
    private String markName;

    /**
     * 标记值
     * Nullable:  true
     */
    private String markValue;
}