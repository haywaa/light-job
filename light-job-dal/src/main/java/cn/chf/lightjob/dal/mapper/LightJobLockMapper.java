package cn.chf.lightjob.dal.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.chf.lightjob.dal.base.BaseMapper;
import cn.chf.lightjob.dal.entity.LightJobLockDO;

@Mapper
public interface LightJobLockMapper extends BaseMapper<LightJobLockDO> {

    int lockByName(@Param("lockName") String lockName);

    Date currentTime();
}