package com.community.service;

import com.community.dao.UserMapper;
import com.community.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServer {

    @Autowired
    public UserMapper userMapper;
    public User getUserById(int id){
        return userMapper.selectById(id);
    }
}
