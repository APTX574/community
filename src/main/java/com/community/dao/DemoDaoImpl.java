package com.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author aptx
 */
@Repository
public class DemoDaoImpl implements DemoDao{
    @Override
    public String select() {
        return "hello DaoDaoDao";
    }
}
