package com.community.controller;

import com.community.annotation.LoginRequired;
import com.community.entity.User;
import com.community.service.LikeServer;
import com.community.util.CommunityUtil;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.AccessType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理点赞和关注
 *
 * @author aptx
 */
@Controller
public class LikeController {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeServer likeServer;

    @LoginRequired
    @ResponseBody
    @RequestMapping(value = "like/add", method = RequestMethod.POST)
    public String like(int entityId, int entityType) {
        User loginUser = hostHolder.getUser();
        Long likeCount = likeServer.like(loginUser.getId(), entityType, entityId);
        Map<String,Object> map=new HashMap<>(1);
        map.put("newCount",likeCount);
        return CommunityUtil.getJsonString(200, "操作成功",map);
    }
}
