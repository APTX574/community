package com.community;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerTest.class.getName());

    @Test
    public void test() {
        for (int i = 0; i < 99999; i++) {
            LOGGER.error("error");
            LOGGER.info("info");
        }

    }
}
