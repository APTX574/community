package com.community.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.PublicKey;

/**
 * @author aptx
 */
public class CookieUtil {
    public static String getValue(HttpServletRequest request,String name){
        if(name==null||request==null|| StringUtils.isBlank(name)){
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if(cookies==null){
            return null;
        }
        for (Cookie cookie : cookies) {
            if(name.equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }
}
