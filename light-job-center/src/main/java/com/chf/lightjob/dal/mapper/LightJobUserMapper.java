package com.chf.lightjob.dal.mapper;

import com.chf.lightjob.dal.base.BaseMapper;
import com.chf.lightjob.dal.entity.LightJobUserDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LightJobUserMapper extends BaseMapper<LightJobUserDO> {

    LightJobUserDO selectByUsercode(@Param("usercode") String usercode);
    LightJobUserDO selectById(@Param("userId") long userId);
}