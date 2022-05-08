package cn.chf.lightjob.dal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.chf.lightjob.dal.base.BaseMapper;
import cn.chf.lightjob.dal.entity.LightJobMarkDO;

@Mapper
public interface LightJobMarkMapper extends BaseMapper<LightJobMarkDO> {

    String findMarkValue(@Param("markName") String markName);
    int updateMarkValue(@Param("markName") String markName, @Param("markValue") String markValue);
}