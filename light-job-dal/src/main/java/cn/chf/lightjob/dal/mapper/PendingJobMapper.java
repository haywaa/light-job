package cn.chf.lightjob.dal.mapper;

import org.apache.ibatis.annotations.Mapper;

import cn.chf.lightjob.dal.base.BaseMapper;
import cn.chf.lightjob.dal.entity.PendingJobDO;

@Mapper
public interface PendingJobMapper extends BaseMapper<PendingJobDO> {
}