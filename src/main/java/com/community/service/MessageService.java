package com.community.service;

import com.community.dao.MessageMapper;
import com.community.dao.UserMapper;
import com.community.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aptx
 */
@Service
public class MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    UserMapper userMapper;

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

}
