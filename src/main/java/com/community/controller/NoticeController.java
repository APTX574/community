package com.community.controller;

import com.community.entity.Message;
import com.community.entity.User;
import com.community.service.MessageService;
import com.community.service.UserServer;
import com.community.util.CommunityConstant;
import com.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author aptx
 */
@Controller
public class NoticeController implements CommunityConstant {

    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserServer userServer;
    Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @RequestMapping("notice")
    public String getNoticePage(Model model) {
        User loginUser = hostHolder.getUser();
        if (loginUser == null) {
            logger.info("该用户未登录");
            throw new IllegalArgumentException("未登录");
        }



        int likeCount =messageService.getCountByUserIdAndType(loginUser.getId(),TOPIC_LIKE);
        if(likeCount!=0){
            int unreadCount=messageService.findUnReadByConversation(loginUser.getId(),TOPIC_LIKE);
            model.addAttribute("unreadCountLike",unreadCount);
            Message likeMessage = messageService.findNewestByUserIdAndType(loginUser.getId(), TOPIC_LIKE);
            model.addAttribute("likeMessage", likeMessage);
            model.addAttribute("likeUser",userServer.getUserById(likeMessage.getFromId()));
        }
        model.addAttribute("likeCount",likeCount);



        int commentCount =messageService.getCountByUserIdAndType(loginUser.getId(),TOPIC_COMMENT);
        if(commentCount!=0){
            int unreadCount=messageService.findUnReadByConversation(loginUser.getId(),TOPIC_COMMENT);
            model.addAttribute("unreadCountComment",unreadCount);
            Message commentMessage = messageService.findNewestByUserIdAndType(loginUser.getId(), TOPIC_COMMENT);
            model.addAttribute("commentMessage", commentMessage);
            model.addAttribute("commentUser",userServer.getUserById(commentMessage.getFromId()));
        }
        model.addAttribute("commentCount",commentCount);



        int followCount =messageService.getCountByUserIdAndType(loginUser.getId(),TOPIC_FOLLOW);
        if(followCount!=0){
            int unreadCount=messageService.findUnReadByConversation(loginUser.getId(),TOPIC_FOLLOW);
            model.addAttribute("unreadCountFollow",unreadCount);
            Message followMessage = messageService.findNewestByUserIdAndType(loginUser.getId(), TOPIC_FOLLOW);
            model.addAttribute("followMessage", followMessage);
            model.addAttribute("followUser",userServer.getUserById(followMessage.getFromId()));
        }
        model.addAttribute("followCount",followCount);
        int unreadMessageCount = messageService.findUnReadCountByType(loginUser.getId(), UNREAD_MESSAGE);
        int unreadNoticeCount = messageService.findUnReadCountByType(loginUser.getId(), UNREAD_NOTICE);
        model.addAttribute("unreadMessage", unreadMessageCount);
        model.addAttribute("unreadNotice", unreadNoticeCount);


        return "site/notice";
    }
}
