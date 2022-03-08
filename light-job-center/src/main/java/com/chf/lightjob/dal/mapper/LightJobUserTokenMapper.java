package com.chf.lightjob.dal.mapper;

import com.chf.lightjob.dal.base.BaseMapper;
import com.chf.lightjob.dal.entity.LightJobUserTokenDO;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LightJobUserTokenMapper extends BaseMapper<LightJobUserTokenDO> {

    LightJobUserTokenDO selectByToken(@Param("token") String token);

    int add(LightJobUserTokenDO tokenDO);
}