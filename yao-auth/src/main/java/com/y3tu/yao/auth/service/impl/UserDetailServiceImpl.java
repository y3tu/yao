package com.y3tu.yao.auth.service.impl;

import com.y3tu.yao.auth.service.AuthService;
import com.y3tu.yao.common.constant.ParamsConstant;
import com.y3tu.yao.common.util.YaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 默认用户查询的实现
 *
 * @author y3tu
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest httpServletRequest = YaoUtil.getHttpServletRequest();
        String loginType = (String) httpServletRequest.getAttribute(ParamsConstant.LOGIN_TYPE);
        return authService.transformAuthUser(username, null, loginType);
    }
}
