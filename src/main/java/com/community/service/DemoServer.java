package com.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class DemoServer {

    @Autowired
    private TransactionTemplate transactionTemplate;

    /**
     * 隔离级别
     * READ_UNCOMMITTED
     * READ_COMMITTED
     * REPEATABLE_READ
     * SERIALIZABLE
     * 传播级别
     * REQUIRED
     * REQUIRES_NEW
     * NESTED
     * @return fs
     */
    @Transactional(isolation = Isolation.SERIALIZABLE,propagation = Propagation.REQUIRED)
    public String demo1(){
        return "demo";
    }

}
