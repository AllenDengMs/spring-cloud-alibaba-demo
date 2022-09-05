package org.backend.cloud.authentication.constains;

public class LoginSessionRedisKey {

  private static final String REDIS_KEY_USER_LOGIN_INFO = "org:backend:cloud:authentication:user:token:";

  public static String loginInfoKey(String token) {
    return REDIS_KEY_USER_LOGIN_INFO.concat(token);
  }
}
