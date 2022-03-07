package com.chf.lightjob.controller.web;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chf.lightjob.model.DataResult;

/**
 * @description
 * @author: davy
 * @create: 2022-03-05 22:12
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @RequestMapping("/login")
    public DataResult login(String usercode, String password, HttpServletResponse response) {
        return new DataResult();
    }
}
