package org.backend.cloud.web.exception;

import javax.servlet.http.HttpServletResponse;
import org.backend.cloud.common.web.constant.HttpStatus;
import org.backend.cloud.common.web.exception.BaseException;
import org.backend.cloud.common.web.model.Result;
import org.backend.cloud.common.web.model.ResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WebGlobalExceptionHandler {

  private Logger log = LoggerFactory.getLogger(getClass());

  @ExceptionHandler(value = Exception.class)
  public Result exceptionHandler(HttpServletResponse response,Exception e) {
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    log.info("服务器异常", e);
    return Result.fail("服务器异常");
  }

  @ExceptionHandler(value = BaseException.class)
  public Result exceptionHandler(HttpServletResponse response, BaseException e) {
    response.setStatus(e.getHttpStatus().value());
    return createResult(e);
  }

  private Result createResult(BaseException e) {
    return ResultBuilder.aResult()
        .msg(e.getMessage())
        .status(Result.FAIL_STATUS)
        .code(e.getCode())
        .build();
  }
}
