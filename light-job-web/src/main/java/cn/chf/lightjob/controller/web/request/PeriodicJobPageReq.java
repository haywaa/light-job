package cn.chf.lightjob.controller.web.request;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-03-08 23:39
 */
@Data
public class PeriodicJobPageReq extends PageRequest {

    private String groupCode;
}
