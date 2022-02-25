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
    int ACTIVATION_FAIL = 2;

    /**
     * 登录成功
     */
    int LOGIN_SUCCESS = 3;

    /**
     * 登录失败，密码错误
     */
    int LOGIN_PASSWORD_ERROR = 4;

    /**
     * 登录失败，无法查找到用户
     */
    int LOGIN_GET_USER_FAIL = 5;

    /**
     * 账号验证正确，但是未激活
     */
    int LOGIN_NOT_ACTIVATION = 8;
    /**
     * 无法查找到ticket
     */
    int TICKET_NOT_FIND = 6;

    /**
     * 查找到ticket，但是已经过期
     */
    int TICKET_OVERDUE = 7;

    /**
     * 查找到ticket且未过期
     */
    int TICKET_FIND_SUCCESS = 9;

    /**
     * 修改密码传过来的旧密码不正确
     */
    int CHANGE_PWD_ERROR_OLD_PWD = 10;

    /**
     * 修改密码成功
     */
    int CHANGE_PWD_SUCCESS = 11;

    /**
     * 未登录
     */
    int UN_LOGIN = 12;

    /**
     * 实体类型帖子
     */
    int ENTITY_TYPE_POST = 0;
    /**
     * 实体类型评论
     */
    int ENTITY_TYPE_COMMENT = 1;

    int ENTITY_TYPE_USER=2;
    /**
     * 评论每行显示条数
     */
    int COMMENT_LIMIT = 5;

    /**
     * 未读的消息类型
     */
    int UNREAD_MESSAGE = 0;
    /**
     * 已读的消息类型
     */
    int READ_MESSAGE = 1;
    /**
     * 未读的通知类型
     */
    int UNREAD_NOTICE = 2;
    /**
     * 已读的通知类型
     */
    int READ_NOTICE = 3;

    /**
     * 评论通知
     */
    int COMMENT_NOTICE_TYPE = -1;

    /**
     * 点赞通知
     */
    int PRAISE_NOTICE_TYPE = -2;
    /**
     * 关注通知
     */
    int FELLOW_NOTICE_TYPE = -3;

    /**
     * 验证码过期时间
     */
    long KAPTCHA_TIME_OUT = 1800;

    /**
     * 评论时间主题
     */
    String TOPIC_COMMENT = "comment";

    /**
     * 点赞时间主题
     */
    String TOPIC_LIKE = "like";

    /**
     * 关注时间主题
     */
    String TOPIC_FOLLOW = "follow";
}
