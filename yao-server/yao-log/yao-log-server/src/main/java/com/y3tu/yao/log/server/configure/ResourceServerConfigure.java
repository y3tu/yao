package com.y3tu.yao.log.server.configure;

import com.y3tu.yao.common.annotation.EnableAuthExceptionHandler;
import com.y3tu.yao.common.constant.EndpointConstant;
import com.y3tu.yao.common.handler.AuthAccessDeniedHandler;
import com.y3tu.yao.common.handler.AuthExceptionEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器配置
 *
 * @author y3tu
 */
@Configuration
@EnableResourceServer
@EnableAuthExceptionHandler
public class ResourceServerConfigure extends ResourceServerConfigurerAdapter {

    @Autowired
    private AuthAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthExceptionEntryPoint exceptionEntryPoint;


    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().disable()
                .and().csrf().disable()
                .requestMatchers().antMatchers(EndpointConstant.ALL)
                .and()
                .authorizeRequests()
                .antMatchers(EndpointConstant.ALL).authenticated()
                .and().httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(exceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }
}
