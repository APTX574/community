package com.community.controller;

import com.community.entity.User;
import com.community.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author aptx
 */
@Controller
public class LoginController {

    @Autowired
    private UserServer userServer;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {
        return "site/register";
    }
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> registerMsg = userServer.register(user);
        if (registerMsg == null || registerMsg.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们发送了验证邮件，请尽快激活");
            model.addAttribute("target", "/index");
            return "site/operate-result";
        }
        model.addAttribute("user",user);
        model.addAttribute("usernameMsg",registerMsg.get("usernameMsg"));
        model.addAttribute("emailMsg",registerMsg.get("emailMsg"));
        model.addAttribute("passwordMsg",registerMsg.get("passwordMsg"));
        return "site/register";

    }

    @RequestMapping(path = "/activation/{id}/{code}", method = RequestMethod.GET)
    public void activation(@PathVariable("id") int id, @PathVariable("code") String code) {
        userServer.activation(id, code);
    }


}
