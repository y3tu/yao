package com.y3tu.yao.common.configure;

import com.y3tu.yao.common.handler.AuthAccessDeniedHandler;
import com.y3tu.yao.common.handler.AuthExceptionEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * 权限异常翻译配置
 *
 * @author y3tu
 */
public class AuthExceptionConfigure {

    @Bean
    @ConditionalOnMissingBean(name = "accessDeniedHandler")
    public AuthAccessDeniedHandler accessDeniedHandler() {
        return new AuthAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authenticationEntryPoint")
    public AuthExceptionEntryPoint authenticationEntryPoint() {
        return new AuthExceptionEntryPoint();
    }

}
