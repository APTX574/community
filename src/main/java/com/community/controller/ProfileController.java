package com.community.controller;

import com.community.entity.Comment;
import com.community.entity.DiscussPost;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.CommentService;
import com.community.service.DiscussPostServer;
import com.community.service.UserServer;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aptx
 */
@Controller
public class ProfileController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserServer userServer;

    @Autowired
    DiscussPostServer discussPostServer;

    @Autowired
    CommentService commentService;

    @RequestMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable(value = "userId") int userId, Model model) {
        User user = userServer.getUserById(userId);
        model.addAttribute("user", user);
        return "/site/profile";
    }

    @RequestMapping("/mypost/{userId}")
    public String getMyPostPage(@PathVariable(value = "userId") int userId, Model model, Page page) {
        page.setPath("/mypost/" + userId);
        if (page.getRows() == 0) {
            page.setRows(discussPostServer.findDiscussPostSize(userId));
        }
        List<DiscussPost> discussPosts = discussPostServer.findDiscussPosts(userId, page.getOffset(), page.getLimit());
        model.addAttribute("list", discussPosts);
        model.addAttribute("count", page.getRows());

        User user = userServer.getUserById(userId);
        model.addAttribute("user", user);
        return "/site/my-post";
        //TODO 处理查找到模板错误
    }

    @RequestMapping("/myreply/{userId}")
    public String getMyReplyPage(@PathVariable(value = "userId")int userId,Model model,Page page){

        page.setRows(commentService.findCountByUserId(userId));
        page.setPath("/myreply/"+userId);
        List<Comment> comments = commentService.findCommentByUserId(userId, page.getOffset(), page.getLimit());
        List<Map<String,Object>> list=new ArrayList<>();
        for (Comment comment :comments){
            Map<String,Object> map=new HashMap<>();
            DiscussPost discussPost=null;
            if(comment.getEntityType()==0){
                discussPost = discussPostServer.getDiscussPost(comment.getEntityId());
            }
            if(comment.getEntityType()==1){
                int entityId=commentService.findCommentById(comment.getEntityId()).getEntityId();
                discussPost=discussPostServer.getDiscussPost(entityId);
            }
            map.put("comment",comment);
            map.put("post",discussPost);


            list.add(map);
        }
        model.addAttribute("list",list);
        model.addAttribute("count",commentService.findCountByUserId(userId));
        User user = userServer.getUserById(userId);
        model.addAttribute("user", user);
        return "/site/my-reply";
        //TODO 处理查找到模板错误

    }

}
