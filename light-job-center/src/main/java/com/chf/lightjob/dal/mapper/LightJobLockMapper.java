package com.chf.lightjob.dal.mapper;

import com.chf.lightjob.dal.base.BaseMapper;
import com.chf.lightjob.dal.entity.LightJobLockDO;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LightJobLockMapper extends BaseMapper<LightJobLockDO> {

    int lockByName(@Param("lockName") String lockName);

    Date currentTime();
}