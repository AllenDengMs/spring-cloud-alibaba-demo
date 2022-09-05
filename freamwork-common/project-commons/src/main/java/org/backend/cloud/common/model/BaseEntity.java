package org.backend.cloud.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * Entity基类
 */
@Data
public class BaseEntity implements Serializable {


  private static final long serialVersionUID = 1170021715685710275L;

  /**
   * 创建者
   */
  private String createBy;

  /**
   * 创建时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;

  /**
   * 更新者
   */
  private String updateBy;

  /**
   * 更新时间
   */
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date updateTime;

  /**
   * 备注
   */
  private String remark;

}
