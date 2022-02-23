package com.community.service;

import com.community.util.CommunityConstant;
import com.community.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * 处理点赞相关的业务逻辑
 *
 * @author aptx
 */
@Service
public class LikeServer implements CommunityConstant {
    @Autowired
    private RedisTemplate<String, Object> template;

    @Autowired
    private DiscussPostServer discussPostServer;

    @Autowired
    private CommentService commentService;

    /**
     * 通过用户id和实体类去点赞或者取消赞
     *
     * @param userId     操作的用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     */
    public Long like(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKey.getEntityLikeKey(entityType, entityId);
        int likedUserId;
        if (entityType == ENTITY_TYPE_POST) {
            likedUserId = discussPostServer.getDiscussPost(entityId).getUserId();
        } else {
            likedUserId = commentService.findCommentById(entityId).getUserId();
        }
        String userLikeKey = RedisKey.getUserLikeKey(likedUserId);

//
//        Boolean isMember = template.opsForSet().isMember(entityLikeKey, userId);
//        if (isMember != null && isMember) {
//            template.opsForSet().remove(entityLikeKey, userId);
//            template.opsForValue().decrement(userLikeKey);
//        } else {
//            template.opsForSet().add(entityLikeKey, userId);
//            template.opsForValue().increment(userLikeKey);
//        }
//        return template.opsForSet().size(entityLikeKey);
        //事物管理
        template.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                Boolean isMember = template.opsForSet().isMember(entityLikeKey, userId);
                operations.multi();
                if (isMember != null && isMember) {
                    template.opsForSet().remove(entityLikeKey, userId);
                    template.opsForValue().decrement(userLikeKey);
                } else {
                    template.opsForSet().add(entityLikeKey, userId);
                    template.opsForValue().increment(userLikeKey);
                }
                return operations.exec();
            }
        });
        return template.opsForSet().size(entityLikeKey);
    }

    /**
     * 查询实体的总赞数
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 总赞数
     */
    public Long likeSize(int entityType, int entityId) {
        String key = RedisKey.getEntityLikeKey(entityType, entityId);
        return template.opsForSet().size(key);
    }

    /**
     * 判断用户是否对该实体点赞
     *
     * @param userId     用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 用户是否对该实体点赞
     */
    public boolean isLiked(int userId, int entityType, int entityId) {
        String key = RedisKey.getEntityLikeKey(entityType, entityId);
        Boolean isLiked = template.opsForSet().isMember(key, userId);
        return Boolean.TRUE.equals(isLiked);
    }

    /**
     * 获取用户的总点赞数数
     *
     * @param userId 用户id
     * @return 总点赞数
     */
    public int getUserLikedCount(int userId) {
        String key = RedisKey.getUserLikeKey(userId);
        Object o = template.opsForValue().get(key);
        if (o == null) {
            template.opsForValue().set(key, 0);
            return 0;
        } else {
            return (int) o;
        }
    }
}
