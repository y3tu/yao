package com.y3tu.yao.gateway.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * 网关配置
 *
 * @author y3tu
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:gateway.properties"})
@ConfigurationProperties(prefix = "yao.gateway")
public class YaoGatewayProperties {

    /**
     * 是否需要在网关上做权限认证 默认不需要
     * 如果在网关上做认证，那么需要网关访问资源服务获取此请求是否具有权限
     * 如果不在网关上做认证，那么直接转发请求到具体服务上做权限认证
     */
    private boolean gatewayPermission = false;

    /**
     * 禁止外部访问的 URI，多个值的话以逗号分隔
     */
    private String forbidRequestUri;
}
