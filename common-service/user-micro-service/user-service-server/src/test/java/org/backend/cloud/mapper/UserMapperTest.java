package org.backend.cloud.mapper;

import java.util.List;
import javax.swing.Spring;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.model.user.entity.SystemUser;
import org.backend.cloud.user.UserApplication;
import org.backend.cloud.user.mapper.UserMapper;
import org.backend.cloud.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class UserMapperTest {

  @Autowired
  private UserMapper userMapper;

  @Test
  public void findUserList() {
    List<SystemUser> systemUsers = userMapper.selectList(null);
    System.out.println(JSON.stringify(systemUsers));
  }

  @Test
  public void getByUserId() {
    SystemUser systemUser = userMapper.selectById(1L);
    System.out.println(systemUser);
  }
}
