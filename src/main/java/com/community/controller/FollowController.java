package com.community.controller;

import com.community.CommunityApplication;
import com.community.entity.User;
import com.community.service.FollowServer;
import com.community.service.UserServer;
import com.community.util.CommunityUtil;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aptx
 */
@Controller
public class FollowController {

    @Autowired
    FollowServer followServer;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserServer userServer;

    @ResponseBody
    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    public String follow(int toUserId) {
        int fromUserId = hostHolder.getUser().getId();
        User toUser = userServer.getUserById(toUserId);
        if (toUser == null) {
            throw new IllegalArgumentException("关注用户不存在");
        }
        boolean isFollowed = followServer.follow(fromUserId, toUserId);
        Map<String, Object> map = new HashMap<>();
        map.put("isFollow", isFollowed);
        return CommunityUtil.getJsonString(200, "操作成功", map);
    }

}
