package com.y3tu.yao.common.handler;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.yao.common.util.YaoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 无权限访问处理Handler
 *
 * @author y3tu
 */
@Slf4j
public class AuthAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream().write(JsonUtil.toJson(R.error("没有权限访问该资源")).getBytes());
    }

}
