package com.chf.lightjob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chf.lightjob.constants.ResultCode;
import com.chf.lightjob.dal.entity.LightJobUserDO;
import com.chf.lightjob.dal.mapper.LightJobUserMapper;
import com.chf.lightjob.exception.BizException;
import com.chf.lightjob.service.UserService;
import com.chf.lightjob.util.PasswordeUtil;

/**
 * @description
 * @author: davy
 * @create: 2022-03-08 21:34
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private LightJobUserMapper lightJobUserMappe;

    @Override
    public LightJobUserDO authUser(String usercode, String password) {
        LightJobUserDO userDO = lightJobUserMappe.selectByUsercode(usercode);
        if (userDO == null || !PasswordeUtil.checkPasswordMatch(password, userDO.getPassword())) {
            throw new BizException(ResultCode.INVALID_USER_OR_PASSWORD, "无效的账号或密码");
        }
        return userDO;
    }
}
