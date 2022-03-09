package com.chf.lightjob.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chf.lightjob.constants.ErrorCode;
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
    private LightJobUserMapper lightJobUserMapper;

    @Override
    public LightJobUserDO authUser(String usercode, String password) {
        LightJobUserDO userDO = lightJobUserMapper.selectByUsercode(usercode);
        if (userDO == null || !PasswordeUtil.checkPasswordMatch(password, userDO.getPassword())) {
            throw new BizException(ErrorCode.INVALID_USER_OR_PASSWORD.getErrorNo(), "无效的账号或密码");
        }
        return userDO;
    }

    @Override
    public LightJobUserDO selectById(Long userId) {
        if (userId == null) {
            return null;
        }
        return lightJobUserMapper.selectById(userId);
    }
}
