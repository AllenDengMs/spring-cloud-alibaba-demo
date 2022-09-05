package org.backend.cloud.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.backend.cloud.model.user.entity.SystemUser;

@Mapper
public interface UserMapper extends BaseMapper<SystemUser> {

}
