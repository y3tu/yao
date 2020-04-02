package com.y3tu.yao.common.constant;

/**
 * 端点常量
 *
 * @author y3tu
 */
public class EndpointConstant {

    public static final String ALL = "/**";

    public static final String OAUTH_ALL = "/oauth/**";

    public static final String OAUTH_AUTHORIZE = "/oauth/authorize";

    public static final String OAUTH_CHECK_TOKEN = "/oauth/check_token";

    public static final String OAUTH_CONFIRM_ACCESS = "/oauth/confirm_access";

    public static final String OAUTH_TOKEN = "/oauth/token";

    public static final String OAUTH_TOKEN_KEY = "/oauth/token_key";

    public static final String OAUTH_ERROR = "/oauth/error";

    /**
     * 手机验证码登录的地址
     */
    public static final String MOBILE_TOKEN_URL = "/mobile/token";
}
