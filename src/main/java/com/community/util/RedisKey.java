package com.community.util;

/**
 * 获取Redis的key值
 *
 * @author aptx
 */
public class RedisKey {

    public static final String SPILT = ":";
    public static final String ENTITY_LIKE = "entity:like";

    public static String getLikeKey(int entityType, int entityId) {
        return ENTITY_LIKE + SPILT + entityType + SPILT + entityId;
    }
}
