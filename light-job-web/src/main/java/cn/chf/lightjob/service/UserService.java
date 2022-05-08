package cn.chf.lightjob.service;

import cn.chf.lightjob.dal.entity.LightJobUserDO;

/**
 * @description
 * @author: davy
 * @create: 2022-03-05 22:31
 */
public interface UserService {

    LightJobUserDO authUser(String usercode, String password);

    LightJobUserDO selectById(Long userId);
}
