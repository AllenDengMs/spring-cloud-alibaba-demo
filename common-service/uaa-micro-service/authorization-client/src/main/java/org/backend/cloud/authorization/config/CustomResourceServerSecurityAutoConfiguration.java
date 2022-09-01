package org.backend.cloud.authorization.config;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

/**
 * <pre>
 *    Spring Security 5.2.2.RELEASE 版本，配置权限。
 *    官方文档：https://docs.spring.io/spring-security/site/docs/5.2.2.RELEASE/reference/pdf/
 *    不同版本的Spring Security，配置方式会不一样，具体请看官方文档。
 * </pre>
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 开启@PreAuthorize注解
public class CustomResourceServerSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

  private static final String[] EXCLUDE_URLS = {"/**/*.js", "/**/*.css", "/**/*.jpg", "/**/*.png",
      "/**/*.gif", "/doc.html*", "/doc.html#/**",
      "/v2/**", "/errors", "/error", "/favicon.ico", "/swagger-ui.html/**", "/swagger-ui/**",
      "/webjars/**",
      "/swagger-resources/**", "/auth/login"};

  /**
   * 全局拦截配置
   */
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers(EXCLUDE_URLS).permitAll() // 普通API不拦截
        .anyRequest().authenticated() // 其他一律拦截
        .and()
        .oauth2ResourceServer()
        .jwt()
        .jwtAuthenticationConverter(jwtAuthenticationConverter());
  }

  /**
   * 从JWT中解析出权限，这里可以指定权限读取字段
   */
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    MyJwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new MyJwtGrantedAuthoritiesConverter();
    grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities"); // 指定权限在JWT payload中的哪个字段
    grantedAuthoritiesConverter.setAuthorityPrefix("");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }
}