package org.backend.cloud.authorization.utils;

import org.backend.cloud.model.user.dto.UserLoginInfo;

/**
 * 获取当前登录用户的信息
 */
public class CurrentUserInfo {

  private static final ThreadLocal<UserLoginInfo> userThreadLocal = new ThreadLocal<>();

  public static UserLoginInfo getUser() {
    return userThreadLocal.get();
  }

  public static void setUser(UserLoginInfo user) {
    userThreadLocal.set(user);
  }
}
