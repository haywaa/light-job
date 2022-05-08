package cn.chf.lightjob.plugin;

import cn.chf.lightjob.dal.entity.TaskDO;

/**
 * @description
 * @author: davy
 * @create: 2022-03-04 22:26
 */
public interface ChannelPlugin {

    void notifyExecutor(TaskDO taskDO);
}
