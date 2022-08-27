package org.backend.cloud.model.user.entity;

import lombok.Data;

/**
 * 和数据库的user表有映射关系
 */
@Data
public class SystemUser {

  private Long userId;
  private String username;
  private String password;
  private String nickname;

}
