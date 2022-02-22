package com.community.dao;

import com.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author aptx
 */
@Mapper
public interface CommentMapper {

    List<Comment> selectCommentByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

    int insertComment(Comment comment);

    /**
     * 通过用户id查询用户的回复
     *
     * @param userId 用户的id
     * @param offset 起始位置
     * @param limit  每页的限制
     * @return 评论的列表
     */
    List<Comment> selectCommentsByUserId(int userId, int offset, int limit);

    int selectCountByUserId(int userId);

    Comment selectCommentById(int id);
}
