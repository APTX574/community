package com.community.service;

import com.community.entity.Event;
import com.community.entity.User;
import com.community.event.EventProducer;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import com.community.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 处理关注相关
 *
 * @author aptx
 */
@Service
public class FollowServer implements CommunityConstant {

    @Autowired
    RedisTemplate<String, Object> template;
    @Autowired
    UserServer userServer;

    @Autowired
    EventProducer eventProducer;
    public boolean follow(int fromUserId, int toUserId) {
        String toUserKey = RedisKey.getUserFollowedKey(toUserId);
        String fromUserKey = RedisKey.getUserFollowKey(fromUserId);
        Boolean isMember = isFollow(fromUserId,toUserId);
        if (!Boolean.TRUE.equals(isMember)){
            Event event=new Event();
            event.setCreateTime(new Date()).setTopic(TOPIC_FOLLOW).setEntityType(ENTITY_TYPE_USER).setEntityId(toUserId)
                    .setUserId(fromUserId).setEntityUserId(toUserId);
            eventProducer.addEvent(event);
        }
        Boolean execute = template.execute(new SessionCallback<>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                if (isMember) {
                    template.opsForZSet().remove(fromUserKey, toUserId);
                    template.opsForZSet().remove(toUserKey, fromUserId);
                    operations.exec();
                    return false;
                } else {
                    long time = System.currentTimeMillis();
                    template.opsForZSet().add(fromUserKey, toUserId, time);
                    template.opsForZSet().add(toUserKey, fromUserId, time);
                    operations.exec();
                    return true;
                }
            }
        });
        return Boolean.TRUE.equals(execute);
    }

    public Long getFollowCount(int userId) {
        String key = RedisKey.getUserFollowKey(userId);

        Long size = template.opsForZSet().zCard(key);
        if (size == null) {
            return 0L;
        } else {
            return size;
        }
    }


    public Long getFollowedCount(int userId) {
        String key = RedisKey.getUserFollowedKey(userId);

        Long size = template.opsForZSet().zCard(key);
        if (size == null) {
            return 0L;
        } else {
            return size;
        }
    }

    public boolean isFollow(int fromId, int toId) {
        String key = RedisKey.getUserFollowedKey(toId);
        return template.opsForZSet().score(key, fromId) != null;
    }

    public List<Map<String, Object>> getFollowUsers(int userId, int offset, int limit) {
        String key = RedisKey.getUserFollowKey(userId);
        return getUserByKey(key, offset, limit);
    }

    public List<Map<String, Object>> getFollowedUsers(int userId, int offset, int limit) {
        String key = RedisKey.getUserFollowedKey(userId);
        return getUserByKey(key, offset, limit);

    }

    private List<Map<String, Object>> getUserByKey(String key, int offset, int limit) {
        Set<Object> members = template.opsForZSet().reverseRange(key, offset, offset + limit + 1);
        if (members == null) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> list = new ArrayList<>();
        for (Object o : members) {
            Map<String, Object> map = new HashMap<>();
            map.put("user", userServer.getUserById((Integer) o));
            map.put("time", CommunityUtil.getDate(template.opsForZSet().score(key, o).longValue()));
            list.add(map);
        }
        return list;
    }


}

