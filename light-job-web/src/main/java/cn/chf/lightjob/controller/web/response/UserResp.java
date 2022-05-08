package cn.chf.lightjob.controller.web.response;

import java.util.Date;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-03-08 22:09
 */
@Data
public class UserResp {

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
