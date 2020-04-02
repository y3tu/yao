package com.y3tu.yao.common.constant;

/**
 * 系统常量类
 *
 * @author y3tu
 */
public class Constant {

    /**
     * Gateway请求头TOKEN名称（不要有空格）
     */
    public static final String GATEWAY_TOKEN_HEADER = "GatewayToken";
    /**
     * Gateway请求头TOKEN值
     */
    public static final String GATEWAY_TOKEN_VALUE = "yao:gateway:123456";

    /**
     * 验证码 key前缀
     */
    public static final String CODE_PREFIX = "yao.captcha.";

    /**
     * OAUTH2 令牌类型 https://oauth.net/2/bearer-tokens/
     */
    public static final String OAUTH2_TOKEN_TYPE = "bearer";

    /**
     * 手机验证码redis存储前缀
     */
    public static final String REDIS_MOBILE_CODE_PREFIX = "yao-mobile-code-";
    /**
     * 手机验证码redis失效时间，单位秒
     */
    public static final Integer REDIS_MOBILE_CODE_EXPIRE = 60;

    /**
     * 项目的license
     */
    public static final String LICENSE = "made by y3tu";

    /**
     * jwt中 用户id的key
     */
    public static final String USER_ID = "userId";
    /**
     * jwt中 用户userName的key
     */
    public static final String USER_NAME = "userName";
    /**
     * jwt中 角色集合的key
     */
    public static final String AUTHORITIES = "authorities";

    /**
     * 注册默认角色编码
     */
    public static final String REGISTER_ROLE_CODE = "DEFAULT_ROLE";

    public static final String SOCIAL_LOGIN = "social_login";
    /**
     * 第三方登录密码
     */
    public static final String SOCIAL_LOGIN_PASSWORD = "yao_social_login_password";


}
