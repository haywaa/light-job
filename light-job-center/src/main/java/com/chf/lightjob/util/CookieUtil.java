package com.chf.lightjob.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.util.StringUtils;

import com.chf.lightjob.constants.WebConstants;

/**
 * @description
 * @author: davy
 * @create: 2022-03-08 21:10
 */
public class CookieUtil {

    private static final FastDateFormat expiresDateFormat= FastDateFormat.getInstance("EEE, dd MMM yyyy HH:mm:ss zzz", TimeZone.getTimeZone("GMT"));

    public static String getAdminToken(HttpServletRequest request) {
        String token = request.getHeader(WebConstants.TOKEN_HEADER);
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

    public static void addCookie(HttpServletResponse response, Cookie cookie, String sameSite) {
        // 跨域支持，sameSite为none 必须设置secure
        if ("none".equalsIgnoreCase(sameSite)) {
            cookie.setSecure(true);
        }

        StringBuilder c = new StringBuilder(64+cookie.getValue().length());

        c.append(cookie.getName());
        c.append('=');
        c.append(cookie.getValue());

        append2cookie(c,"domain",   cookie.getDomain());
        append2cookie(c,"path",     cookie.getPath());
        append2cookie(c,"SameSite", sameSite);

        if (cookie.getSecure()) {
            c.append("; secure");
        }

        if (cookie.isHttpOnly()) {
            c.append("; HttpOnly");
        }

        if (cookie.getMaxAge()>=0) {
            append2cookie(c,"Expires", getExpires(cookie.getMaxAge()));
        }

        response.addHeader("Set-Cookie", c.toString());
    }

    private static void append2cookie(StringBuilder cookie, String key, String value) {
        if (key==null ||
                value==null ||
                key.trim().equals("")
                || value.trim().equals("")) {
            return;
        }

        cookie.append("; ");
        cookie.append(key);
        cookie.append('=');
        cookie.append(value);
    }

    private static String getExpires(int maxAge) {
        if (maxAge<0) {
            return "";
        }
        Calendar expireDate = Calendar.getInstance();
        expireDate.setTime(new Date());
        expireDate.add(Calendar.SECOND,maxAge);

        return expiresDateFormat.format(expireDate);
    }

}
