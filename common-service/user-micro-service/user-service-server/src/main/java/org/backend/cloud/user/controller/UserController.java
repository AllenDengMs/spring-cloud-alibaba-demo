package org.backend.cloud.user.controller;

import org.backend.cloud.common.web.model.Result;
import org.backend.cloud.model.user.dto.SimpleUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @GetMapping("/{userId}")
  public Result<SimpleUser> getUser(@PathVariable("userId") String userId) {
    SimpleUser simpleUser = new SimpleUser();
    simpleUser.setUserId(1L);
    simpleUser.setNickname("张三");
    return Result.success(simpleUser);
  }

}