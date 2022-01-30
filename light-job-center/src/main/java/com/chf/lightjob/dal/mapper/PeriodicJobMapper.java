package com.chf.lightjob.dal.mapper;

import com.chf.lightjob.dal.base.BaseMapper;
import com.chf.lightjob.dal.entity.PeriodicJobDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PeriodicJobMapper extends BaseMapper<PeriodicJobDO> {

    List<PeriodicJobDO> selectNowScheduleJob(@Param("preReadSecond") int preReadSecond, @Param("pagesize") int pagesize);
}