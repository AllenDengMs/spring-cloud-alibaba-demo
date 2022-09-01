package org.backend.cloud.model.user.dto;

import java.io.Serializable;
import lombok.Data;

@Data
public class UsernameAndPasswordDto implements Serializable {

  private static final long serialVersionUID = 8988611032282429673L;

  private Long userId;
  private String username;
  private String password;

}
