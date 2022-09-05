package org.backend.cloud.model.user.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 简单用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginInfo {

  private Long userId;
  private String nickName; // 昵称
  private String userName; // 登录用户名
  private Set<String> authorities; // 拥有的权限

}
