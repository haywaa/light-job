package cn.chf.lightjob.dal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import cn.chf.lightjob.dal.base.BaseMapper;
import cn.chf.lightjob.dal.entity.LightJobGroupDO;

@Mapper
public interface LightJobGroupMapper extends BaseMapper<LightJobGroupDO> {

    List<LightJobGroupDO> queryAll();
}