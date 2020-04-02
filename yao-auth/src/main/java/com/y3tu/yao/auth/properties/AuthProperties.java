package com.y3tu.yao.auth.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.TimeUnit;

/**
 * 鉴权服务配置
 *
 * @author y3tu
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:auth.properties"})
@ConfigurationProperties(prefix = "yao.auth")
public class AuthProperties {

    /**
     * 免认证访问路径
     */
    private String anonUrl;

    /**
     * JWT加签密钥
     */
    private String jwtAccessKey;
    /**
     * 是否使用 JWT令牌
     */
    private Boolean enableJwt;

    /**
     * 社交登录所使用的 Client
     */
    private String socialLoginClientId;

    /**
     * 是否可以重用刷新令牌
     */
    private boolean isReuseRefreshToken = true;

    /**
     * 是否支持刷新令牌
     */
    private boolean isSupportRefreshToken = true;

    /**
     * 令牌失效时间 单位秒
     */
    private int accessTokenValiditySeconds = (int) TimeUnit.MINUTES.toSeconds(60);

    /**
     * 刷新令牌失效时间 单位秒
     */
    private int refreshTokenValiditySeconds = (int) TimeUnit.HOURS.toSeconds(10);

    /**
     * 验证码配置
     */
    private ValidateCodeProperties code = new ValidateCodeProperties();

}
