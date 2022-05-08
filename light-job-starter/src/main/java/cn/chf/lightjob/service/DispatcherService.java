package cn.chf.lightjob.service;

import cn.chf.lightjob.model.TaskContent;

/**
 * @description
 * @author: davy
 * @create: 2022-03-04 22:21
 */
public interface DispatcherService {

    void dispatchTask(TaskContent taskContent) throws Exception;
}
