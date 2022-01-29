package com.community.dao;

import com.community.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectById(int id);

    User selectByEmail(String email);

    User selectByName(String name);

    /**
     * 添加用户功能，用于用户注册
     * @param user 需要添加的用户
     * @return int
     */
    int insertUser(User user);

//    int updateUser(User user);

    int updateStatus(int id,int status);

    int updateHeader(int id ,String headerUrl);

    int updatePassword(int id,String password);
}
