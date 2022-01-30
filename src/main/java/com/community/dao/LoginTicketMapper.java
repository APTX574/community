package com.community.dao;

import com.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author aptx
 */
@Mapper
public interface LoginTicketMapper {
    /**
     * @param ticket 需要加入的LoginTicket对象
     * @return 操作数目
     */
    int addLoginTicket(LoginTicket ticket);

    /**
     * 通过ticket改变LoginTicket的状态
     * @param ticket 需要更改的LoginTicket的ticket属性
     * @param status 修改后的状态值
     * @return 修改条数
     */
    int updateStatusByTicket(String ticket, int status);

    /**
     * 通过ticket属性查找对应的LoginTicket对象
     * @param ticket ticket属性
     * @return LoginTicket对象
     */
    LoginTicket findByTicket(String ticket);
}
