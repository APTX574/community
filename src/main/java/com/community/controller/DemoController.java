package com.community.controller;

import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aptx
 */
@Controller
@RequestMapping("/demo")
public class DemoController {
    public String str="HelloWorld SpringBoot";
    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello(){
        return str;
    }
    @RequestMapping("/newcode")
    public void newCode(HttpServletRequest request, HttpServletResponse response){
        System.out.println(request.getContextPath());
        response.setContentType("text/html;Charset=utf-8");
        try (PrintWriter writer= response.getWriter()){
            writer.write("<h1>newCode<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(path = "/getid1/{id}",method = RequestMethod.GET)
    public void getId1(@PathVariable("id") int id ,HttpServletResponse response,HttpServletRequest request){
        response.setContentType("text/html;Charset=utf-8");
        try(PrintWriter writer=response.getWriter()) {
            writer.write("<h1>"+id+"<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(path = "/getid2",method = RequestMethod.GET)
    public void getId2(@PathParam("id") int id , HttpServletResponse response, HttpServletRequest request){
        response.setContentType("text/html;Charset=utf-8");
        try(PrintWriter writer=response.getWriter()) {
            writer.write("<h1>"+id+"<h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/show")
    public ModelAndView show(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("name","张三");
        modelAndView.addObject("age",12);
        modelAndView.setViewName("/demo/demo.html");
        return modelAndView;
    }
    @RequestMapping("/show2")
    public String show2(Model model){
     model.addAttribute("name","李四");
     model.addAttribute("age",13);
     return "demo/demo.html";
    }
    //测试json
    @RequestMapping("/json")
    @ResponseBody
    public Map json(){
        Map<String,String> map=new HashMap<>();
        map.put("1","id1");
        map.put("2","id2");
        return map;
    }
    @RequestMapping(path =  "/json2",method = RequestMethod.POST)
    @ResponseBody
    public Map json2(@RequestBody Map<String,String> map){
        System.out.println(map.get("id"));
        return map;
    }

}
