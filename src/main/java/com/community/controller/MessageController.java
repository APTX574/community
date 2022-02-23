package com.community.controller;

import com.community.annotation.LoginRequired;
import com.community.entity.Message;
import com.community.entity.Page;
import com.community.entity.User;
import com.community.service.MessageService;
import com.community.service.UserServer;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import com.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 处理私信相关请求
 *
 * @author aptx
 */
@Controller
@RequestMapping(path = "letter")
public class MessageController implements CommunityConstant {
    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserServer userServer;

    @LoginRequired
    @RequestMapping(path = "page", method = RequestMethod.GET)
    public String getLetterPage(@RequestParam(value = "limit", defaultValue = "5") int limit,
                                @RequestParam(value = "current", defaultValue = "1") int current,
                                Model model) {
        Page page = new Page();
        page.setCurrent(current);
        page.setLimit(limit);
        page.setPath("/letter/page");
        User user = hostHolder.getUser();
        int userId = user.getId();
        page.setRows(messageService.findAllConversationCount(userId));
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> conversationIds = messageService.findConversationByUserId(user.getId(), page.getOffset(), limit);
        if (conversationIds != null) {
            for (String conversationId : conversationIds) {
                Map<String, Object> map = new HashMap<>();
                Message message = messageService.findNewestMessageByConversationId(conversationId);
                map.put("message", message);
                if (message.getFromId() == userId) {
                    map.put("user", userServer.getUserById(message.getToId()));
                } else {
                    map.put("user", userServer.getUserById(message.getFromId()));
                }
                int unReadCount = messageService.findUnReadByConversation(userId, conversationId);
                map.put("unReadCount", unReadCount);
                int allCount = messageService.findAllCountByConversation(conversationId);
                map.put("allCount", allCount);
                map.put("conversationId", conversationId);
                list.add(map);
            }
        }
        int unreadMessageCount = messageService.findUnReadCountByType(userId, UNREAD_MESSAGE);
        int unreadNoticeCount = messageService.findUnReadCountByType(userId, UNREAD_NOTICE);
        model.addAttribute("unreadMessage", unreadMessageCount);
        model.addAttribute("unreadNotice", unreadNoticeCount);
        model.addAttribute("conversations", list);
        model.addAttribute("page", page);
        return "/site/letter";
    }

    @LoginRequired
    @RequestMapping(path = "detail/{conversationId}", method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable(value = "conversationId", required = false) String conversationId,
                                  Model model, Page page) {
        //获取关于会话的消息
        User user = hostHolder.getUser();
        int userId = user.getId();
        page.setRows(messageService.findAllCountByConversation(conversationId));
        page.setPath("/letter/detail/" + conversationId);
        List<Message> messages = messageService.findMessageByConversation(conversationId, page.getOffset(), page.getLimit());
        int fromUser;
        if (messages.get(0).getFromId() == userId) {
            fromUser = messages.get(0).getToId();
        } else {
            fromUser = messages.get(0).getFromId();
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Message message : messages) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("message", message);
            map.put("user", userServer.getUserById(message.getFromId()));
            list.add(map);
        }
        model.addAttribute("fromUser", userServer.getUserById(fromUser));
        model.addAttribute("list", list);
        //将消息全部设为已读
        messageService.changeMessageStatus(userId,conversationId);
        return "/site/letter-detail";

    }

    @LoginRequired
    @RequestMapping(path = "add", method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(int fromId, String toName, String content) {
        User toUser = userServer.getUserByUsername(toName);

        if (toUser != null) {
            int toId = toUser.getId();
            int loginId = hostHolder.getUser().getId();
            if (loginId == fromId) {
                int i = messageService.addMessage(fromId, toId, content, 0);
                if (i == 1) {
                    return CommunityUtil.getJsonString(200, "添加成功");
                }
                return CommunityUtil.getJsonString(500, "添加失败");
            }
        }
        return CommunityUtil.getJsonString(500, "添加失败");
    }

}
