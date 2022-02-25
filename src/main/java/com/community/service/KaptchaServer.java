package com.community.service;

import com.community.util.CommunityConstant;
import com.community.util.RedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author aptx
 */
@Service
public class KaptchaServer implements CommunityConstant {

    @Autowired
    RedisTemplate<String,Object> redisTemplate;

    public void storeKaptchaCode(String token, String code) {
        String key = RedisKey.getKaptchaCode(token);
        redisTemplate.opsForValue().set(key, code, KAPTCHA_TIME_OUT, TimeUnit.SECONDS);
    }

    public String getKaptchaCode(String userMark) {
        String key = RedisKey.getKaptchaCode(userMark);
        String code = (String) redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return code;
    }
}
