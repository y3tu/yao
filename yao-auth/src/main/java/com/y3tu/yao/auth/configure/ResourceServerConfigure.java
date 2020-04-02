package com.y3tu.yao.auth.configure;

import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.auth.properties.AuthProperties;
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
 * 资源配置服务器
 *
 * @author y3tu
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfigure extends ResourceServerConfigurerAdapter {

    @Autowired
    private AuthAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private AuthExceptionEntryPoint exceptionEntryPoint;
    @Autowired
    private AuthProperties properties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] anonUrls = StrUtil.split(properties.getAnonUrl(), ",");

        http.csrf().disable()
                .requestMatchers().antMatchers(EndpointConstant.ALL)
                .and()
                .authorizeRequests()
                .antMatchers(anonUrls).permitAll()
                .antMatchers(EndpointConstant.ALL).authenticated()
                .and().httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(exceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }
}
