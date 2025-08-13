package org.software.code.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * DatabaseExceptionAspect 是一个切面类，用于处理数据库操作相关的异常。
 * 当指定的数据库操作方法抛出异常时，该切面会捕获异常并进行统一处理，
 * 同时根据不同的方法名抛出相应的业务异常。
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
// @Aspect 注解表明这是一个切面类，Spring AOP 会识别并处理该类中的通知方法。
@Aspect
// @Component 注解将该类标记为 Spring 组件，以便 Spring 容器自动扫描并管理该类的实例。
@Component
public class DatabaseExceptionAspect {

    // 创建一个 Logger 实例，用于记录日志，日志记录的类为当前 DatabaseExceptionAspect 类。
    private static final Logger logger = LoggerFactory.getLogger(DatabaseExceptionAspect.class);

}