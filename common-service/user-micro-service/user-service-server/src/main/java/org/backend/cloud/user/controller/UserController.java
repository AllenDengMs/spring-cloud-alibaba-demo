package org.backend.cloud.user.controller;

import java.util.Set;
import org.backend.cloud.common.web.model.Result;
import org.backend.cloud.model.user.entity.SystemUser;
import org.backend.cloud.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/users")
public class UserController {

  @Autowired
  private UserService userService;

  @GetMapping("/{userId}")
  public Result<SystemUser> getUser(@PathVariable("userId") String userId) {
    SystemUser systemUser = userService.getUserById(Long.valueOf(userId));
    return Result.success(systemUser);
  }

  @GetMapping("/name/{username}")
  public Result<SystemUser> getSystemUserByUsername(@PathVariable("username") String username) {
    SystemUser systemUser = userService.getSystemUserByUsername(username);
    return Result.success(systemUser);
  }

  @GetMapping("/{userId}/authorities")
  public Result<Set<String>> getAuthoritiesByUserId(@PathVariable("userId") String userId) {
    Set<String> result = userService.getAuthoritiesByUserId(Long.valueOf(userId).longValue());
    return Result.success(result);
  }
}