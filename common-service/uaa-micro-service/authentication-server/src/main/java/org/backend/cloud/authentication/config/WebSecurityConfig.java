package org.backend.cloud.authentication.config;

import java.util.List;
import org.backend.cloud.authentication.provider.UsernamePasswordAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private static final String[] EXCLUDE_URLS = {"/**/*.js", "/**/*.css", "/**/*.jpg", "/**/*.png",
      "/**/*.gif",
      "/v2/**", "/errors", "/error", "/favicon.ico", "/swagger-ui.html/**", "/swagger-ui/**",
      "/webjars/**",
      "/swagger-resources/**", "/auth/login"};

  @Autowired
  private AuthenticationSuccessHandler securitySuccessHandler;
  @Autowired
  private AuthenticationFailureHandler securityFailureHandler;
  @Autowired
  private AccessDeniedHandler deniedHandler;
  @Autowired
  private AuthenticationEntryPoint entryPoint;
  @Autowired
  private List<AuthenticationProvider> authenticationProviderList;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  public static void main(String[] args) {
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    String encode = passwordEncoder.encode("123456");
    System.out.println(encode);
  }

  @Bean
  public AuthenticationProvider usernamePasswordAuthenticationProvider(
      UserDetailsService usernamePasswordUserDetailsService) {
    return new UsernamePasswordAuthenticationProvider(usernamePasswordUserDetailsService);
  }

  /**
   * 配置http请求拦截器
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // 把所有的注入到spring容器中的认证提供者，拿出来，放入到AuthenticationManager管理起来；
    for (AuthenticationProvider authenticationProvider : authenticationProviderList) {
      http.authenticationProvider(authenticationProvider);
    }

    http.authorizeRequests()
        .antMatchers(EXCLUDE_URLS).permitAll()
        .antMatchers("/r/1").hasAnyAuthority("p1")
        .antMatchers("/r/2").hasAnyAuthority("p2")
        .antMatchers("/r/**").authenticated()
        .anyRequest().permitAll();

    // 全局异常配置
    http.exceptionHandling().accessDeniedHandler(deniedHandler).authenticationEntryPoint(entryPoint);
    // 1、表单操作
    FormLoginConfigurer<HttpSecurity> formLogin = http.formLogin();
    // 表单请求成功处理器、失败处理器;与loginPage冲突，配置后，loginPage不生效
    formLogin.successHandler(securitySuccessHandler).failureHandler(securityFailureHandler);
    // 表单提交的post请求地址,用户参数名称
    formLogin.loginProcessingUrl("/auth/login");

    http.sessionManagement().disable()
        .logout().disable()
        .httpBasic().disable()
        .csrf().disable();
  }

}
