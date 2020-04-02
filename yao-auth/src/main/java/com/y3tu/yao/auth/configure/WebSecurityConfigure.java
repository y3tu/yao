package com.y3tu.yao.auth.configure;

import com.y3tu.yao.auth.filter.ValidateCodeFilter;
import com.y3tu.yao.auth.mobile.MobileAuthenticationFailHandler;
import com.y3tu.yao.auth.mobile.MobileAuthenticationFilter;
import com.y3tu.yao.auth.mobile.MobileAuthenticationProvider;
import com.y3tu.yao.auth.mobile.MobileAuthenticationSuccessHandler;
import com.y3tu.yao.auth.service.impl.UserDetailServiceImpl;
import com.y3tu.yao.common.constant.EndpointConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * WebSecurity配置
 *
 * @author y3tu
 */
@Configuration
@EnableWebSecurity
@Slf4j
@Order(2)
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private UserDetailServiceImpl userDetailsService;
    @Autowired
    private MobileAuthenticationSuccessHandler mobileAuthenticationSuccessHandler;
    @Autowired
    private MobileAuthenticationFailHandler mobileAuthenticationFailHandler;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config
                = http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .requestMatchers().antMatchers(EndpointConstant.OAUTH_ALL, EndpointConstant.MOBILE_TOKEN_URL)
                .and()
                .authorizeRequests();
        config.antMatchers(EndpointConstant.MOBILE_TOKEN_URL).permitAll();
        config.antMatchers(EndpointConstant.OAUTH_ALL).authenticated()
                .and().csrf().disable();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 注入自定义的userDetailsService实现，获取用户信息，设置密码加密方式
     *
     * @param authenticationManagerBuilder
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .authenticationProvider(mobileAuthenticationProvider())
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }


    @Bean
    public AuthenticationProvider mobileAuthenticationProvider() {
        MobileAuthenticationProvider mobileAuthenticationProvider = new MobileAuthenticationProvider();
        return mobileAuthenticationProvider;
    }

    /**
     * 自定义登陆过滤器
     *
     * @return
     */
    @Bean
    public MobileAuthenticationFilter mobileAuthenticationFilter() {
        MobileAuthenticationFilter filter = new MobileAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        filter.setAuthenticationSuccessHandler(mobileAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(mobileAuthenticationFailHandler);
        return filter;
    }

}