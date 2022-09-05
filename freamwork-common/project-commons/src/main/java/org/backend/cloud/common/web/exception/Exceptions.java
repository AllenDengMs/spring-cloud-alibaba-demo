package org.backend.cloud.common.web.exception;

import org.backend.cloud.common.web.constant.HttpStatus;

/**
 * 抛异常的工具类
 */
public class Exceptions {

  private static HttpStatus defaultHttpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

  public static void error(String message) {
    throw new BaseException(message, defaultHttpStatus);
  }

  public static void error(boolean throwException, String message) {
    if (throwException) {
      throw new BaseException(message, defaultHttpStatus);
    }
  }

  public static void error(HttpStatus httpStatus) {
    throw new BaseException(httpStatus);
  }

  public static void error(String message, String code) {
    BaseException baseException = new BaseException(message, defaultHttpStatus);
    baseException.setCode(code);
    throw baseException;
  }

  public static void error(String message, HttpStatus httpStatus) {
    throw new BaseException(message, httpStatus);
  }

  public static void error(String message, Throwable e) {
    throw new BaseException(message, e, defaultHttpStatus);
  }

  public static void error(String message, HttpStatus httpStatus, String errorCode) {
    BaseException baseException = new BaseException(message, httpStatus);
    baseException.setCode(errorCode);
    throw baseException;
  }
}
