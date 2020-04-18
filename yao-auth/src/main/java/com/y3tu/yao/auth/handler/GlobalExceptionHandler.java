package com.y3tu.yao.auth.handler;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.yao.auth.exception.SocialLoginException;
import com.y3tu.yao.auth.exception.ValidateCodeException;
import com.y3tu.yao.common.handler.BaseExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 统一的Controller异常处理
 *
 * @author y3tu
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler extends BaseExceptionHandler {

    @Override
    @ExceptionHandler(value = ValidateCodeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }

    @ExceptionHandler(value = SocialLoginException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R handleException(SocialLoginException e) {
        log.error(e.getMessage(), e);
        return R.error(e.getMessage());
    }
}