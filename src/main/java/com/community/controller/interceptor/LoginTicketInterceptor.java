package com.community.controller.interceptor;

import com.community.entity.User;
import com.community.service.LoginTicketServer;
import com.community.service.UserServer;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import com.community.util.CookieUtil;
import com.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;

/**
 * @author aptx
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor , CommunityConstant {

    @Autowired
    private LoginTicketServer loginTicketServer;

    @Autowired
    private UserServer userServer;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取到cookie中的ticket
        String ticket = CookieUtil.getValue(request, "loginId");
        //判断ticket
        if (ticket != null && !StringUtils.isBlank(ticket)) {
            //验证ticket
            Map<String, Object> map = loginTicketServer.verifyTicket(ticket);
            if(Objects.equals(map.get("msg"), TICKET_FIND_SUCCESS)){
                //持有User信息
                //原理，这个方法在请求处理前执行，只要当前请求为处理结束，线程就不会结束，所以后面的方法中
                //可以从线程中取获取User信息，http协议无法判断请求的前后关系，所以对与这种请求是每次都必须处理。
                hostHolder.setUser(userServer.getUserById((Integer) map.get("userId")));
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //获取用户数据，并将用户数据放入模板
        User user = hostHolder.getUser();
        if(user!=null&&modelAndView!=null){
            modelAndView.addObject("loginUser",user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //删除对象
        hostHolder.clear();
    }
}
