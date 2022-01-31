package com.community.util;

import com.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * 用于代替Session对象，持有用户信息
 *
 * @author aptx
 */
@Component
public class HostHolder {

    private final ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }
    public void clear(){
        users.remove();
    }
}
