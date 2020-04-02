package com.y3tu.yao.auth.mobile;

import com.y3tu.yao.common.entity.AuthUser;
import com.y3tu.yao.auth.service.AuthService;
import com.y3tu.yao.common.constant.Constant;
import com.y3tu.yao.common.constant.LoginTypeConstant;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


/**
 * 手机验证码登录逻辑实现
 *
 * @author y3tu
 */
@Slf4j
@Data
public class MobileAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AuthService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        MobileAuthenticationToken mobileAuthenticationToken = (MobileAuthenticationToken) authentication;
        String mobile = mobileAuthenticationToken.getPrincipal().toString();
        Object realCode = redisTemplate.opsForValue().get(Constant.REDIS_MOBILE_CODE_PREFIX + mobile);
        String inputCode = authentication.getCredentials().toString();
        // 判断手机的验证码是否存在
        if (realCode == null) {
            log.error("登录失败，当前手机号验证码不存在或者已经过期");
            throw new BadCredentialsException("登录失败，验证码不存在");
        }
        // 判断是否验证码跟redis中存的验证码是否正确
        if (!inputCode.equalsIgnoreCase(realCode.toString())) {
            log.debug("登录失败，您输入的验证码不正确");
            throw new BadCredentialsException("登录失败，验证码不正确");
        }

        AuthUser authUser = authService.transformAuthUser("",mobile, LoginTypeConstant.MOBILE_LOGIN);
        // 重新构造token  登录成功
        MobileAuthenticationToken authenticationToken = new MobileAuthenticationToken(authUser, inputCode, authUser.getAuthorities());
        authenticationToken.setDetails(mobileAuthenticationToken.getDetails());
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return MobileAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
