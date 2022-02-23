package com.community.service;

import com.community.dao.MessageMapper;
import com.community.dao.UserMapper;
import com.community.entity.Message;
import com.community.util.CommunityConstant;
import com.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.*;

/**
 * @author aptx
 */
@Service
public class MessageService implements CommunityConstant {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    SensitiveFilter sensitiveFilter;

    /**
     * 通过用户id查找到与之相关的会话进行分页
     *
     * @param userId 用户id
     * @param offset 分页起始位置
     * @param limit  单页数目
     * @return 会话列表
     */
    public List<String> findConversationByUserId(int userId, int offset, int limit) {
        return messageMapper.selectConversationIdByUserId(userId, offset, limit);

    }

    /**
     * 通过会话获得会话的消息条数
     *
     * @param conversation 会话id
     * @return 该会话的所有消息条数
     */
    public int findAllCountByConversation(String conversation) {
        return messageMapper.selectAllCountByConversion(conversation);
    }

    /**
     * 通过会话获取到指定用户的未读消息
     *
     * @param userId       用户id
     * @param conversation 会话信息
     * @return 用户指定会话的消息条数
     */
    public int findUnReadByConversation(int userId, String conversation) {
        return messageMapper.selectUnReadCountByConversion(userId, conversation);
    }

    /**
     * 通过会话信息获取到改会话的最新消息
     *
     * @param conversation 会话id
     * @return 指定会话的最新的Message对象
     */
    public Message findNewestMessageByConversationId(String conversation) {
        return messageMapper.selectNewestMessageByConversation(conversation);
    }

    /**
     * 通过用户id查找到该用户的会话数目
     *
     * @param userId 用户id
     * @return 指定用户的所有会话的条数
     */
    public int findAllConversationCount(int userId) {
        if (messageMapper.selectConversationCount(userId) == null) {
            return 0;
        }
        return messageMapper.selectConversationCount(userId);
    }

    /**
     * 通过会话的id获取到该会话的所有消息进行分页
     *
     * @param conversationId 会话id
     * @param offset         分页起始位置
     * @param limit          单页最大条数
     * @return Message列表
     */
    public List<Message> findMessageByConversation(String conversationId, int offset, int limit) {
        return messageMapper.selectAllMessageByConversation(conversationId, offset, limit);
    }

    /**
     * 添加消息或者通知
     *
     * @param fromId
     * @param toId
     * @param content
     * @return
     */
    public int addMessage(int fromId, int toId, String content, int status) {
        StringBuffer sb = new StringBuffer(HtmlUtils.htmlEscape(content));
        sensitiveFilter.replace(sb);
        Message message = new Message();
        message.setContent(sb.toString());
        int min = Math.min(fromId, toId);
        int max = Math.max(fromId, toId);
        message.setConversationId(min + "_" + max);
        message.setFromId(fromId);
        message.setToId(toId);
        message.setStatus(status);
        message.setCreateTime(new Date());
        return messageMapper.insertMessage(message);
    }

    /**
     * 查找到某个用户所有的未读消息和通知的数目
     *
     * @param userId 用户id
     * @return 未读的消息和通知的数目
     */
    public int findUnReadCountByUserId(int userId) {
        return messageMapper.selectUnReadCountByUserId(userId, UNREAD_MESSAGE) +
                messageMapper.selectUnReadCountByUserId(userId, UNREAD_NOTICE);
    }

    public int findUnReadCountByType(int userId, int status) {
        return messageMapper.selectUnReadCountByUserId(userId, status);
    }

    public int changeMessageStatus(int userId,String conversationId) {
        return messageMapper.updateMessageStatus(userId,conversationId);
    }

    public int addNotice(int fromId, String content, int toId, int status) {
        String conversationId = fromId + "_" + toId;
        Message message=new Message();
        message.setCreateTime(new Date());
        message.setStatus(status);
        message.setToId(toId);
        message.setFromId(fromId);
        message.setConversationId(conversationId);
        return messageMapper.insertMessage(message);
    }
}
