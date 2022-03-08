package com.chf.lightjob.dal.entity;

import com.chf.lightjob.dal.base.BaseEntity;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * 表名：light_job_user_token
 */
@Setter
@Getter
public class LightJobUserTokenDO extends BaseEntity {
    /**
     * Nullable:  false
     */
    private Long id;

    /**
     * 用户code
     * Nullable:  false
     */
    private Long userId;

    /**
     * 密码
     * Nullable:  false
     */
    private String token;

    /**
     * 过期时间
     * Nullable:  false
     */
    private Date gmtExpire;

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