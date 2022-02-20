package com.community.service;

import com.community.dao.CommentMapper;
import com.community.entity.Comment;
import com.community.util.CommunityConstant;
import com.community.util.SensitiveFilter;
import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @author aptx
 */
@Service
public class CommentService implements CommunityConstant {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private DiscussPostServer discussPostServer;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit) {
        return commentMapper.selectCommentByEntity(entityType, entityId, offset, limit);
    }

    public int findCountByEntity(int entityType, int entityId) {
        return commentMapper.selectCountByEntity(entityType, entityId);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public int addComment(Comment comment) {
        try {
            if (comment == null||comment.getContent()==null) {
                System.out.println("comment==null");
                throw new IllegalArgumentException("参数不能为空");
            }
            //敏感词过滤
            StringBuffer sb = new StringBuffer(HtmlUtils.htmlEscape(comment.getContent(),"utf8"));
            sensitiveFilter.replace(sb);
            comment.setContent(sb.toString());
            //插入评论
            int rows = commentMapper.insertComment(comment);
            //修改commentCount
            if (comment.getEntityType() == ENTITY_TYPE_POST) {
                int commentId = comment.getEntityId();
                int oldCount = discussPostServer.getDiscussPost(commentId).getCommentCount();
                discussPostServer.changeCommentCount(commentId, oldCount + 1);
            }
            return rows;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }


    }
}
