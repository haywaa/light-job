package cn.chf.lightjob.dal.entity;

import java.util.Date;

import cn.chf.lightjob.dal.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 表名：light_job_group
 * 表注释：执行器
 */
@Setter
@Getter
public class LightJobGroupDO extends BaseEntity {
    /**
     * Nullable:  false
     */
    private Integer id;

    /**
     * 执行器Code
     * Nullable:  false
     */
    private String groupCode;

    /**
     * 执行器名称
     * Nullable:  false
     */
    private String groupName;

    /**
     * 创建时间
     * Nullable:  false
     */
    private Date gmtCreate;

    /**
     * 最近更新时间
     * Nullable:  false
     */
    private Date gmtModified;
}