package com.community.controller;

import com.community.entity.LoginTicket;
import com.community.entity.User;
import com.community.service.LoginTicketServer;
import com.community.service.UserServer;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import com.community.util.CookieUtil;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * @author aptx
 */
@Controller
public class LoginController implements CommunityConstant {

    @Autowired
    private LoginTicketServer loginTicketServer;

    @Autowired
    private UserServer userServer;

    @Autowired
    private Producer producer;

    /**
     * 获取注册页面的模板
     *
     * @return 注册页面模板
     * @ResquestMethod Get
     * @RequestPath /register
     */
    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "/site/register";
    }

    /**
     * 获取登录页面的模板
     *
     * @return 登录页面模板
     * @ResquestMethod Get
     * @RequestPath /login
     */
    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage(HttpServletRequest request) {

        return "/site/login";


    }

    /**
     * 处理注册请求
     *
     * @param model 数据模型
     * @param user  前端页面post来的user对象，含有用户注册信息
     * @return 注册失败返回注册页面，成功则返会成功页面
     * @ResquestMethod Post
     * @RequestPath /register
     */
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> registerMsg = userServer.register(user);
        if (registerMsg == null || registerMsg.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们发送了验证邮件，请尽快激活");
            model.addAttribute("target", "/index");
            return "site/operate-result";
        }
        model.addAttribute("user", user);
        model.addAttribute("usernameMsg", registerMsg.get("usernameMsg"));
        model.addAttribute("emailMsg", registerMsg.get("emailMsg"));
        model.addAttribute("passwordMsg", registerMsg.get("passwordMsg"));
        return "site/register";

    }

    /**
     * 处理激活注册请求
     *
     * @param id    激活用户id
     * @param code  激活用户激活码
     * @param model 数据模型
     * @return 视图模板，
     * @ResquestMethod Get
     * @RequestPath /activation/{id}/{code}
     */
    @RequestMapping(path = "/activation/{id}/{code}", method = RequestMethod.GET)
    public String activation(@PathVariable("id") int id, @PathVariable("code") String code, Model model) {
        int msg = userServer.activation(id, code);
        if (msg == ACTIVATION_FAIL) {
            model.addAttribute("msg", "激活失败");
        }
        if (msg == ACTIVATION_REPEAT) {
            model.addAttribute("msg", "重复激活");
        }
        if (msg == ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功");
        }
        model.addAttribute("target", "/index");
        return "site/operate-result";

    }

    /**
     * 处理登录验证逻辑
     *
     * @param model      数据模型
     * @param user       前端页面post过来的user数据，包含账户和密码
     * @param code       前端传来的验证码
     * @param session    访问的回话对象，期中存储这这次会话的验证码信息
     * @param rememberMe 是否记住登录
     * @return 视图模板
     * @ResquestMethod Post
     * @RequestPath /login
     */
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(Model model, User user, String code, HttpSession session, boolean rememberMe, HttpServletResponse response) {
        String code1 = session.getAttribute("code").toString().toLowerCase(Locale.ROOT);
        if (!code1.equals(code.toLowerCase(Locale.ROOT))) {
            model.addAttribute("codeMsg", "验证码输入错误");
            model.addAttribute("user", user);
            return "/site/login";
        }

        Map<String, Object> map = userServer.login(user.getUsername(), user.getPassword(), rememberMe);

        int result = (int) map.get("msg");
        if (result == LOGIN_SUCCESS) {
            //添加cookie
            Cookie cookie = new Cookie("loginId", (String) map.get("ticket"));
            cookie.setMaxAge(60 * 60 * 24 * 7);
            response.addCookie(cookie);
//            model.addAttribute("msg", "登录成功，即将跳转");
//            model.addAttribute("target", "/index");

            //重定向到首页
            return "redirect:/index";
        }
        if (result == LOGIN_PASSWORD_ERROR) {
            model.addAttribute("user", user);
            model.addAttribute("passwordMsg", "密码错误");
            return "/site/login";
        }
        if (result == LOGIN_NOT_ACTIVATION) {
            model.addAttribute("usernameMsg", "该账号为激活，请激活后登录");
            model.addAttribute("user", user);
            return "/site/login";
        }
        model.addAttribute("usernameMsg", "该账号不存在");
        model.addAttribute("user", user);
        return "/site/login";

    }

    /**
     * 设置页面退出
     * 1. 将cookie对于的ticket状态设置为无效（1）
     * 2. 将cookie删除（有效时间设为0）
     * @param ticket cookie中所附带ticket
     * @param response 响应对象，用于设置cookie
     * @return 重定向至主页面
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        String ticket=CookieUtil.getValue(request,"loginId");
        if(ticket!=null){
            userServer.logout(ticket);
            Cookie cookie=new Cookie("loginId",ticket);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return "redirect:index";
    }

    /**
     * 获取验证码
     *
     * @param response 请求响应对象，用于传输图片流
     * @param session  会话对象，用于存储当前的验证码答案
     * @ResquestMethod Get
     * @RequestPath /kaptcha
     */
    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getImage(HttpServletResponse response, HttpSession session) {
        //生成验证码
        String code = producer.createText();
        //获取图片
        BufferedImage image = producer.createImage(code);
        //保存到回话
        session.setAttribute("code", code);
        //返回图片
        response.setContentType("image/png");
        try {
            OutputStream outputStream = response.getOutputStream();
            ImageIO.write(image, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}
