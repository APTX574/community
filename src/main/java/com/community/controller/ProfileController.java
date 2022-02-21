package com.community.controller;

import com.community.entity.User;
import com.community.service.UserServer;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author aptx
 */
@Controller
public class ProfileController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserServer userServer;

    @RequestMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable(value = "userId")int userId, Model model){
        User user = userServer.getUserById(userId);
        model.addAttribute("user",user);
        System.out.println(user.getCreatTime());
        return"/site/profile";
    }
}
