package com.chf.lightjob.dal.mapper;

import com.chf.lightjob.dal.base.BaseMapper;
import com.chf.lightjob.dal.entity.TaskDO;
import com.chf.lightjob.dal.entity.TaskScheduleInfo;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TaskMapper extends BaseMapper<TaskDO> {

    List<TaskScheduleInfo> findAllTaskPlanTime(@Param("bizType") String bizType);
}