package com.y3tu.yao.auth.service.impl;

import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.auth.exception.ValidateCodeException;
import com.y3tu.yao.auth.properties.AuthProperties;
import com.y3tu.yao.auth.properties.ValidateCodeProperties;
import com.y3tu.yao.auth.service.ValidateCodeService;
import com.y3tu.yao.common.constant.Constant;
import com.y3tu.yao.common.constant.ImageTypeConstant;
import com.y3tu.yao.common.constant.ParamsConstant;
import com.y3tu.yao.redis.starter.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码服务
 *
 * @author y3tu
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private AuthProperties properties;

    @Override
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        String key = request.getParameter(ParamsConstant.VALIDATE_CODE_KEY);
        if (StrUtil.isBlank(key)) {
            throw new ValidateCodeException("验证码key不能为空");
        }
        ValidateCodeProperties code = properties.getCode();
        setHeader(response, code.getType());
        Captcha captcha = createCaptcha(code);
        redisService.set(Constant.CODE_PREFIX + key, captcha.text().toLowerCase(), code.getTime());
        captcha.out(response.getOutputStream());
    }

    @Override
    public void check(String key, String value) throws ValidateCodeException {
        Object codeInRedis = redisService.get(Constant.CODE_PREFIX + key);
        if (StrUtil.isBlank(value)) {
            throw new ValidateCodeException("请输入验证码");
        }
        if (codeInRedis == null) {
            throw new ValidateCodeException("验证码已过期");
        }
        if (!StrUtil.equalsIgnoreCase(value, String.valueOf(codeInRedis))) {
            throw new ValidateCodeException("验证码不正确");
        }
    }

    private Captcha createCaptcha(ValidateCodeProperties code) {
        Captcha captcha = null;
        if (StrUtil.equalsIgnoreCase(code.getType(), ImageTypeConstant.GIF)) {
            captcha = new GifCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        } else {
            captcha = new SpecCaptcha(code.getWidth(), code.getHeight(), code.getLength());
        }
        captcha.setCharType(code.getCharType());
        return captcha;
    }

    private void setHeader(HttpServletResponse response, String type) {
        if (StrUtil.equalsIgnoreCase(type, ImageTypeConstant.GIF)) {
            response.setContentType(MediaType.IMAGE_GIF_VALUE);
        } else {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
        }
        response.setHeader(HttpHeaders.PRAGMA, "No-cache");
        response.setHeader(HttpHeaders.CACHE_CONTROL, "No-cache");
        response.setDateHeader(HttpHeaders.EXPIRES, 0L);
    }
}
