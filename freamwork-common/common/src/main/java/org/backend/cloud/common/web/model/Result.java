package org.backend.cloud.common.web.model;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @author ruoyi
 */
public class Result<T> implements Serializable {

  private static final long serialVersionUID = -6149948941889889657L;
  public static final int SUCCESS_STATUS = 1;
  public static final int FAIL_STATUS = 0;

  private String code;
  private int status;
  private String msg;
  private T data;


  public static <T> Result<T> success(T data) {
    return ResultBuilder.aResult()
        .data(data)
        .status(SUCCESS_STATUS)
        .msg("success")
        .build();
  }

  public static <T> Result<T> success(T data, String msg) {
    return ResultBuilder.aResult()
        .data(data)
        .status(SUCCESS_STATUS)
        .msg(msg)
        .build();
  }

  public static <T> Result<T> fail(String msg) {
    return ResultBuilder.aResult()
        .data(null)
        .status(FAIL_STATUS)
        .msg(msg)
        .build();
  }

  public static <T> Result<T> fail(T data) {
    return ResultBuilder.aResult()
        .data(data)
        .status(FAIL_STATUS)
        .msg("fail")
        .build();
  }

  public Result() {
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }
}
