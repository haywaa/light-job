package com.chf.lightjob.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.chf.lightjob.constants.WebConstants;

/**
 * @description
 * @author: davy
 * @create: 2022-03-08 21:10
 */
public class CookieUtil {

    public static String getAdminToken(HttpServletRequest request) {
        String token = request.getHeader("x-lj-token");
        if (StringUtils.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return null;
            }

            String cookieKey = WebConstants.TOKEN_KEY;
            for (Cookie cookie : cookies) {
                if (cookieKey.equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }
}
