package org.backend.cloud.authentication.handler;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.backend.cloud.common.utils.JSON;
import org.backend.cloud.common.web.model.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 全局异常处理器
 * @author liqi
 */
@Component
public class DeniedHandler implements AccessDeniedHandler {

    @SuppressWarnings("deprecation")
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String error = "请求Url:" + request.getRequestURI() + " 鉴权失败:" + accessDeniedException.getMessage();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.print(JSON.stringify(Result.success(HttpStatus.UNAUTHORIZED.value(), error)));
        writer.flush();
        writer.close();
    }
}
