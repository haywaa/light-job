package com.chf.lightjob.controller.web.intercepter;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.chf.lightjob.annotation.LoginNotRequired;
import com.chf.lightjob.constants.ErrorCode;
import com.chf.lightjob.dal.entity.LightJobUserTokenDO;
import com.chf.lightjob.dal.mapper.LightJobUserTokenMapper;
import com.chf.lightjob.util.CookieUtil;

/**
 * @description
 * @author: davy
 * @create: 2022-03-08 21:00
 */
@Component
public class WebSessionFilter implements HandlerInterceptor {

    @Autowired
    private LightJobUserTokenMapper lightJobUserTokenMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        String servletPath = request.getServletPath();
        if (!servletPath.startsWith("/admin/")) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        String token = CookieUtil.getAdminToken(request);
        if (StringUtils.hasText(token)) {
            LightJobUserTokenDO tokenDO = lightJobUserTokenMapper.selectByToken(token);
            if (tokenDO != null) {
                UserInfoResource.USER_ID.set(tokenDO.getUserId());
            }
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginNotRequired loginNotRequired = handlerMethod.getMethodAnnotation(LoginNotRequired.class);
        if (loginNotRequired == null) {
            // 用户ID存在，微信ID不存在也是登录了
            if (UserInfoResource.USER_ID.get() == null) {
                responseJson(response, ErrorCode.AUTH_UNAUTHORIZED.getErrorNo(), "请登录");
                return false;
            }
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    protected void responseJson(HttpServletResponse response, int code, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(200);
        PrintWriter writer = response.getWriter();
        writer.write(new StringBuilder().append("{\"errorCode\":").append(code).append(",\"errorMsg\":\"").append(message)
                .append("\"}").toString());
        writer.flush();
        writer.close();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserInfoResource.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    public static abstract class UserInfoResource {
        static final ThreadLocal<Long> USER_ID = new ThreadLocal();
        static void clear() {
            USER_ID.remove();
        }

        public static Long getUserId() {
            return USER_ID.get();
        }
    }
}
