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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * 未认证的全局处理器
 * 没有认证时，在这里处理结果，不要重定向
 * @author liqi
 *
 */
@Component
public class EntryPoint implements AuthenticationEntryPoint {

    @SuppressWarnings("deprecation")
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        String error = "请求Url:" + request.getRequestURI() + " 认证失败:" + authException.getMessage();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        writer.print(JSON.stringify(Result.success(HttpStatus.UNAUTHORIZED.value(), error)));
        writer.flush();
        writer.close();
    }
}
