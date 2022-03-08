package com.chf.lightjob.service;

import com.chf.lightjob.dal.entity.LightJobUserDO;

/**
 * @description
 * @author: davy
 * @create: 2022-03-05 22:31
 */
public interface UserService {

    LightJobUserDO authUser(String usercode, String password);

    LightJobUserDO selectById(Long userId);
}
