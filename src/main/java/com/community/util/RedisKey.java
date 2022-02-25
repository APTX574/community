package com.community.util;

/**
 * 获取Redis的key值
 *
 * @author aptx
 */
public class RedisKey {

    private static final String SPILT = ":";
    private static final String ENTITY_LIKE = "entity:like";
    private static final String USER_LIKE = "user:like";
    private static final String USER_FOLLOW = "user:follow";
    private static final String USER_FOLLOWED = "user:followed";
    private static final String KAPTCHA_CODE = "kaptcha";
    private static final String TICKET = "ticket";
    private static final String USER = "user";


    public static String getEntityLikeKey(int entityType, int entityId) {
        return ENTITY_LIKE + SPILT + entityType + SPILT + entityId;
    }

    public static String getUserLikeKey(int userId) {
        return USER_LIKE + SPILT + userId;
    }

    public static String getUserFollowKey(int userId) {
        return USER_FOLLOW + SPILT + userId;
    }

    public static String getUserFollowedKey(int userId) {
        return USER_FOLLOWED + SPILT + userId;
    }

    public static String getKaptchaCode(String cookie) {
        return KAPTCHA_CODE + SPILT + cookie;
    }


    public static String getTicketKeyByTicket(String ticket) {
        return TICKET + SPILT + ticket;
    }

    public static String getUserKey(int userId) {
        return USER + SPILT + userId;
    }
}
