package com.community.controller;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.community.entity.Comment;
import com.community.entity.Message;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.CommentService;
import com.community.service.MessageService;
import com.community.service.UserServer;
import com.community.util.CommunityConstant;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author aptx
 */
@Controller
public class NoticeDetailController implements CommunityConstant {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserServer userServer;

    @Autowired
    CommentService commentService;


    @RequestMapping(path = "/notice/{type}")
    public String getDetailPage(@PathVariable("type") String type, Model model, Page page) {
        User loginUser = hostHolder.getUser();
        page.setRows(messageService.findAllCountByConversation(type,loginUser.getId()));
        page.setPath("/notice/"+type);
        List<Message> messages = messageService.findMessageByConversation(
                type, page.getOffset(), page.getLimit(), loginUser.getId());
        List<Map<String, Object>> cvo = new ArrayList<>();
        for (Message message : messages) {
            Map<String, Object> map = new HashMap<>();
            User user = userServer.getUserById(message.getFromId());
            map.put("user", user);
            Map<String, Object> map1 = JSONObject.parseObject(message.getContent(), Map.class);
            map.put("msg", map1.get("msg"));
            map.put("entityType", map1.get("entity"));
            map.put("message",message);
            String href;
            int entityType = (int) map1.get("entityType");
            int entityId = (int) map1.get("entityId");
            if (entityType == ENTITY_TYPE_COMMENT) {
                Comment comment = commentService.findCommentById(entityId);
                href = "/discuss/post/" + comment.getEntityId();
            }else if (entityType == ENTITY_TYPE_POST) {
                href = "/discuss/post/" + entityId;

            } else {
                href = "/follower/" + loginUser.getId();
            }
            map.put("href", href);
            cvo.add(map);
        }
        model.addAttribute("list", cvo);
        messageService.changeMessageStatus(loginUser.getId(),type);
        return "site/notice-detail";
    }
}
