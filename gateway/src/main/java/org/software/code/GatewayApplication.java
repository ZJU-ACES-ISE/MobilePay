package org.software.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * GatewayApplication 是 Spring Boot 应用的主类，用于启动网关应用程序。
 * 该类通过一系列注解进行配置，以满足应用程序的特定需求。
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
// @SpringBootApplication 是一个组合注解，它包含了 @Configuration、@EnableAutoConfiguration 和 @ComponentScan。
// 此注解会自动配置 Spring Boot 应用程序，并扫描当前包及其子包下的组件。
// exclude = DataSourceAutoConfiguration.class 表示排除 Spring Boot 自动配置数据源的功能，
// 若应用程序不需要使用数据库连接，则可以使用此配置来避免不必要的数据源配置。
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
// @EnableFeignClients 注解用于启用 Spring Cloud OpenFeign 客户端功能。
// OpenFeign 是一个声明式的 HTTP 客户端，它可以帮助我们更方便地调用其他服务的接口。
// 启用该注解后，Spring 会自动扫描带有 @FeignClient 注解的接口，并为其创建代理实现。
@EnableFeignClients
public class GatewayApplication {

    /**
     * 这是 Java 程序的入口方法，Spring Boot 应用程序将从这里开始启动。
     *
     * @param args 命令行参数，在启动应用程序时可以传递一些额外的配置信息。
     */
    public static void main(String[] args) {
        // SpringApplication.run() 方法是 Spring Boot 启动应用程序的核心方法。
        // 它会创建并启动 Spring 应用上下文，自动配置并启动嵌入式服务器（如 Tomcat、Jetty 等）。
        // GatewayApplication.class 表示要启动的应用程序的主类，args 是传递给应用程序的命令行参数。
        SpringApplication.run(GatewayApplication.class, args);
    }
}