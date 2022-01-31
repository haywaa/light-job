package com.chf.lightjob.service;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.PostConstruct;

import com.chf.lightjob.dal.entity.TaskDO;

/**
 * @description
 * @author: davy
 * @create: 2022-01-31 12:40
 */
public interface TaskTriggerService {

    void trigger(List<TaskDO> taskDOList);

}
