package com.y3tu.yao.common.selector;

import com.y3tu.yao.common.configure.OAuth2FeignConfigure;
import com.y3tu.yao.common.configure.AuthExceptionConfigure;
import com.y3tu.yao.common.configure.ServerProtectConfigure;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import javax.annotation.Nonnull;

/**
 * 加载配置
 *
 * @author y3tu
 */
public class CloudApplicationSelector implements ImportSelector {

    @Override
    @SuppressWarnings("all")
    public String[] selectImports(@Nonnull AnnotationMetadata annotationMetadata) {
        return new String[]{
                AuthExceptionConfigure.class.getName(),
                OAuth2FeignConfigure.class.getName(),
                ServerProtectConfigure.class.getName()
        };
    }
}
