package com.community.service;

import com.community.dao.LoginTicketMapper;
import com.community.entity.LoginTicket;
import com.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aptx
 */
@Service
public class LoginTicketServer implements CommunityConstant {

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    public Map<String ,Object> verifyTicket(String ticket){
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isBlank(ticket)){
            map.put("msg",TICKET_NOT_FIND);
            return map;
        }
        LoginTicket loginTicket = loginTicketMapper.findByTicket(ticket);
        if(new Date().after(loginTicket.getExpired())){
            map.put("msg",TICKET_OVERDUE);
            return map;
        }
        map.put("msg",TICKET_FIND_SUCCESS);
        map.put("userId",loginTicket.getUserId());
        return map;
    }

    public void addLoginTicket(LoginTicket loginTicket) {
        if(loginTicket!=null){
            loginTicketMapper.addLoginTicket(loginTicket);
        }
    }

    public void updateStatus(String ticket, int status) {
        loginTicketMapper.updateStatusByTicket(ticket,status);
    }


}
