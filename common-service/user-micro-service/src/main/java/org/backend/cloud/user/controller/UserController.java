package org.backend.cloud.user.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  @GetMapping("/{userId}")
  public Map<String, Object> getUser(@PathVariable String userId) {
    Map<String, Object> response = new HashMap<>();
    response.put("userId", userId);
    response.put("userName", "张三");
    return response;
  }

}
