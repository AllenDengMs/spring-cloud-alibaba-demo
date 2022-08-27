package org.backend.cloud.openfegin.config;

import feign.RequestInterceptor;
import feign.codec.Decoder;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import org.backend.cloud.openfegin.util.FeignResultDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class CustomizedFeignAutoConfiguration {

  private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

  /**
   * 解析响应参数
   * @return
   */
  @Bean
  public Decoder feignDecoder() {
    return new FeignResultDecoder();
  }

  /**
   * <pre>
   *   参考文档：https://juejin.cn/post/7111656818060820493
   *    自定义的请求头处理类，处理服务发送时的请求头；
   *    将服务接收到的请求头中的uniqueId和token字段取出来，并设置到新的请求头里面去转发给下游服务
   *    比如A服务收到一个请求，请求头里面包含token字段，A处理时会使用Feign客户端调用B服务
   *    那么token这个字段就会添加到请求头中一并发给B服务；
   * </pre>
   *
   */
  @Bean
  public RequestInterceptor requestInterceptor() {
    return requestTemplate -> {
      ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
      if (attrs != null) {
        HttpServletRequest request = attrs.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
          while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            // 遍历请求头里面的属性字段，token添加到新的请求头中转发到下游服务
            if ("token".equalsIgnoreCase(name)) {
              logger.debug("添加自定义请求头key:" + name + ",value:" + value);
              requestTemplate.header(name, value);
            } else {
              logger.debug("FeignHeadConfiguration: " +
                  "非自定义请求头key:" + name + ",value:" + value + "不需要添加!");
            }
          }
        } else {
          logger.warn("FeignHeadConfiguration: " + "获取请求头失败！");
        }
      }
    };
  }

}