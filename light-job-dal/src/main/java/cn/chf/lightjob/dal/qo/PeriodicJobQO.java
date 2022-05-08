package cn.chf.lightjob.dal.qo;

import cn.chf.lightjob.model.PageRequest;

import lombok.Data;

/**
 * @description
 * @author: davy
 * @create: 2022-03-09 21:53
 */
@Data
public class PeriodicJobQO extends PageRequest {

    private String groupCode;
}
