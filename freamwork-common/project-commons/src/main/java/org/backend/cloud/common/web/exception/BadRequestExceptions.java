package org.backend.cloud.common.web.exception;

import org.backend.cloud.common.web.constant.HttpStatus;

public class BadRequestExceptions {

  private static HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

  public static void error(String message) {
    Exceptions.error(message, httpStatus);
  }

  public static void error(boolean throwException, String message) {
    if (throwException) {
      Exceptions.error(message, httpStatus);
    }
  }

  public static void error(String message, String errorCode) {
    Exceptions.error(message, httpStatus, errorCode);
  }
}
