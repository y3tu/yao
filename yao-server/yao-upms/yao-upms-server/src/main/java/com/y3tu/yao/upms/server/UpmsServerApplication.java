package com.y3tu.yao.upms.server;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.y3tu.tool.web.annotation.EnableDefaultExceptionAdvice;
import com.y3tu.tool.web.annotation.EnableToolWeb;
import com.y3tu.yao.common.annotation.YaoCloudApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * 启动类
 *
 * @author y3tu
 */
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@YaoCloudApplication
@EnableDiscoveryClient
@EnableToolWeb
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages="com.y3tu.yao.upms")
public class UpmsServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(UpmsServerApplication.class, args);
    }
}
