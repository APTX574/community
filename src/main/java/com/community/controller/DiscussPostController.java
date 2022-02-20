package com.community.controller;

import com.community.entity.Comment;
import com.community.entity.DiscussPost;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.CommentService;
import com.community.service.DiscussPostServer;
import com.community.service.UserServer;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @author aptx
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {

    @Autowired
    DiscussPostServer discussPostServer;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserServer userServer;

    @Autowired
    CommentService commentService;

    @RequestMapping(path = "add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String content, String title) {
        User user = hostHolder.getUser();
        if (user == null) {
            //403代表没有权限
            return CommunityUtil.getJsonString(403, "你还没有登录");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setContent(content);
        post.setTitle(title);
        post.setCreateTime(new Date());
        post.setCommentCount(0);
        discussPostServer.addDiscussPost(post);
        //对于报错的问题进行统一处理
        return CommunityUtil.getJsonString(200, "发布成功");
    }

    @RequestMapping(path = "post/{id}", method = RequestMethod.GET)
    public String showDiscussPost(@PathVariable(name = "id", required = false) int id, Model model, Page page) {
        DiscussPost discussPost = discussPostServer.getDiscussPost(id);
        User user = userServer.getUserById(discussPost.getUserId());

        //规定中对于帖子的评论的entityType为0
        page.setLimit(COMMENT_LIMIT);
        page.setRows(discussPost.getCommentCount());
        page.setPath("/discuss/post/" + id);

        List<Comment> comments = commentService.
                findCommentsByEntity(ENTITY_TYPE_POST, id, page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentAndUser = new ArrayList<>();
        if (comments.size() != 0) {
            for (Comment comment : comments) {
                Map<String, Object> map = new HashMap<>();
                //评论
                map.put("comment", comment);
                //作者
                map.put("user", userServer.getUserById(comment.getUserId()));
                //回复列表
                List<Comment> replyList = commentService.findCommentsByEntity(
                        ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                //回复的vo列表
                map.put("replyCount",replyList.size());
                List<Map<String, Object>> replyVo = new ArrayList<>();
                for (Comment reply : replyList) {
                    Map<String, Object> replyMap = new HashMap<>();
                    replyMap.put("reply",reply);
                    replyMap.put("user",userServer.getUserById(reply.getUserId()));
                    if(reply.getTargetId()!=0){
                        replyMap.put("target",userServer.getUserById(reply.getTargetId()));
                    }else{
                        replyMap.put("target",null);
                    }
                    replyVo.add(replyMap);
                }
                map.put("replys",replyVo);
                //将评论添加到列表
                commentAndUser.add(map);
            }
        }
        model.addAttribute("comments", commentAndUser);
        model.addAttribute("post", discussPost);
        model.addAttribute("user", user);
        model.addAttribute("page",page);

        return "/site/discuss-detail";
    }


}
















