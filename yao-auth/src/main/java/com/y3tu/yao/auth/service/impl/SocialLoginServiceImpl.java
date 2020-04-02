package com.y3tu.yao.auth.service.impl;

import com.xkcoding.justauth.AuthRequestFactory;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.auth.entity.BindUser;
import com.y3tu.yao.auth.entity.UserConnection;
import com.y3tu.yao.auth.exception.SocialLoginException;
import com.y3tu.yao.auth.properties.AuthProperties;
import com.y3tu.yao.auth.service.AuthService;
import com.y3tu.yao.auth.service.SocialLoginService;
import com.y3tu.yao.auth.service.UserConnectionService;
import com.y3tu.yao.common.constant.Constant;
import com.y3tu.yao.common.constant.GrantTypeConstant;
import com.y3tu.yao.common.constant.LoginTypeConstant;
import com.y3tu.yao.common.constant.ParamsConstant;
import com.y3tu.yao.common.util.YaoUtil;
import com.y3tu.yao.upms.client.entity.User;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 第三方登录服务
 *
 * @author y3tu
 */
@Service
public class SocialLoginServiceImpl implements SocialLoginService {

    private static final String USERNAME = "username"           ;
    private static final String PASSWORD = "password";

    private static final String NOT_BIND = "not_bind";
    private static final String SOCIAL_LOGIN_SUCCESS = "social_login_success";

    @Autowired
    private AuthService authService;
    @Autowired
    private AuthRequestFactory factory;
    @Autowired
    private AuthProperties properties;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserConnectionService userConnectionService;
    @Autowired
    private ResourceOwnerPasswordTokenGranter granter;
    @Autowired
    private RedisClientDetailsService redisClientDetailsService;

    @Override
    public AuthRequest renderAuth(String oauthType) throws SocialLoginException {
        return factory.get(getAuthSource(oauthType));
    }

    @Override
    public R resolveBind(String oauthType, AuthCallback callback) throws SocialLoginException {
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        AuthResponse<?> response = authRequest.login(resolveAuthCallback(callback));
        if (response.ok()) {
           return R.success("data",response.getData());
        } else {
            throw new SocialLoginException(String.format("第三方登录失败，%s", response.getMsg()));
        }
    }

    @Override
    public R resolveLogin(String oauthType, AuthCallback callback) throws SocialLoginException {
        R r = new R();
        AuthRequest authRequest = factory.get(getAuthSource(oauthType));
        AuthResponse<?> response = authRequest.login(resolveAuthCallback(callback));

        if (response.ok()) {
            AuthUser authUser = (AuthUser) response.getData();
            UserConnection userConnection = userConnectionService.selectByCondition(authUser.getSource().toString(), authUser.getUuid());
            if (userConnection == null) {
                r.setStatus(R.Status.ERROR).setMessage(NOT_BIND).setData(authUser);
            } else {
                User user = authService.findUserByName(userConnection.getUserName());
                if (user == null) {
                    throw new SocialLoginException("系统中未找到与第三方账号对应的账户");
                }
                OAuth2AccessToken oAuth2AccessToken = getOAuth2AccessToken(user);
                Map data = new HashMap();
                data.put("token",oAuth2AccessToken);
                data.put("username",user.getUsername());
                r.setStatus(R.Status.SUCCESS).setMessage(SOCIAL_LOGIN_SUCCESS).setData(data);
            }
        } else {
            throw new SocialLoginException(String.format("第三方登录失败，%s", response.getMsg()));
        }
        return r;
    }

    @Override
    public OAuth2AccessToken bindLogin(BindUser bindUser, AuthUser authUser) throws SocialLoginException {
        User user = authService.findUserByName(bindUser.getBindUsername());
        if (user == null || !passwordEncoder.matches(bindUser.getBindPassword(), user.getPassword())) {
            throw new SocialLoginException("绑定系统账号失败，用户名或密码错误！");
        }
        this.createConnection(user, authUser);
        return this.getOAuth2AccessToken(user);
    }

    @Override
    public OAuth2AccessToken signLogin(BindUser bindUser, AuthUser authUser) throws SocialLoginException {
        User user = authService.findUserByName(bindUser.getBindUsername());
        if (user != null) {
            throw new SocialLoginException("该用户名已存在！");
        }
        String encryptPassword = passwordEncoder.encode(bindUser.getBindPassword());
        User registerUser = authService.registerUser(bindUser.getBindUsername(), encryptPassword);
        this.createConnection(registerUser, authUser);
        return this.getOAuth2AccessToken(registerUser);
    }

    @Override
    public void bind(BindUser bindUser, AuthUser authUser) throws SocialLoginException {
        String username = bindUser.getBindUsername();
        if (isCurrentUser(username)) {
            UserConnection userConnection = userConnectionService.selectByCondition(authUser.getSource().toString(), authUser.getUuid());
            if (userConnection != null) {
                throw new SocialLoginException("绑定失败，该第三方账号已绑定" + userConnection.getUserName() + "系统账户");
            }
            User user = new User();
            user.setUsername(username);
            this.createConnection(user, authUser);
        } else {
            throw new SocialLoginException("绑定失败，您无权绑定别人的账号");
        }
    }

    @Override
    public void unbind(BindUser bindUser, String oauthType) throws SocialLoginException {
        String username = bindUser.getBindUsername();
        if (isCurrentUser(username)) {
            this.userConnectionService.deleteByCondition(username, oauthType);
        } else {
            throw new SocialLoginException("解绑失败，您无权解绑别人的账号");
        }
    }

    @Override
    public List<UserConnection> findUserConnections(String username) {
        return this.userConnectionService.selectByCondition(username);
    }

    private void createConnection(User user, AuthUser authUser) {
        UserConnection userConnection = new UserConnection();
        userConnection.setUserName(user.getUsername());
        userConnection.setProviderName(authUser.getSource().toString());
        userConnection.setProviderUserId(authUser.getUuid());
        userConnection.setProviderUserName(authUser.getUsername());
        userConnection.setImageUrl(authUser.getAvatar());
        userConnection.setNickName(authUser.getNickname());
        userConnection.setLocation(authUser.getLocation());
        this.userConnectionService.createUserConnection(userConnection);
    }


    private AuthCallback resolveAuthCallback(AuthCallback callback) {
        String state = callback.getState();
        String[] strings = StrUtil.split(state, "::");
        if (strings.length == 3) {
            callback.setState(strings[0] + "::" + strings[1]);
        }
        return callback;
    }

    private AuthSource getAuthSource(String type) throws SocialLoginException {
        if (StrUtil.isNotBlank(type)) {
            return AuthSource.valueOf(type.toUpperCase());
        } else {
            throw new SocialLoginException(String.format("暂不支持%s第三方登录", type));
        }
    }

    private boolean isCurrentUser(String username) {
        String currentUsername = YaoUtil.getCurrentUsername();
        return StrUtil.equalsIgnoreCase(username, currentUsername);
    }

    private OAuth2AccessToken getOAuth2AccessToken(User user) throws SocialLoginException {
        final HttpServletRequest httpServletRequest = YaoUtil.getHttpServletRequest();
        //设置为第三方登录
        httpServletRequest.setAttribute(ParamsConstant.LOGIN_TYPE, LoginTypeConstant.SOCIAL_LOGIN);
        String socialLoginClientId = properties.getSocialLoginClientId();
        ClientDetails clientDetails = null;
        try {
            clientDetails = redisClientDetailsService.loadClientByClientId(socialLoginClientId);
        } catch (Exception e) {
            throw new SocialLoginException("获取第三方登录可用的Client失败");
        }
        if (clientDetails == null) {
            throw new SocialLoginException("未找到第三方登录可用的Client");
        }
        Map<String, String> requestParameters = new HashMap<>(5);
        requestParameters.put(ParamsConstant.GRANT_TYPE, GrantTypeConstant.PASSWORD);
        requestParameters.put(USERNAME, user.getUsername());
        //第三方登录系统自动给出默认密码
        requestParameters.put(PASSWORD, Constant.SOCIAL_LOGIN_PASSWORD);

        String grantTypes = String.join(",", clientDetails.getAuthorizedGrantTypes());
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientDetails.getClientId(), clientDetails.getScope(), grantTypes);
        return granter.grant(GrantTypeConstant.PASSWORD, tokenRequest);
    }
}
