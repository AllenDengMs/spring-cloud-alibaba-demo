package org.backend.cloud.api.gateway.filter;

import java.util.Objects;
import java.util.Optional;
import org.backend.cloud.authentication.constains.LoginSessionRedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(-1)
@Component
public class AuthorizeFilter implements GlobalFilter {

  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    // 获取token，token相当于有状态登录的sessionId
    String jwt = Optional
        .ofNullable(exchange.getRequest().getHeaders().getFirst("token"))
        .map(token -> redisTemplate.opsForValue().get(LoginSessionRedisKey.loginInfoKey(token)))
        .map(objFromRedis -> "Bearer ".concat(objFromRedis.toString()))
        .orElse(null);
    if (!Objects.isNull(jwt)) {
      // 修改header。将token换成jwt
      ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
      requestBuilder.headers(header -> header.remove("token"));
      requestBuilder.headers(header -> header.set("Authorization", jwt));
      exchange.mutate().request(requestBuilder.build()).build();
    }
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