package com.community.controller;

import com.alibaba.fastjson.JSONObject;
import com.community.annotation.LoginRequired;
import com.community.entity.Comment;
import com.community.entity.User;
import com.community.service.CommentService;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 处理评论相关请求
 *
 * @author aptx
 */
@Controller
@RequestMapping(path = "/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @RequestMapping(path = "add", method = RequestMethod.POST)
    @ResponseBody
    public String addComment(Comment comment) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJsonString(401, "未登录");
        }
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);
        return CommunityUtil.getJsonString(200, "添加成功");
    }
}
