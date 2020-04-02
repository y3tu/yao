package com.y3tu.yao.auth.service;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.yao.auth.entity.BindUser;
import com.y3tu.yao.auth.entity.UserConnection;
import com.y3tu.yao.auth.exception.SocialLoginException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.List;

/**
 * 第三方登录服务
 *
 * @author y3tu
 */
public interface SocialLoginService {

    /**
     * 解析第三方登录请求
     *
     * @param oauthType 第三方平台类型
     * @return AuthRequest
     * @throws SocialLoginException 异常
     */
    AuthRequest renderAuth(String oauthType) throws SocialLoginException;

    /**
     * 处理第三方登录（绑定页面）
     *
     * @param oauthType 第三方平台类型
     * @param callback  回调
     * @return R
     * @throws SocialLoginException 异常
     */
    R resolveBind(String oauthType, AuthCallback callback) throws SocialLoginException;


    /**
     * 处理第三方登录（登录页面）
     *
     * @param oauthType 第三方平台类型
     * @param callback  回调
     * @return R
     * @throws SocialLoginException 异常
     */
    R resolveLogin(String oauthType, AuthCallback callback) throws SocialLoginException;

    /**
     * 绑定并登录
     *
     * @param bindUser 绑定用户
     * @param authUser 第三方平台对象
     * @return OAuth2AccessToken 令牌对象
     * @throws SocialLoginException 异常
     */
    OAuth2AccessToken bindLogin(BindUser bindUser, AuthUser authUser) throws SocialLoginException;

    /**
     * 注册并登录
     *
     * @param bindUser 注册用户
     * @param authUser   第三方平台对象
     * @return OAuth2AccessToken 令牌对象
     * @throws SocialLoginException 异常
     */
    OAuth2AccessToken signLogin(BindUser bindUser, AuthUser authUser) throws SocialLoginException;

    /**
     * 绑定
     *
     * @param bindUser 绑定对象
     * @param authUser 第三方平台对象
     * @throws SocialLoginException 异常
     */
    void bind(BindUser bindUser, AuthUser authUser) throws SocialLoginException;

    /**
     * 解绑
     *
     * @param bindUser  绑定对象
     * @param oauthType 第三方平台对象
     * @throws SocialLoginException 异常
     */
    void unbind(BindUser bindUser, String oauthType) throws SocialLoginException;

    /**
     * 根据用户名获取绑定关系
     *
     * @param username 用户名
     * @return 绑定关系集合
     */
    List<UserConnection> findUserConnections(String username);

}
