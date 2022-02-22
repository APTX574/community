package com.community.controller;

import com.community.entity.DiscussPost;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.CommentService;
import com.community.service.DiscussPostServer;
import com.community.service.UserServer;
import com.community.util.CommunityConstant;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aptx
 */
@Controller
public class HomeController implements CommunityConstant {
    @Autowired
    private DiscussPostServer discussPostServer;
    @Autowired
    private UserServer userServer;
    @Autowired
    private CommentService commentService;




    @RequestMapping("/")
    public void  gen(HttpServletResponse response){
        try {
            response.sendRedirect("http://localhost:8080/index");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * 获取current页面的数据
     *
     * @param current 当前页数
     * @param limit   每页的显示数据
     * @return 数据
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView getIndexPage(@RequestParam(value = "current", defaultValue = "1")
                                             int current, @RequestParam(value = "limit", defaultValue = "5") int limit) {

        Page page = new Page();
        page.setCurrent(current);
        page.setLimit(limit);
        //设置页面的路径，可以复用
        page.setPath("/index");
        //设置总行数
        page.setRows(discussPostServer.findDiscussPostSize(0));

        ModelAndView modelAndView = new ModelAndView();

        //获取总的帖子的列表
        List<DiscussPost> discussPosts = discussPostServer.findDiscussPosts(0, page.getOffset(), page.getLimit());
        //将帖子用户数据拼接
        List<Map<String, Object>> posts = new ArrayList<>();
        //起始也可以将这部分封装，用于用户获取帖子数据
        if (discussPosts != null) {
            for (DiscussPost post : discussPosts) {
                Map<String, Object> map = new HashMap<>(2);
                map.put("post", post);
                User user = userServer.getUserById(post.getUserId());
                map.put("user", user);
                int replyCount=commentService.findCountByEntity(ENTITY_TYPE_POST,post.getId());
                map.put("replyCount",replyCount);
                posts.add(map);

            }
        }
        //将数据和页面数据方法。
        modelAndView.setViewName("index");
        modelAndView.addObject("page", page);
        modelAndView.addObject("discussPosts", posts);

        return modelAndView;
    }

}
