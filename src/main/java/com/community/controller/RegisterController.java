package com.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author aptx
 */
@Controller
public class RegisterController {

    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        System.out.println("访问了RegisterController");
        return "/site/register";
    }
}
