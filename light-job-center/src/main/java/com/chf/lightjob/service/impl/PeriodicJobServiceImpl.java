package com.chf.lightjob.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chf.lightjob.dal.entity.PeriodicJobDO;
import com.chf.lightjob.dal.mapper.PeriodicJobMapper;
import com.chf.lightjob.dal.qo.PeriodicJobQO;
import com.chf.lightjob.scheduler.PeriodicJobScheduler;
import com.chf.lightjob.service.PeriodicJobService;
import com.chf.lightjob.util.AssertBiz;

/**
 * @description
 * @author: davy
 * @create: 2022-03-09 18:07
 */
@Service
public class PeriodicJobServiceImpl implements PeriodicJobService {

    @Autowired
    private PeriodicJobMapper periodicJobMapper;

    @Override
    public Long addOrUpdate(PeriodicJobDO periodicJobDO) {
        if (periodicJobDO.getId() != null) {
            checkUpdateInfo(periodicJobDO);
            periodicJobMapper.updateByPrimaryKey(periodicJobDO);
            return periodicJobDO.getId();
        }
        periodicJobDO.setTriggerNextTime(PeriodicJobScheduler.generateNextValidTime(periodicJobDO, new Date()).getTime());
        periodicJobDO.setScheduleFailTimes(0);
        periodicJobMapper.addJob(periodicJobDO);
        return periodicJobDO.getId();
    }

    private void checkUpdateInfo(PeriodicJobDO periodicJobDO) {
        PeriodicJobDO dbJob = periodicJobMapper.selectById(periodicJobDO.getId());
        AssertBiz.notNull(dbJob, "无效的任务ID");
    }

    @Override
    public int countByQuery(PeriodicJobQO qo) {
        return periodicJobMapper.countByQuery(qo);
    }

    @Override
    public List<PeriodicJobDO> listByQuery(PeriodicJobQO qo) {
        return periodicJobMapper.listByQuery(qo);
    }
}
