package com.community.service;

import com.community.dao.LoginTicketMapper;
import com.community.entity.LoginTicket;
import com.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author aptx
 */
@Service
public class LoginTicketServer implements CommunityConstant {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    public int findLoginTicketByTicket(String ticket){
        if(StringUtils.isBlank(ticket)){
            return TICKET_NOT_FIND;
        }
        LoginTicket loginTicket = loginTicketMapper.findByTicket(ticket);
        if(new Date().after(loginTicket.getExpired())){
            return TICKET_OVERDUE;
        }

        return TICKET_FIND_SUCCESS;
    }

    public void addLoginTicket(LoginTicket loginTicket) {
        if(loginTicket!=null){
            loginTicketMapper.addLoginTicket(loginTicket);
        }
    }
}
