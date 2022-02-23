package com.community.service;

import com.community.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * 处理关注相关
 *
 * @author aptx
 */
@Service
public class FollowServer {

    @Autowired
    RedisTemplate<String, Object> template;

    public boolean follow(int fromUserId, int toUserId) {
        String toUserKey = RedisKey.getUserFollowedKey(toUserId);
        String fromUserKey = RedisKey.getUserFollowKey(fromUserId);
        Boolean isMember = template.opsForSet().isMember(toUserKey, fromUserId);
        Boolean execute = template.execute(new SessionCallback<Boolean>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                if (isMember != null && isMember) {
                    template.opsForSet().remove(fromUserKey, toUserId);
                    template.opsForSet().remove(toUserKey, fromUserId);
                    operations.exec();
                    return false;
                } else {
                    template.opsForSet().add(fromUserKey, toUserId);
                    template.opsForSet().add(toUserKey, fromUserId);
                    operations.exec();
                    return true;
                }
            }
        });
        return Boolean.TRUE.equals(execute);

    }

    public Long getFollowCount(int userId) {
        String key = RedisKey.getUserFollowKey(userId);

        Long size = template.opsForSet().size(key);
        if (size == null) {
            return 0L;
        } else {
            return size;
        }
    }


    public Long getFollowedCount(int userId) {
        String key = RedisKey.getUserFollowedKey(userId);

        Long size = template.opsForSet().size(key);
        if (size == null) {
            return 0L;
        } else {
            return size;
        }
    }

    public boolean isFollow(int fromId, int toId) {
        String key=RedisKey.getUserFollowedKey(toId);
        Boolean isMember = template.opsForSet().isMember(key, fromId);
        return isMember != null && isMember;
    }

}

