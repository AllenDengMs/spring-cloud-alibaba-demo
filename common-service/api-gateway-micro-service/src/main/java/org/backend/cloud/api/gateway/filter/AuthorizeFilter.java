package org.backend.cloud.api.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    // TODO 这里可以校验登录状态
    if (true) {
      // 放行
      return chain.filter(exchange);
    }
    // 拦截
    // 禁止访问，设置状态码
    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
    // 结束处理
    return exchange.getResponse().setComplete();
  }
}