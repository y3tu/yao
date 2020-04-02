package com.y3tu.yao.auth.service;

import com.y3tu.yao.common.entity.AuthUser;
import com.y3tu.yao.upms.client.entity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限服务
 *
 * @author y3tu
 */
public interface AuthService {
    /**
     * 校验权限
     * @param authRequest
     * @return 是否有权限
     */
    boolean decide(HttpServletRequest authRequest);

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User findUserByName(String username);

    /**
     * 根据手机号码查询用户
     * @param mobile
     * @return
     */
    User findUserByMobile(String mobile);

    /**
     * 根据用户名获取用户权限
     * @param username
     * @return
     */
    String findUserPermissions(String username);

    /**
     * 根据用户名和用户密码注册用户
     * @param username
     * @param password
     * @return
     */
    User registerUser(String username,String password);

    /**
     * 根据用户名、手机号、登录类型把用户信息转换成权限用户
     * @param username
     * @param mobile
     * @param loginType
     * @return
     */
    AuthUser transformAuthUser(String username, String mobile, String loginType);

}
