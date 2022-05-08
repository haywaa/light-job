package cn.chf.lightjob.dal.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import cn.chf.lightjob.dal.base.BaseMapper;
import cn.chf.lightjob.dal.entity.LightJobUserTokenDO;

@Mapper
public interface LightJobUserTokenMapper extends BaseMapper<LightJobUserTokenDO> {

    LightJobUserTokenDO selectByToken(@Param("token") String token);

    int add(LightJobUserTokenDO tokenDO);
}