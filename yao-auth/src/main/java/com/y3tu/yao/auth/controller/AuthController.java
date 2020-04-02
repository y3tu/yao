package com.y3tu.yao.auth.controller;

import cn.hutool.core.util.RandomUtil;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.yao.auth.exception.ValidateCodeException;
import com.y3tu.yao.auth.service.AuthService;
import com.y3tu.yao.auth.service.ValidateCodeService;
import com.y3tu.yao.common.constant.Constant;
import com.y3tu.yao.common.enums.SmsMessageChannnelEnum;
import com.y3tu.yao.common.enums.SmsTemplateEnum;
import com.y3tu.yao.common.template.sms.SmsMessageTemplate;
import com.y3tu.yao.upms.client.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.concurrent.TimeUnit;

/**
 * 授权服务Controller
 *
 * @author y3tu
 */
@RestController
@Slf4j
public class AuthController {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AuthService authService;
    @Autowired
    private ValidateCodeService validateCodeService;
//    @Autowired
//    private AuthService authService;

    @DeleteMapping("/token/{token}")
    public R<Boolean> removeAccessToken(@PathVariable("token") String token) {
        return new R<>(consumerTokenServices.revokeToken(token));
    }

    /**
     * 发送图形验证码
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }

    /**
     * 判断用户是否有访问此url的权限 内部调用 直接返回true or false
     *
     * @param url
     * @param method
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/hasPermission")
    public boolean hasPermission(@RequestParam String url, @RequestParam String method, HttpServletRequest request) {
        boolean hasPermission = false;
        //authService.decide(new HttpServletRequestAuthWrapper(request, url, method));
        return hasPermission;
    }

    /**
     * 获取当前用户
     * @param principal
     * @return
     */
    @GetMapping("user")
    public Principal currentUser(Principal principal) {
        return principal;
    }

    /**
     * 发送手机验证码
     *
     * @param mobile
     * @return
     */
    @GetMapping("/mobile/{mobile}")
    public R sendMobileCode(@PathVariable("mobile") String mobile) throws ValidateCodeException{
        Object originCode = redisTemplate.opsForValue().get(Constant.REDIS_MOBILE_CODE_PREFIX + mobile);
        if (originCode != null) {
            throw new ValidateCodeException("验证码尚未失效:" + originCode);
        }
        User user = authService.findUserByMobile(mobile);
        if (user == null) {
            throw new ValidateCodeException("此手机号码未注册");
        }
        String code = RandomUtil.randomNumbers(4);
        String[] params = {code};
        SmsMessageTemplate smsMessageTemplate = new SmsMessageTemplate();
        smsMessageTemplate.setParams(params);
        smsMessageTemplate.setMobile(mobile);
        smsMessageTemplate.setSignName(SmsTemplateEnum.LOGIN_CODE.getSignName());
        smsMessageTemplate.setTemplate(SmsTemplateEnum.LOGIN_CODE.getTempalte());
        smsMessageTemplate.setChannel(SmsMessageChannnelEnum.TENCENT_CLOUD.getCode());

        // 发送消息处理中心，存储在消息队列，供真正的短信程序获取队列数据并发送短信
//        rabbitTemplate.convertAndSend(MqQueueNameConstant.MOBILE_CODE_QUEUE,smsMessageTemplate);
        // 存redis
        redisTemplate.opsForValue().set(Constant.REDIS_MOBILE_CODE_PREFIX + mobile, code, Constant.REDIS_MOBILE_CODE_EXPIRE, TimeUnit.SECONDS);
        return R.success(code);
    }
}
