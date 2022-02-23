package com.community.dao;

import com.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author aptx
 * 处理私信相关
 */
@Mapper
public interface MessageMapper {


    /**
     * 通过用户信息获取与用户有关的会话信息
     *
     * @param userId 用户id
     * @param offset 偏移量
     * @param limit  每页的限制
     * @return 回话id的集合
     */
    List<String> selectConversationIdByUserId(int userId, int offset, int limit);

    /**
     * 通过会话id获取最新的消息
     *
     * @param conversationId 会话id
     * @return 最新的消息对象
     */
    Message selectNewestMessageByConversation(String conversationId);

    /**
     * 获取指定用户指定会话的未读消息数目
     *
     * @param userId         用户id
     * @param conversationId 会话id
     * @return 未读消息数目
     */
    int selectUnReadCountByConversion(int userId, String conversationId);

    /**
     * 获取指定用户指定会话的所有消息数目
     *
     * @param userId         用户id
     * @param conversationId 会话id
     * @return 未读消息数目
     */
    int selectAllCountByConversion(String conversationId);

    /**
     * 根据用户id查询与之有关的会话数目
     *
     * @param userId 用户id
     * @return 会话数目
     */
    Integer selectConversationCount(int userId);

    /**
     * 通过会话获得所有的消息记录并做分页
     *
     * @param conversationId 会话id
     * @param offset         起始
     * @param limit          每页最大显示数目
     * @return 消息列表
     */
    List<Message> selectAllMessageByConversation(String conversationId, int offset, int limit);

    /**
     * 添加消息或者通知
     *
     * @param message 消息或通知对象
     * @return 该变条数
     */
    int insertMessage(Message message);

    /**
     * 通过userId获取所有未读消息和通知数目
     *
     * @param userId 用户id
     * @param status 查询的消息状态类型
     * @return 要查询的状态的消息和通知的数目
     */
    int selectUnReadCountByUserId(int userId, int status);

    /**
     * 修改对应会话的消息的状态，将未读转为已读
     * @param conversationId 会话id
     * @param userId 用户id
     * @return 返回修改的条数
     */
    int updateMessageStatus(int userId,String conversationId);
}
