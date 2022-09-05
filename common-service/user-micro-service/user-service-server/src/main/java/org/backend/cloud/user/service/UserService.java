package org.backend.cloud.user.service;

import java.util.Set;
import org.backend.cloud.model.user.entity.SystemUser;

public interface UserService {

  SystemUser getUserById(long userId);

  SystemUser getSystemUserByUsername(String username);

  Set<String> getAuthoritiesByUserId(long longValue);
}
