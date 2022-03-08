package com.chf.lightjob.service;

import com.chf.lightjob.dal.entity.PeriodicJobDO;

/**
 * @description
 * @author: davy
 * @create: 2022-03-08 23:25
 */
public interface PeriodicJobService {

    Long addOrUpdate(PeriodicJobDO periodicJobDO);
}
