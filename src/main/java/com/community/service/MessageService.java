package com.community.service;

import com.community.dao.MessageMapper;
import com.community.dao.UserMapper;
import com.community.entity.Message;
import com.community.util.CommunityUtil;
import com.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @author aptx
 */
@Service
public class MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SensitiveFilter sensitiveFilter;

    public List<String> findConversationByUserId(int userId, int offset, int limit) {
        return messageMapper.selectConversationIdByUserId(userId, offset, limit);

    }

    public int findAllCountByConversation(int userId, String conversation) {
        return messageMapper.selectAllCountByConversion(userId, conversation);
    }

    public int findUnReadByConversation(int userId, String conversation) {
        return messageMapper.selectUnReadCountByConversion(userId, conversation);
    }

    public Message findNewestMessageByConversationId(String conversation) {
        return messageMapper.selectNewestMessageByConversationId(conversation);
    }

    public int findAllConversationCount(int userId) {
        if (messageMapper.selectConversationCount(userId) == null) {
            return 0;
        }
        return messageMapper.selectConversationCount(userId);
    }

    public List<Message> findMessageByConversation(String conversationId, int offset, int limit) {
        return messageMapper.selectAllMessageByConversation(conversationId, offset, limit);
    }

    public int addMessage(int fromId, int toId, String content) {
        StringBuffer sb = new StringBuffer(HtmlUtils.htmlEscape(content));
        sensitiveFilter.replace(sb);
        Message message = new Message();
        message.setContent(sb.toString());
        int min = Math.min(fromId, toId);
        int max = Math.max(fromId, toId);
        message.setConversationId(min + "_" + max);
        message.setFromId(fromId);
        message.setToId(toId);
        message.setStatus(0);
        message.setCreateTime(new Date());
        return messageMapper.insertMessage(message);
    }

}
