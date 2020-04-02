package com.y3tu.yao.common.annotation;

import com.y3tu.yao.common.configure.AuthExceptionConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用权限异常翻译处理
 *
 * @author y3tu
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AuthExceptionConfigure.class)
public @interface EnableAuthExceptionHandler {
}
