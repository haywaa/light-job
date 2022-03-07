package com.chf.lightjob.service;

import java.util.List;

import com.chf.lightjob.dal.entity.LightJobGroupDO;
import com.chf.lightjob.dal.qo.GroupQO;

/**
 * @description
 * @author: davy
 * @create: 2022-03-06 14:27
 */
public interface GroupService {

    int countList(GroupQO groupQO);

    List<LightJobGroupDO> queryList(GroupQO groupQO);

    Long addOrUpdate(LightJobGroupDO groupDO);
}
