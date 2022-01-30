package com.community.util;

/**
 * @author aptx
 */
public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;
    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;
    /**
     * 激活失败
     */
    int ACTIVATION_FAIL=2;

    /**
     * 登录成功
     */
    int LOGIN_SUCCESS=3;

    /**
     * 登录失败，密码错误
     */
    int LOGIN_PASSWORD_ERROR=4;

    /**
     * 登录失败，无法查找到用户
     */
    int LOGIN_GET_USER_FAIL=5;

    /**
     * 账号验证正确，但是未激活
     */
    int LOGIN_NOT_ACTIVATION=9;
    /**
     * 无法查找到ticket
     */
    int TICKET_NOT_FIND=6;

    /**
     * 查找到ticket，但是已经过期
     */
    int TICKET_OVERDUE=7;

    /**
     * 查找到ticket且未过期
     */
    int TICKET_FIND_SUCCESS=8;
}
