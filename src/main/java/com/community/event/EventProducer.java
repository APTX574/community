package com.community.event;

import com.alibaba.fastjson.JSONObject;
import com.community.entity.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author aptx
 */
@Component
public class EventProducer {

    Logger logger= LoggerFactory.getLogger(EventProducer.class);
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    public void addEvent(Event event) {
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
        logger.info("生成者生产了消息"+event.toString());
    }
}
