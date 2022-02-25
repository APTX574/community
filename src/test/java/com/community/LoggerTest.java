package com.community;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerTest {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(LoggerTest.class.getName());

        logger.info("hello JUL");
        //通用的方法
        logger.log(Level.INFO, "info msg");
        //占位符输出变量
        String name = "wqs";
        int age = 1;
        logger.log(Level.INFO, " 用户信息{0},{1}", new Object[]{name, age});

        //设置文件
        FileHandler fileHandler;
        try {
            fileHandler = new FileHandler("/home/aptx/code/log/test.log");
            fileHandler.setEncoding("utf8");
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.severe("server");
            logger.warning("warning");
            logger.info("info");
            logger.fine("fine");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
