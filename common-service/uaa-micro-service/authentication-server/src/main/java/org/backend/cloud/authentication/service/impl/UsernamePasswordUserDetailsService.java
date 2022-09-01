package org.backend.cloud.authentication.service.impl;

import org.backend.cloud.authentication.model.SystemUserDetails;
import org.backend.cloud.model.user.dto.UsernameAndPasswordDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsernamePasswordUserDetailsService implements UserDetailsService {

//  @Autowired
//  private LoginService loginService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    UsernameAndPasswordDto user = loginService.getUsernameAndPassword(username);
//    if (user != null) {
//      return new SystemUserDetails(user.getUsername(), user.getPassword(), user, null);
//    }
//    throw new UsernameNotFoundException("用户未注册，请先注册");
    UsernameAndPasswordDto user = new UsernameAndPasswordDto();
    user.setUserId(1L);
    user.setUsername("allen");
    user.setPassword("allen");
    return new SystemUserDetails(user.getUsername(), user.getPassword(), user, null);
  }
}