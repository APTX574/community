package com.community.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 * @author aptx
 */
public class Page {
    /**
     * 当前页码
     */
    private int current = 1;

    /**
     * 每页显示行数
     */
    private int limit = 5;
    /**
     * 总行数 通过查询获得
     */
    private int rows;

    /**
     * 查询路径用于复用的分页连接
     */
    private String path;

    /**
     * 获取当前页的起始行
     *
     * @return 当前页的起始行
     */
    public int getOffset() {
        return (current - 1) * limit;
    }

    /**
     * 获取总的页码数
     *
     * @return 总的页码数
     */
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        }
        return rows / limit + 1;
    }

    /**
     * 获取起始页
     *
     * @return 返回起始页
     */
    public int getFromPage() {
        if(getTotal()<6){
            return 1;
        }
        if (current <= 2) {
            return 1;
        }
        return current - 1;
    }

    /**
     * 获取终止页
     * @return 返回终止页
     */
    public int getTo() {
        if(getTotal()<6){
            return getTotal();
        }
        return Math.min(current + 3, getTotal());
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <=100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
