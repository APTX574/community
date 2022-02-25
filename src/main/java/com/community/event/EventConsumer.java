package com.community.event;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.hsf.HSFJSONUtils;
import com.community.entity.Event;
import com.community.service.MessageService;
import com.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * @author aptx
 */
@Component
public class EventConsumer implements CommunityConstant {

    Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    MessageService messageService;

    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_FOLLOW, TOPIC_LIKE})
    public void dealCommentFollowAndLike(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            logger.error("消息队列发送的消息为空");
            return;
        }
        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        messageService.addEvent(event);
        logger.info("消费者消费了消息"+event.toString());

    }
}