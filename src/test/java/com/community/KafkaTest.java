package com.community;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class KafkaTest {

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @Autowired KafkaProducer kafkaProducer;

    @Test
    public void test(){
        String topic="msg";
        for (int i = 0; i < 5; i++) {
            kafkaProducer.sendMessage(topic,"你吃饭了吗"+i);
        }
        for (int i = 0; i < 5; i++) {
            kafkaProducer.sendMessage("test","你吃饭了吗a"+i);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}



@Component
class KafkaProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String content) {
        kafkaTemplate.send(topic, content);
    }
}

@Component
class KafkaConsumer {
    @KafkaListener(topics="test")
    public void handler(ConsumerRecord record){
        System.out.println(record);
    }

    @KafkaListener(topics="msg")
    public void handler1(ConsumerRecord record){
        System.out.println(record);
    }
}