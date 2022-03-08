package com.chf.lightjob.dal.entity;

import com.chf.lightjob.dal.base.BaseEntity;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 表名：light_job_user
 */
@Setter
@Getter
public class LightJobUserDO extends BaseEntity {
    /**
     * Nullable:  false
     */
    private Long id;

    /**
     * 用户code
     * Nullable:  false
     */
    private String usercode;

    /**
     * 密码
     * Nullable:  false
     */
    private String password;

    /**
     * 用户名称
     * Nullable:  true
     */
    private String userName;

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