package org.software.code.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/kafka")
public class KafkaTestController {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaTestController.class);
    
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;
    
    @PostMapping("/send")
    public String sendMessage(@RequestParam String message) {
        try {
            kafkaTemplate.send("test-topic", message);
            logger.info("发送Kafka消息成功: {}", message);
            return "消息发送成功: " + message;
        } catch (Exception e) {
            logger.error("发送Kafka消息失败", e);
            return "消息发送失败: " + e.getMessage();
        }
    }
    
    @GetMapping("/test")
    public String testKafkaConnection() {
        try {
            kafkaTemplate.send("test-topic", "Kafka连接测试消息");
            return "Kafka连接测试成功";
        } catch (Exception e) {
            logger.error("Kafka连接测试失败", e);
            return "Kafka连接测试失败: " + e.getMessage();
        }
    }
}