package com.community.service;

import com.community.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 处理点赞相关的业务逻辑
 *
 * @author aptx
 */
@Service
public class LikeServer {
    @Autowired
    private RedisTemplate<String, Object> template;

    /**
     * 通过用户id和实体类去点赞或者取消赞
     *
     * @param userId     操作的用户id
     * @param entityType 实体类型
     * @param entityId   实体id
     */
    public void like(int userId, int entityType, int entityId) {
        String key = RedisKey.getLikeKey(entityType, entityId);
        Boolean isMember = template.opsForSet().isMember(key, userId);
        if (isMember != null && isMember) {
            template.opsForSet().remove(key, userId);
        } else {
            template.opsForSet().add(key, userId);
        }
    }

    /**
     * 查询实体的总赞数
     *
     * @param entityType 实体类型
     * @param entityId   实体id
     * @return 总赞数
     */
    public Long likeSize(int entityType, int entityId) {
        String key = RedisKey.getLikeKey(entityType, entityId);
        return template.opsForSet().size(key);
    }

    /**
     * 判断用户是否对该实体点赞
     *
     * @param userId 用户id
     * @param entityType 实体类型
     * @param entityId 实体id
     * @return 用户是否对该实体点赞
     */
    public boolean isLiked(int userId, int entityType, int entityId) {
        String key = RedisKey.getLikeKey(entityType, entityId);
        Boolean isLiked = template.opsForSet().isMember(key, userId);
        return Boolean.TRUE.equals(isLiked);
    }
}
