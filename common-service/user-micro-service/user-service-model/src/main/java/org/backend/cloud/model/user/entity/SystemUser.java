package org.backend.cloud.model.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.cloud.common.model.BaseEntity;

/**
 * 和数据库的user表有映射关系 sys_user
 */
@Data
@NoArgsConstructor
@TableName("sys_user")
public class SystemUser extends BaseEntity {

  private static final long serialVersionUID = -2561437283033622267L;

  @TableId
  private Long userId;

  /** 用户类型（00系统用户） */
  private String userType;

  /** 部门ID */
  private Long deptId;

  /** 用户账号 */
  private String userName;

  /** 用户昵称 */
  private String nickName;

  /** 用户邮箱 */
  private String email;

  /** 手机号码 */
  private String phonenumber;

  /** 用户性别 */
  private String sex;

  /** 用户头像 */
  private String avatar;

  /** 密码 */
  private String password;

  /** 帐号状态（0正常 1停用） */
  private String status;

  /** 删除标志（0代表存在 2代表删除） */
  private String delFlag;

  /** 最后登录IP */
  private String loginIp;

  /** 最后登录时间 */
  private Date loginDate;

}
