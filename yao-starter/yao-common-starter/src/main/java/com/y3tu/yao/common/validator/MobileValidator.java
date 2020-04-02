package com.y3tu.yao.common.validator;

import cn.hutool.core.lang.Validator;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.common.annotation.IsMobile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验是否是手机号实现
 *
 * @author y3tu
 */
public class MobileValidator implements ConstraintValidator<IsMobile, String> {

    @Override
    public void initialize(IsMobile isMobile) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (StrUtil.isBlank(s)) {
                return true;
            } else {
                return Validator.isMobile(s);
            }
        } catch (Exception e) {
            return false;
        }
    }
}
