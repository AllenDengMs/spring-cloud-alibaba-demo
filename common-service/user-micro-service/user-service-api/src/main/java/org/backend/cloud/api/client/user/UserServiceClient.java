package org.backend.cloud.api.client.user;

import org.backend.cloud.model.user.dto.SimpleUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("user-service")
@RequestMapping("/users")
public interface UserServiceClient {

  @GetMapping("/{userId}")
  SimpleUser getUser(@PathVariable("userId") String userId);

}