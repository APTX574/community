package com.community.service;

import com.alibaba.fastjson.JSON;
import com.community.dao.MessageMapper;
import com.community.entity.Event;
import com.community.entity.Message;
import com.community.util.CommunityConstant;
import com.community.util.SensitiveFilter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    UserServer userServer;

    @Autowired
    SensitiveFilter sensitiveFilter;

    Logger logger= LoggerFactory.getLogger(MessageService.class);


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
    public int findAllCountByConversation(String conversation,int userId) {
        return messageMapper.selectAllCountByConversion(conversation,userId);
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

    public Message findNewestByUserIdAndType(int userId, String conversionId) {
        return messageMapper.selectMessageByUserIdAndType(userId, conversionId);
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
    public List<Message> findMessageByConversation(String conversationId, int offset, int limit,int userId) {
        return messageMapper.selectAllMessageByConversation(conversationId,userId, offset, limit);
    }

    /**
     * 添加消息或者通知
     *
     * @param fromId  消息发送者
     * @param toId    消息接受者
     * @param content 消息内容
     * @return 影响条数
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

    public void changeMessageStatus(int userId, String conversationId) {
        messageMapper.updateMessageStatus(userId, conversationId);
    }

    public void addEvent(@NotNull Event event) {
        Message message = new Message();
        message.setToId(event.getEntityUserId());
        message.setFromId(event.getUserId());
        message.setCreateTime(event.getCreateTime());
        String action;
        String entity;
        //xxx关注了您的xx评论
        switch (event.getTopic()) {
            case TOPIC_COMMENT -> action = "评论了";
            case TOPIC_LIKE -> action = "点赞了";
            case TOPIC_FOLLOW -> action = "关注了";
            default -> action = "";
        }
        switch (event.getEntityType()) {
            case ENTITY_TYPE_POST -> entity = "帖子";
            case ENTITY_TYPE_COMMENT -> entity = "评论";
            case ENTITY_TYPE_USER -> entity = "人";
            default -> entity = "";
        }
        String username = userServer.getUserById(event.getUserId()).getUsername();
        message.setCreateTime(event.getCreateTime());
        message.setStatus(UNREAD_NOTICE);
        message.setConversationId(event.getTopic().toLowerCase(Locale.ROOT));
        HashMap<String,Object> map = new HashMap<>(4);
        map.put("msg", username + action + "您的");
        map.put("entityType", event.getEntityType());
        map.put("entityId", event.getEntityId());
        map.put("entity",entity);
        message.setContent(JSON.toJSONString(map));
        messageMapper.insertMessage(message);
        logger.info("messageMapper添加了event");

    }

    public int getCountByUserIdAndType(int userId, String conversationId) {
        return messageMapper.selectCountByTypeAndUserId(userId, conversationId);
    }


}
