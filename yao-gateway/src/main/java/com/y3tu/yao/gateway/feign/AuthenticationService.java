package com.y3tu.yao.gateway.feign;

import com.y3tu.yao.common.constant.ServerConstant;
import feign.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

/**
 * 调用资源服务器获取请求权限
 *
 * @author y3tu
 */
@FeignClient(name = ServerConstant.AUTH_SERVER, configuration = AuthenticationService.UserFeignConfig.class)
public interface AuthenticationService {

    /**
     * 调用签权服务，判断用户是否有权限
     *
     * @param authentication
     * @param url
     * @param method
     * @return <pre>
     * </pre>
     */
    @PostMapping(value = "/hasPermission")
    boolean hasPermission(@RequestHeader(HttpHeaders.AUTHORIZATION) String authentication, @RequestParam("url") String url, @RequestParam("method") String method);

    class UserFeignConfig {
        @Bean
        public Logger.Level logger() {
            return Logger.Level.FULL;
        }
    }

}
