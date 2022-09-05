package org.backend.cloud.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Sets;
import java.util.Objects;
import java.util.Set;
import org.backend.cloud.common.web.exception.BadRequestExceptions;
import org.backend.cloud.model.user.entity.SystemUser;
import org.backend.cloud.user.mapper.UserMapper;
import org.backend.cloud.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserMapper userMapper;

  @Override
  public SystemUser getUserById(long userId) {
    return userMapper.selectById(userId);
  }

  @Override
  public SystemUser getSystemUserByUsername(String username) {
    BadRequestExceptions.error(Objects.isNull(username) || username.isEmpty(),
        "{request.param.not.blank:查询条件不能为null或空串}");
    QueryWrapper<SystemUser> queryWrapper = new QueryWrapper();
    queryWrapper.eq("user_name", username);
    return userMapper.selectOne(queryWrapper);
  }

  @Override
  public Set<String> getAuthoritiesByUserId(long longValue) {
    // TODO
    return Sets.newHashSet("admin");
  }
}
