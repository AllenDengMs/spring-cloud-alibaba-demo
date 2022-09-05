package org.backend.cloud.authentication.model;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;
import org.backend.cloud.model.user.entity.SystemUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户系统的领域模型
 *
 * @author liqi
 */
public class SystemUserDetails implements UserDetails {

  private static final long serialVersionUID = -7127141675788677116L;

  /**
   * 用户名.
   */
  @Setter
  private String username;
  /**
   * 密码.
   */
  @Setter
  private String password;
  /**
   * 用户角色信息
   */
  @Setter
  private Collection<? extends GrantedAuthority> authorities;
  /**
   * 当前用户信息
   */

  @Getter
  @Setter
  private SystemUser user;

  public SystemUserDetails() {
    super();
  }

  public SystemUserDetails(String username, String password, SystemUser user,
      Collection<? extends GrantedAuthority> authorities) {
    this.username = username;
    this.password = password;
    this.authorities = authorities;
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    // 账户未过期
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // 帐户未锁定
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // 凭据未过期
    return true;
  }

  @Override
  public boolean isEnabled() {
    // 账户可用
    return true;
  }
}
