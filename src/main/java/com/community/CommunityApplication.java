package com.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;

@Controller
@SpringBootApplication
public class CommunityApplication {

    @PostConstruct
    public void init() {
		// 解决Netty启动冲突问题
		// Netty4Utils
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

    public static void main(String[] args) {
        SpringApplication.run(CommunityApplication.class, args);
    }

}

