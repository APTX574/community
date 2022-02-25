package com.community;

import com.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class FilterTest {

    @Autowired
    SensitiveFilter sensitiveFilter;

    Logger logger = LoggerFactory.getLogger(FilterTest.class);

    @Test
    public void test() {

        sensitiveFilter.init();
        StringBuffer sb = new StringBuffer("1212353414343678247645637829019876543212312214324563748938271652323467382191223321318273289871654322122123212 ");
        System.out.println(sb);
        sensitiveFilter.replace(sb);
        System.out.println(sb);
        logger.error("会话会话或");
        logger.info("ansionafo");
    }
}
