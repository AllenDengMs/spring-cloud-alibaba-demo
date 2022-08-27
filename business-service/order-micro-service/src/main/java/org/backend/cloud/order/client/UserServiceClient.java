package org.backend.cloud.order.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("user-service")
@RequestMapping("/users")
public interface UserServiceClient {

  @GetMapping("/{userId}")
  Map<String, Object> getUser(@PathVariable("userId") String userId);

}