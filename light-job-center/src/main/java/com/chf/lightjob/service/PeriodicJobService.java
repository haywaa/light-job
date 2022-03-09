package com.chf.lightjob.service;

import java.util.List;

import com.chf.lightjob.dal.entity.PeriodicJobDO;
import com.chf.lightjob.dal.qo.PeriodicJobQO;

/**
 * @description
 * @author: davy
 * @create: 2022-03-08 23:25
 */
public interface PeriodicJobService {

    Long addOrUpdate(PeriodicJobDO periodicJobDO);

    int countByQuery(PeriodicJobQO qo);

    List<PeriodicJobDO> listByQuery(PeriodicJobQO qo);
}
