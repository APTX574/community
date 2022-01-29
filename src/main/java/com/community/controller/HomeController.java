package com.community.controller;

import com.community.entity.DiscussPost;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.DiscussPostServer;
import com.community.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.websocket.server.PathParam;
import java.util.*;

/**
 * @author aptx
 */
@Controller
public class HomeController {
    @Autowired
    public DiscussPostServer discussPostServer;
    @Autowired
    public UserServer userServer;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView getIndexPage(@RequestParam(value = "current", defaultValue = "1")
                                             int current, @RequestParam(value = "limit", defaultValue = "5") int limit) {

        Page page = new Page();
        page.setCurrent(current);
        page.setLimit(limit);
        page.setPath("/index");
        page.setRows(discussPostServer.findDiscussPostSize(0));

        ModelAndView modelAndView = new ModelAndView();

        List<DiscussPost> discussPosts = discussPostServer.findDiscussPosts(0, page.getOffset(), page.getLimit());
        List<Map<String, Object>> posts = new ArrayList<>();
        if (discussPosts != null) {
            for (DiscussPost post : discussPosts) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("post", post);
                User user = userServer.getUserById(post.getUserId());
                map.put("user", user);
                posts.add(map);
            }
        }
        modelAndView.setViewName("index");
        modelAndView.addObject("page", page);
        modelAndView.addObject("discussPosts", posts);
        return modelAndView;
    }

}
