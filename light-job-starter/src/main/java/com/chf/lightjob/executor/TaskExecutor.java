package com.chf.lightjob.executor;

import com.chf.lightjob.model.TaskContent;

/**
 * @description
 * @author: davy
 * @create: 2022-03-08 19:41
 */
public interface TaskExecutor {

    String handlerCode();

    void execute(TaskContent taskContent) throws Exception;
}
