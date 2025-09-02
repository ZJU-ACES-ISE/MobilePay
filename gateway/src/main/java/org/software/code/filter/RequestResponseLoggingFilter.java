package org.software.code.filter;

import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicReference;

/**
 * RequestResponseLoggingFilter 是一个全局过滤器，实现了 GlobalFilter 和 Ordered 接口。
 * 该过滤器用于记录通过 Spring Cloud Gateway 转发的请求信息，包括请求方法、URI、请求头和请求体，
 * 有助于开发和调试阶段对请求进行监控和分析。
 *
 * @author “101”计划《软件工程》实践教材案例团队
 */
@Component
public class RequestResponseLoggingFilter implements GlobalFilter, Ordered {

  // 日志记录器，用于记录请求信息
  private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

  /**
   * 过滤器的核心方法，用于处理请求并记录相关信息。
   *
   * @param exchange 服务器 Web 交换对象，包含请求和响应信息
   * @param chain 网关过滤链，用于将请求传递给下一个过滤器
   * @return Mono<Void> 异步处理结果
   */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    // 获取当前请求
    ServerHttpRequest request = exchange.getRequest();
    // 记录请求信息
    logRequest(request);
    // 装饰请求并继续过滤链的执行
    return chain.filter(exchange.mutate().request(decorateRequest(request)).build());
  }

  /**
   * 记录请求的详细信息，包括请求方法、URI、请求头和请求体。
   *
   * @param request 服务器请求对象
   */
  private void logRequest(ServerHttpRequest request) {
    StringBuilder logMessage = new StringBuilder();
    logMessage.append("Request Information:\n");
    // 记录请求方法
    logMessage.append("  Method: ").append(request.getMethod()).append("\n");
    // 记录请求 URI
    logMessage.append("  URI: ").append(request.getURI()).append("\n");

    HttpHeaders headers = request.getHeaders();
    logMessage.append("  Headers:\n");
    // 遍历并记录请求头信息
    for (var entry : headers.toSingleValueMap().entrySet()) {
      logMessage.append("    ").append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
    }

    // 记录请求体（此处代码未实际获取请求体内容，仅预留框架）
    AtomicReference<String> bodyRef = new AtomicReference<>();
    logMessage.append("  Request Body:\n");
    logMessage.append("    ").append(bodyRef.get()).append("\n");

    // 输出完整的请求信息日志
    logger.info(logMessage.toString());
  }

  /**
   * 装饰请求对象，当前仅返回原请求的装饰器，未做额外处理。
   *
   * @param request 服务器请求对象
   * @return 装饰后的服务器请求对象
   */
  private ServerHttpRequest decorateRequest(ServerHttpRequest request) {
    return new ServerHttpRequestDecorator(request);
  }

  /**
   * 获取过滤器的执行顺序，设置为最高优先级，确保该过滤器最先执行。
   *
   * @return 过滤器的执行顺序
   */
  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }
}