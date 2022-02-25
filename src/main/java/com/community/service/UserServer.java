package com.community.service;

import com.community.dao.UserMapper;
import com.community.entity.LoginTicket;
import com.community.entity.User;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import com.community.util.MailClient;
import com.community.util.RedisKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author aptx
 */
@Service
public class UserServer implements CommunityConstant {

    private final Random random = new Random();

    Logger logger= LoggerFactory.getLogger(UserServer.class);

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

    @Value("${community.path.domain}")
    private String doMain;

    @Value("${server.servlet.context-path}")
    private String comtextPath;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public Map<String, Object> register(User user) {
        Map<String, Object> map = new HashMap<>();

        //对空值处理
        if (user == null) {
            throw new IllegalArgumentException("参数不能为空");
        }
        if (StringUtils.isBlank(user.getUsername())) {
            map.put("usernameMsg", "账号不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            map.put("passwordMsg", "密码不能为空");
            return map;
        }
        if (StringUtils.isBlank(user.getEmail())) {
            map.put("emailMsg", "邮箱不能为空");
            return map;
        }

        //验证账号
        if (userMapper.selectByName(user.getUsername()) != null) {
            map.put("usernameMsg", "该账号已存在");
            return map;
        }
        if (userMapper.selectByEmail(user.getEmail()) != null) {
            map.put("emailMsg", "该邮箱已经注册");
            return map;
        }
        //注册用户

        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID().substring(0, 5));
        user.setCreatTime(new Date());
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(999) + 1));
        userMapper.insertUser(user);

        //发送激活邮件
        Context context = new Context();
        context.setVariable("email", user.getEmail());

        //http://community.xwxs.xyz/activation/id/code
        String url = doMain + comtextPath + "activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "请激活您的账号", content);

        return map;
    }

    public User getUserById(int id) {
        User cacheUser = getCacheUser(id);
         if (cacheUser == null) {
            return addCacheUser(id);
        }
        return cacheUser;
    }

    public int activation(int id, String code) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return ACTIVATION_FAIL;
        }
        if (StringUtils.equals(user.getActivationCode(), code) && user.getStatus() == 0) {
            userMapper.updateStatus(id, 1);
            return ACTIVATION_SUCCESS;
        } else if (user.getStatus() == 1) {
            return ACTIVATION_REPEAT;
        }
        return ACTIVATION_FAIL;

    }

    public Map<String, Object> login(String username, String password, boolean rememberMe) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.selectByName(username);
        if (user == null) {
            map.put("msg", LOGIN_GET_USER_FAIL);
            return map;
        }
        if (user.getPassword().equals(CommunityUtil.md5(password + user.getSalt()))) {
            //判断保存时间
            int dueTime;
            if (rememberMe) {
                dueTime = 60 * 60 * 24 * 7;
            } else {
                dueTime = 60 * 60 * 12;
            }
            //创建LoginTicket对象
            LoginTicket loginTicket = new LoginTicket();
            loginTicket.setStatus(0);
            loginTicket.setUserId(user.getId());
            loginTicket.setExpired(new Date(System.currentTimeMillis() + dueTime * 1000));
            loginTicket.setTicket(CommunityUtil.generateUUID().substring(0, 16));
            //保存对象
            String key = RedisKey.getTicketKeyByTicket(loginTicket.getTicket());
            redisTemplate.opsForValue().set(key, loginTicket);

            map.put("msg", LOGIN_SUCCESS);
            map.put("ticket", loginTicket.getTicket());
            return map;
        }
        if (user.getStatus() == 0) {
            map.put("msg", LOGIN_NOT_ACTIVATION);
            return map;
        }
        map.put("msg", LOGIN_PASSWORD_ERROR);
        return map;
    }

    public void logout(String ticket) {
        String key = RedisKey.getTicketKeyByTicket(ticket);


        LoginTicket loginTicket = (LoginTicket) redisTemplate.opsForValue().get(key);
        if (loginTicket == null) {
            throw new IllegalArgumentException("未登录");
        }
        loginTicket.setStatus(1);
        redisTemplate.opsForValue().set(key, loginTicket);

    }

    public void changePwd(int id, String pwd) {
        userMapper.updatePassword(id, pwd);
        removeCacheUser(id);
    }

    public void changeHeaderUrl(int userId, String headerUrl) {
        if (StringUtils.isBlank(headerUrl)) {
            return;
        }
        userMapper.updateHeader(userId, headerUrl);
        removeCacheUser(userId);
    }

    public User getUserByUsername(String username) {
        return userMapper.selectByName(username);
    }

    private User getCacheUser(int userId) {
        String userKey = RedisKey.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(userKey);
    }

    private void removeCacheUser(int userId) {
        String userKey = RedisKey.getUserKey(userId);
        redisTemplate.delete(userKey);
    }

    private User addCacheUser(int userId) {
        String userKey = RedisKey.getUserKey(userId);
        User user = userMapper.selectById(userId);
        redisTemplate.opsForValue().set(userKey, user,20, TimeUnit.MINUTES);
        logger.info("redis加载缓存 userId="+userId);
        return user;
    }
}
