package org.backend.cloud.authentication.service.impl;

import java.util.Objects;
import org.backend.cloud.api.client.user.UserServiceClient;
import org.backend.cloud.authentication.model.SystemUserDetails;
import org.backend.cloud.common.web.exception.Exceptions;
import org.backend.cloud.model.user.entity.SystemUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsernamePasswordUserDetailsService implements UserDetailsService {

  @Autowired
  private UserServiceClient userServiceClient;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    SystemUser user = userServiceClient.getSystemUserByUsername(username);
    Exceptions.error(Objects.isNull(user),"{user.not.register.error:用户未注册，请先注册}");
    // 不在这个地方查询用户拥有权限，而是在 SecuritySuccessHandler 里面查询权限。
    // 因为这里是认证系统，不做授权操作，所以也不会验证权限。账号+密码匹配上了，认证通过，不校验权限。
    return new SystemUserDetails(user.getUserName(), user.getPassword(), user, null);
  }
}