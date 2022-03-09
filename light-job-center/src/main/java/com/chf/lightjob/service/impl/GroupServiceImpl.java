package com.chf.lightjob.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chf.lightjob.dal.entity.LightJobGroupDO;
import com.chf.lightjob.dal.mapper.LightJobGroupMapper;
import com.chf.lightjob.dal.qo.GroupQO;
import com.chf.lightjob.service.GroupService;

/**
 * @description
 * @author: davy
 * @create: 2022-03-06 14:50
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private LightJobGroupMapper lightJobGroupMapper;

    @Override
    public int countList(GroupQO groupQO) {
        // TODO
        return 0;
    }

    @Override
    public List<LightJobGroupDO> queryList(GroupQO groupQO) {
        // TODO
        return null;
    }

    @Override
    public List<LightJobGroupDO> queryAll() {
        return lightJobGroupMapper.queryAll();
    }

    @Override
    public Long addOrUpdate(LightJobGroupDO groupDO) {
        // TODO
        return null;
    }
}
