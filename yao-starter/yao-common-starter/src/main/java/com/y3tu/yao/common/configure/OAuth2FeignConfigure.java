package com.y3tu.yao.common.configure;

import com.google.common.net.HttpHeaders;
import com.y3tu.yao.common.constant.Constant;
import com.y3tu.yao.common.util.YaoUtil;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Base64Utils;

/**
 * OAuth2 Feign配置
 *
 * @author y3tu
 */
public class OAuth2FeignConfigure {

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return requestTemplate -> {
            // 请求头中添加 Gateway Token
            String zuulToken = new String(Base64Utils.encode(Constant.GATEWAY_TOKEN_VALUE.getBytes()));
            requestTemplate.header(Constant.GATEWAY_TOKEN_HEADER, zuulToken);
            // 请求头中添加原请求头中的 Token
            String authorizationToken = YaoUtil.getCurrentTokenValue();
            requestTemplate.header(HttpHeaders.AUTHORIZATION, Constant.OAUTH2_TOKEN_TYPE + authorizationToken);
        };
    }
}
