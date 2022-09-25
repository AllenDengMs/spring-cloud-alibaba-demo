package org.backend.cloud.authentication.api.client;

import java.util.Set;
import org.backend.cloud.model.user.entity.SystemUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "user-service", configuration = InjectJwtTokenToHeaderConfiguration.class)
@RequestMapping("/system/users")
public interface AuthUserServiceClient {

  @GetMapping("/{userId}")
  SystemUser getSystemUserById(@PathVariable("userId") String userId);

  @GetMapping("/name/{username}")
  SystemUser getSystemUserByUsername(@PathVariable("username") String username);

  @GetMapping("/{userId}/authorities")
  Set<String> getAuthoritiesByUserId(@PathVariable("userId") String userId);

}
