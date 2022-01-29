package com.community;

import com.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    String subject="测试的标题";
    String content = "<h1>内容<h1>";
    String to="2659332353@qq.com";

    @Test
    public void sendTest(){
        mailClient.sendMail(to,subject,content);
    }
    @Test
    public void sendHtmlTest(){
        Context context=new Context();
        context.setVariable("username","21");
        String content1=templateEngine.process("/mail/demo.html",context);
        mailClient.sendMail(to,subject,content1);

    }
}
