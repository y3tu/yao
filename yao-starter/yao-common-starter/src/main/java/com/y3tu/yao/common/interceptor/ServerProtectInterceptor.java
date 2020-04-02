package com.y3tu.yao.common.interceptor;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.common.constant.Constant;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 微服务防护拦截 限制所有对服务的请求都必须经过网关
 *
 * @author y3tu
 */
public class ServerProtectInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 从请求头中获取 Gateway Token
        String token = request.getHeader(Constant.GATEWAY_TOKEN_HEADER);
        String zuulToken = new String(Base64Utils.encode(Constant.GATEWAY_TOKEN_VALUE.getBytes()));
        // 校验 Gateway Token的正确性
        if (StrUtil.equals(zuulToken, token)) {
            return true;
        } else {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getOutputStream().write(JsonUtil.toJson(R.error("请通过网关获取资源")).getBytes());
            return false;
        }
    }
}
