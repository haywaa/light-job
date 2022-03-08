package com.chf.lightjob.controller.web;

import java.util.Date;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chf.lightjob.annotation.LoginNotRequired;
import com.chf.lightjob.constants.WebConstants;
import com.chf.lightjob.controller.web.intercepter.WebSessionFilter;
import com.chf.lightjob.controller.web.response.UserResp;
import com.chf.lightjob.dal.entity.LightJobUserDO;
import com.chf.lightjob.dal.entity.LightJobUserTokenDO;
import com.chf.lightjob.dal.mapper.LightJobUserTokenMapper;
import com.chf.lightjob.model.DataResult;
import com.chf.lightjob.service.UserService;
import com.chf.lightjob.util.CookieUtil;

/**
 * @description
 * @author: davy
 * @create: 2022-03-05 22:12
 */
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LightJobUserTokenMapper lightJobUserTokenMapper;

    @LoginNotRequired
    @RequestMapping(value = "/v1/loginWithPassword")
    public DataResult<UserResp> loginWithPassword(
            //            @ApiParam(value = "组织ID", required = true, defaultValue = "0") Long orgId,
            @RequestParam("usercode") String usercode,
            @RequestParam("password") String password,
            //            @ApiParam(value = "验证码", required = true) String captcha,
            HttpServletRequest request, HttpServletResponse response) {
        //            if (!CaptchaHelper.validate(request, captcha)) {
        //                request.setAttribute("errorMessage", "验证码不正确");
        //                return Result.Failure("验证码不正确");
        //            }

        String domain = request.getServerName();
        //DomainConfig config = getDomainConfig(domain); mbk-+ii7.




        //if (config == null) {
        //    return ResultUtil.resultWithError(ErrorCode.PERMISSION_DENIED);
        //}

        // console的用户属于平台用户，授予租户权限
        //Result<UserDO> userResult = userService.auth(getIpAddr(request), mobile, PasswordeEncrypt.encrypt(password));
        LightJobUserDO lightJobUserDO = userService.authUser(usercode, password);
        String token = UUID.randomUUID().toString().replace("-", "");

        LightJobUserTokenDO tokenDO = new LightJobUserTokenDO();
        tokenDO.setToken(token);
        tokenDO.setUserId(lightJobUserDO.getId());
        tokenDO.setGmtExpire(DateUtils.addHours(new Date(), 24));
        tokenDO.setGmtCreate(new Date());
        tokenDO.setGmtModified(new Date());
        lightJobUserTokenMapper.add(tokenDO);
        addTokenInCookie(WebConstants.TOKEN_KEY, token, request, response);
        return DataResult.success(convertDO2Resp(lightJobUserDO));
    }

    @GetMapping(value = "/v1/currentUser")
    public DataResult<UserResp> currentUser() {
        Long userId = WebSessionFilter.UserInfoResource.getUserId();
        LightJobUserDO lightJobUserDO = userService.selectById(userId);
        return DataResult.success(convertDO2Resp(lightJobUserDO));
    }

    /**
     * Cookie添加
     */
    private static void addTokenInCookie(String cookieKey, String cookieValue, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(cookieValue)) {
            return;
        }
        Cookie cookie = new Cookie(cookieKey, cookieValue);

        /*
         * 跨域种一级域名cookie, 客户端XMLHttpRequest：withCredentials改为true；
         * umi-request (fetch.js): credentials 为include 参考 https://github.github.io/fetch/#credentials
         */
        if (!"true".equals(request.getHeader("localhost"))) {
            String domain = request.getServerName();
            String firstDomain = getFirstClassDomain(domain);
            if (firstDomain != null
                    && !firstDomain.equals("127.0.0.1")
                    && !firstDomain.equals("localhost")) {
                // IP登录不处理
                cookie.setDomain(firstDomain);
            }
        }

        if (cookie.getPath() == null) {
            cookie.setPath("/");
        }

        //        cookie.setMaxAge();

        if ("https".equals(request.getScheme())) {
            cookie.setSecure(true);
        }

        //        cookie.setHttpOnly(true);
        // 不能添加SameSite，Chrome中跨域访问时不能正常设置Cookie
        //        response.addCookie(cookie);
        CookieUtil.addCookie(response, cookie, "None");
        response.addHeader(WebConstants.TOKEN_HEADER, cookieValue);
        response.setHeader("Access-Control-Expose-Headers", "x-hbtoken");
    }

    private static String getFirstClassDomain(String domain) {
        if ("127.0.0.1".equals(domain) || "localhost".equals(domain)) {
            // IP请求（未使用域名）不做处理
            return domain;
        }

        return domain;
    }

    private static UserResp convertDO2Resp(LightJobUserDO userDO) {
        if (userDO == null) {
            return null;
        }

        UserResp userResp = new UserResp();
        userResp.setId(userDO.getId());
        userResp.setUsercode(userDO.getUsercode());
        userResp.setUserName(userDO.getUserName());
        userResp.setGmtCreate(userDO.getGmtCreate());
        userResp.setGmtModified(userDO.getGmtModified());
        return userResp;
    }
}
