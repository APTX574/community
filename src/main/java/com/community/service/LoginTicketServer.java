package com.community.service;

import com.community.dao.LoginTicketMapper;
import com.community.entity.LoginTicket;
import com.community.util.CommunityConstant;
import com.community.util.RedisKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author aptx
 */
@Service
@Deprecated
public class LoginTicketServer implements CommunityConstant {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public Map<String, Object> verifyTicket(String ticket) {
        LoginTicket loginTicket = loginTicketMapper.findByTicket(ticket);
        return verifyTicketByStr(loginTicket.getUserId());

    }

    public Map<String,Object> verifyTicketByStr(int userId){
        Map<String, Object> map = new HashMap<>();
        if(userId==1){
            map.put("msg",TICKET_NOT_FIND);
            return map;
        }
        map.put("msg", TICKET_FIND_SUCCESS);
        map.put("userId",userId);
        return map;
    }

    public void addLoginTicket(LoginTicket loginTicket) {
        if (loginTicket != null) {
            loginTicketMapper.addLoginTicket(loginTicket);
        }
    }

    public void updateStatusByTicket(String ticket, int status) {
        loginTicketMapper.updateStatusByTicket(ticket, status);
    }

    public void updateStatusByUserId(int userId, int status) {
        loginTicketMapper.updateStatusByUserId(userId, status);
    }

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
