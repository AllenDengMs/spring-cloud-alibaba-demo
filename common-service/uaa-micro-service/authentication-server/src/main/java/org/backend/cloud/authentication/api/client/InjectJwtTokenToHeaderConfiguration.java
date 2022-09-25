package org.backend.cloud.authentication.api.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class InjectJwtTokenToHeaderConfiguration implements RequestInterceptor {

  @Override
  public void apply(RequestTemplate template) {
    // 因为登录时需要向user-service获取数据，因为没有token，user-service服务拒绝了访问
    // 因此写死一个jwt token。user-service微服务和登录认证服务最好放在同一台机器。
    // user-service微服务和登录认证服务之间的数据交互相当频繁
    template.header(HttpHeaders.AUTHORIZATION, "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJuaWNrTmFtZSI6IuiLpeS-nSIsInVzZXJOYW1lIjoiYWRtaW4iLCJ1c2VySWQiOjEsImF1dGhvcml0aWVzIjpbImFkbWluIl19.jzWLo8ZOUN0t_f8WEOTIzcfs9mioVQNaAQUY9BC9wTFzfnkXQm3b8KZzU74woay913TS25Fmmz0ru3JmxZ1Udt3VnuOsDEuXjouCwHWS4A3EyCa0-ZypsuVMs1U1Yveb9HT6gMENaDdWJN3bK5vrdyKZcYBBZXAZE1d00cLHfWxRxFwv8OKNWChcDDS2NJukPsQHjihpzxCngCBSTTymjOq7GOGwIeL1QFmBb7MOlO9rT1kNDl-SmFH0olFi8b2kGNj7eE8lCG8QCbsnQtOfz8Mf2ovh04DPSMpeopA0AkVFcFXcJCz1XZpV2lD0AxYu-Ix4v7JsPo54sVxeF6kfPQ");
  }

}