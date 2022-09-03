package org.backend.cloud.common.web.model;

public final class ResultBuilder {

  private Result result;

  private ResultBuilder() {
    result = new Result();
  }

  public static ResultBuilder aResult() {
    return new ResultBuilder();
  }

  public ResultBuilder code(String code) {
    result.setCode(code);
    return this;
  }

  public ResultBuilder status(int status) {
    result.setStatus(status);
    return this;
  }

  public ResultBuilder msg(String msg) {
    result.setMsg(msg);
    return this;
  }

  public <T> ResultBuilder data(T data) {
    result.setData(data);
    return this;
  }

  public Result build() {
    return result;
  }
}
