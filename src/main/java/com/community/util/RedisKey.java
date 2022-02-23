package com.community.util;

/**
 * 获取Redis的key值
 *
 * @author aptx
 */
public class RedisKey {

    public static final String SPILT = ":";
    public static final String ENTITY_LIKE = "entity:like";
    private static final String USER_LIKE = "user:like";
    public static final String USER_FOLLOW = "user:follow";
    public  static final String USER_FOLLOWED="user:followed";

    public static String getEntityLikeKey(int entityType, int entityId) {
        return ENTITY_LIKE + SPILT + entityType + SPILT + entityId;
    }

    public static String getUserLikeKey(int userId) {
        return USER_LIKE + SPILT + userId;
    }

    public static String getUserFollowKey(int userId) {
        return USER_FOLLOW + SPILT + userId;
    }
    public static String getUserFollowedKey(int userId){
        return USER_FOLLOWED + SPILT + userId;
    }
}
