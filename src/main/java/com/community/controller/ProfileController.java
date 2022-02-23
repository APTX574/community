package com.community.controller;

import com.community.entity.Comment;
import com.community.entity.DiscussPost;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.*;
import com.community.util.CommunityConstant;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * @author aptx
 */
@Controller
public class ProfileController implements CommunityConstant {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserServer userServer;

    @Autowired
    DiscussPostServer discussPostServer;

    @Autowired
    LikeServer likeServer;

    @Autowired
    CommentService commentService;

    @Autowired
    FollowServer followServer;

    /**
     * 获取指定用户的个人主页
     *
     * @param userId 指定用户id
     * @param model  模板
     * @return 个人主页
     */
    @RequestMapping("/profile/{userId}")
    public String getProfilePage(@PathVariable(value = "userId") int userId, Model model) {
        User loginUser = hostHolder.getUser();
        User user = userServer.getUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("该用户不存在");
        }
        int likedCount = likeServer.getUserLikedCount(userId);
        model.addAttribute("isFollow",followServer.isFollow(loginUser.getId(),userId));
        model.addAttribute("followedCount",followServer.getFollowedCount(userId));
        model.addAttribute("followCount",followServer.getFollowCount(userId));
        model.addAttribute("user", user);
        model.addAttribute("likeCount", likedCount);
        return "/site/profile";
    }

    /**
     * 获取指定用户的所有帖子页面
     *
     * @param userId 用户id
     * @param model 模板
     * @param page 分页
     * @return 所有帖子页面
     */
    @RequestMapping("/mypost/{userId}")
    public String getMyPostPage(@PathVariable(value = "userId") int userId, Model model, Page page) {
        page.setPath("/mypost/" + userId);
        if (page.getRows() == 0) {
            page.setRows(discussPostServer.findDiscussPostSize(userId));
        }
        List<DiscussPost> discussPosts = discussPostServer.findDiscussPosts(userId, page.getOffset(), page.getLimit());
        List<Map<String,Object>> list=new ArrayList<>();
        for(DiscussPost post:discussPosts){
            Map<String,Object> map=new HashMap<>();
            map.put("post",post);
            map.put("likeCount",likeServer.likeSize(ENTITY_TYPE_POST,post.getId()));
            list.add(map);
        }
        model.addAttribute("list", list);
        model.addAttribute("count", page.getRows());
        User user = userServer.getUserById(userId);
        model.addAttribute("user", user);
        return "/site/my-post";
    }

    /**
     * 获取指定用户的所有回复页面
     * @param userId 指定用户id
     * @param model 模板
     * @param page 分页
     * @return 所有回复页面
     */
    @RequestMapping("/myreply/{userId}")
    public String getMyReplyPage(@PathVariable(value = "userId") int userId, Model model, Page page) {

        page.setRows(commentService.findCountByUserId(userId));
        page.setPath("/myreply/" + userId);
        List<Comment> comments = commentService.findCommentByUserId(userId, page.getOffset(), page.getLimit());
        List<Map<String, Object>> list = new ArrayList<>();
        for (Comment comment : comments) {
            Map<String, Object> map = new HashMap<>();
            DiscussPost discussPost = null;
            if (comment.getEntityType() == 0) {
                discussPost = discussPostServer.getDiscussPost(comment.getEntityId());
            }
            if (comment.getEntityType() == 1) {
                int entityId = commentService.findCommentById(comment.getEntityId()).getEntityId();
                discussPost = discussPostServer.getDiscussPost(entityId);
            }
            map.put("comment", comment);
            map.put("post", discussPost);


            list.add(map);
        }
        model.addAttribute("list", list);
        model.addAttribute("count", commentService.findCountByUserId(userId));
        User user = userServer.getUserById(userId);
        model.addAttribute("user", user);
        return "/site/my-reply";

    }

}
