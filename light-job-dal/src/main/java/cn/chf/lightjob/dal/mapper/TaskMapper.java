package cn.chf.lightjob.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.chf.lightjob.dal.base.BaseMapper;
import cn.chf.lightjob.dal.entity.TaskDO;
import cn.chf.lightjob.dal.entity.TaskScheduleInfo;

@Mapper
public interface TaskMapper extends BaseMapper<TaskDO> {

    List<TaskScheduleInfo> findAllTaskPlanTime(@Param("bizKey") String bizKey);

    List<TaskDO> findAllTaskByBizKey(@Param("bizKey") String bizKey);

    int batchAdd(@Param("list") List<TaskDO> list);

    TaskDO selectFirstUnfinishedTaskForJob(@Param("jobType") String jobType, @Param("fromJobId") Long fromJobId);

    // TODO
    int updateById(@Param("task") TaskDO taskDO);

    void markTaskSuccess(@Param("taskIds") List<Long> taskIds);
}