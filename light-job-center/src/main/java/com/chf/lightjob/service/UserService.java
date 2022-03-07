package com.chf.lightjob.service;

/**
 * @description
 * @author: davy
 * @create: 2022-03-05 22:31
 */
public interface UserService {

    String checkoutToken(String usercode, String password);

    //User checkUser(String token);
}
