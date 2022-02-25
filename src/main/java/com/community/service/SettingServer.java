package com.community.service;

import com.community.entity.User;
import com.community.util.CommunityConstant;
import com.community.util.CommunityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SettingServer implements CommunityConstant {

    @Autowired
    private UserServer userServer;

    public Map<String,Object> changePwd(User user , String newPwd, String oldPwd){
        Map<String,Object> map=new HashMap<>();
        if(user==null){
            map.put("msg",UN_LOGIN);
            return map;
        }
        String md5pwd= CommunityUtil.md5(oldPwd+user.getSalt());
        if(StringUtils.equals(md5pwd,user.getPassword())){
            String newMd5=CommunityUtil.md5(newPwd+user.getSalt());
            userServer.changePwd(user.getId(),newMd5);
            map.put("msg",CHANGE_PWD_SUCCESS);
            return map;
        }else{
            map.put("msg", CHANGE_PWD_ERROR_OLD_PWD);
            return map;
        }

    }
}
