package com.community.controller;

import com.community.CommunityApplication;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.FollowServer;
import com.community.service.UserServer;
import com.community.util.CommunityUtil;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;
import java.util.List;
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

    /**
     * 处理用户关注和取消关注操作
     *
     * @param toUserId 对操作的用户id
     * @return 操作后是否关注
     */
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


    /**
     * 获取用户关注页面（返回用户关注的人）
     *
     * @param userId 用户id
     * @param model  模板
     * @return 界面
     */
    @RequestMapping(value = "/followee/{userId}", method = RequestMethod.GET)
    public String getFollowPade(@PathVariable(value = "userId") int userId, Model model, Page page) {
        page.setPath("/followee/" + userId);
        page.setRows(Math.toIntExact(followServer.getFollowCount(userId)));
        List<Map<String,Object>> followUsers = followServer.getFollowUsers(userId, page.getOffset(), page.getLimit());
        int loginUserId=hostHolder.getUser().getId();
        for (Map<String,Object> map:followUsers){
            map.put("isFollow",followServer.isFollow(loginUserId,((User)map.get("user")).getId()));
        }
        model.addAttribute("list", followUsers);
        model.addAttribute("user",userServer.getUserById(userId));
        return "/site/followee";
    }

    /**
     * 获取关注指定用户的用户列表页面（返回粉丝）
     *
     * @param userId 用户id
     * @param model  模板
     * @return 界面
     */
    @RequestMapping(value = "/follower/{userId}", method = RequestMethod.GET)
    public String getFollowedPade(@PathVariable(value = "userId") int userId, Model model, Page page) {
        page.setPath("/follower/" + userId);
        page.setRows(Math.toIntExact(followServer.getFollowedCount(userId)));
        List<Map<String,Object>> followedUsers = followServer.getFollowedUsers(userId, page.getOffset(), page.getLimit());
        int loginUserId=hostHolder.getUser().getId();
        for (Map<String,Object> map:followedUsers){
            map.put("isFollow",followServer.isFollow(loginUserId,((User)map.get("user")).getId()));
        }
        model.addAttribute("list", followedUsers);
        model.addAttribute("user",userServer.getUserById(userId));
        return "/site/follower";
    }

}
