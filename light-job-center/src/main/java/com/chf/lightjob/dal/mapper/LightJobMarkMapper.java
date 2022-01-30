package com.chf.lightjob.dal.mapper;

import com.chf.lightjob.dal.base.BaseMapper;
import com.chf.lightjob.dal.entity.LightJobMarkDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LightJobMarkMapper extends BaseMapper<LightJobMarkDO> {

    String findMarkValue(@Param("markName") String markName);
    int updateMarkValue(@Param("markName") String markName, @Param("markValue") String markValue);
}