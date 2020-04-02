package com.y3tu.yao.log.client.annotation;

import com.y3tu.yao.log.client.constant.ActionTypeEnum;
import com.y3tu.yao.log.client.constant.SaveModeEnum;

import java.lang.annotation.*;


/**
 * @author y3tu
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerLog {

    /**
     * 服务名
     *
     * @return
     */
    String serverName() default "";

    /**
     * 模块名
     *
     * @return
     */
    String moduleName() default "";

    /**
     * 操作名
     *
     * @return
     */
    String actionName() default "";

    /**
     * 异常返回信息
     */
    String exceptionMessage() default "";

    /**
     * 操作类型
     *
     * @return
     */
    ActionTypeEnum actionType() default ActionTypeEnum.VIEW;

    /**
     * 保存日志方式 默认保存到数据库
     */
    SaveModeEnum saveMode() default SaveModeEnum.NONE;

}
