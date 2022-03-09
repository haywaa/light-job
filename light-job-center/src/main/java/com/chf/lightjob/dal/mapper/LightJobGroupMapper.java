package com.chf.lightjob.dal.mapper;

import com.chf.lightjob.dal.base.BaseMapper;
import com.chf.lightjob.dal.entity.LightJobGroupDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LightJobGroupMapper extends BaseMapper<LightJobGroupDO> {

    List<LightJobGroupDO> queryAll();
}