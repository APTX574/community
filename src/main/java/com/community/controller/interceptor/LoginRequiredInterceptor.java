package com.community.controller.interceptor;

import com.community.annotation.LoginRequired;
import com.community.entity.User;
import com.community.util.CookieUtil;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author aptx
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    HostHolder hostHolder;

    /**
     * 检查登录验证，前置检查
     *
     * @param request  请求参数
     * @param response 响应参数
     * @param handler  被拦截的请求对象
     * @return 是否继续进行请求
     * @throws Exception 错误参数
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断拦截的请求对象是否是是添加的@LoginRequested注解的方法
        if (handler instanceof HandlerMethod handlerMethod) {
            Method method = handlerMethod.getMethod();
            //获得方法的注解,判断是否包含LoginRequired注解
            LoginRequired annotation = method.getAnnotation(LoginRequired.class);
            //如果包含LoginRequest注解且hostHolder中不包含用户数据
            if (annotation != null && hostHolder.getUser() == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;

    }
}
