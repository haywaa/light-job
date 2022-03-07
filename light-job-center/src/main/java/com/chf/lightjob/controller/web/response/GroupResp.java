package com.chf.lightjob.controller.web.response;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-03-06 14:46
 */
@Data
public class GroupResp {

    private Integer id;

    private String groupCode;

    private String groupName;

    private Long gmtCreate;

    private Long gmtModified;
}
