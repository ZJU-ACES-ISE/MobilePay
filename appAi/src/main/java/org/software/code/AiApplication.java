package org.software.code;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * AI模块启动类
 */
@SpringBootApplication
@MapperScan("org.software.code.ai.mapper")
public class AiApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(AiApplication.class, args);
    }
} 