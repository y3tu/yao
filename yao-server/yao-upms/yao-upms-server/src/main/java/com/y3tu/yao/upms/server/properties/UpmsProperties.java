package com.y3tu.yao.upms.server.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * upms配置
 *
 * @author y3tu
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:yao-upms.properties"})
@ConfigurationProperties(prefix = "yao.upms")
public class UpmsProperties {
    /**
     * 免认证 URI，多个值的话以逗号分隔
     */
    private String anonUrl;
}
