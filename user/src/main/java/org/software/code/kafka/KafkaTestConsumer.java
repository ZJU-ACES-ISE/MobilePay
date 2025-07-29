package org.software.code.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaTestConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaTestConsumer.class);
    
    @KafkaListener(topics = "test-topic", groupId = "mobilepay")
    public void consume(ConsumerRecord<String, String> record) {
        logger.info("收到Kafka消息 - Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}", 
                   record.topic(), 
                   record.partition(), 
                   record.offset(), 
                   record.key(), 
                   record.value());
    }
}