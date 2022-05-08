package cn.chf.lightjob.dal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.chf.lightjob.dal.base.BaseMapper;
import cn.chf.lightjob.dal.entity.LightJobUserDO;

@Mapper
public interface LightJobUserMapper extends BaseMapper<LightJobUserDO> {

    LightJobUserDO selectByUsercode(@Param("usercode") String usercode);
    LightJobUserDO selectById(@Param("userId") long userId);
}