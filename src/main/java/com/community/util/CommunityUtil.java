package com.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.xml.crypto.Data;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author aptx
 */
public class CommunityUtil {
    /**
     * 生成随机字符串
     *
     * @return 生成随机字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * MD5加密
     *
     * @param key 加密明文
     * @return 密文
     */
    public static String md5(String key) {
        if(StringUtils.isBlank(key)){
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));
    }
    public static String getJsonString(int code, String msg, Map<String,Object> map){
        JSONObject json=new JSONObject();
        json.put("code",code);
        if(msg!=null){
            json.put("msg",msg);
        }
        if(map!=null){
            for (String key:map.keySet()){
                json.put(key,map.get(key));
            }
        }
        return json.toJSONString();
    }
    public static String getJsonString(int code ,String msg){
        return getJsonString(code,msg,null);
    }
    public static String getJsonString(int code){
        return getJsonString(code,null,null);
    }
    public static void addNotice(){

    }
    public static Date getDate(Long timeStamp){
        return new Date(timeStamp);
    }
}
