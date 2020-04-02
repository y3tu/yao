package com.y3tu.yao.gateway.filter;

import com.y3tu.tool.core.collection.ArrayUtil;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.yao.common.constant.Constant;
import com.y3tu.yao.common.constant.ServerConstant;
import com.y3tu.yao.gateway.feign.AuthenticationService;
import com.y3tu.tool.core.exception.ServerCallException;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.gateway.properties.YaoGatewayProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.*;

/**
 * 网关权限过滤器
 *
 * @author y3tu
 */
@Component
@Slf4j
@Order(0)
public class AccessGatewayFilter implements GlobalFilter {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private YaoGatewayProperties properties;

    private AntPathMatcher pathMatcher = new AntPathMatcher();


    /**
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        Mono<Void> checkForbidUriResult = checkForbidUri(request, response);
        if (checkForbidUriResult != null) {
            return checkForbidUriResult;
        }

        printLog(exchange);

        if (properties.isGatewayPermission()) {
            // 1.首先网关检查token是否有效，无效直接返回401，不调用签权服务
            // 2.调用签权服务器看是否对该请求有权限，有权限进入下一个filter，没有权限返回401
            String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String method = request.getMethodValue();
            String url = request.getPath().value();
            boolean hasPermission = false;
            //调用签权服务看用户是否有权限，若有权限进入下一个filter
            try {
                //调用签权服务看用户是否有权限，若有权限进入下一个filter
                hasPermission = authenticationService.hasPermission(authentication, url, method);
            } catch (Exception e) {
                String msg = e.getMessage();
                //判断是不是token过期失效
                if (msg.contains(String.valueOf(HttpStatus.UNAUTHORIZED.value()))) {
                    return makeResponse(response, HttpStatus.UNAUTHORIZED, R.error("未授权或token过期！"));
                }
                String serverName = ServerConstant.AUTH_SERVER;
                String str = StrUtil.format("{}服务调用{}异常,参数：authentication:{},url{},method{}", serverName, "hasPermission", authentication, url, method);
                throw new ServerCallException(str, e);
            }
            if (hasPermission) {
                ServerHttpRequest.Builder builder = request.mutate();
                //如果每个微服务需要做验证，则转发的请求都加上服务间认证token
                //如果只在网关做验证则不需要
                return chain.filter(exchange.mutate().request(builder.build()).build());
            } else {
                return makeResponse(response, HttpStatus.FORBIDDEN, R.error("当前操作没有权限"));
            }
        } else {
            byte[] token = Base64Utils.encode((Constant.GATEWAY_TOKEN_VALUE).getBytes());
            String[] headerValues = {new String(token)};
            ServerHttpRequest build = request.mutate().header(Constant.GATEWAY_TOKEN_HEADER, headerValues).build();
            ServerWebExchange newExchange = exchange.mutate().request(build).build();
            return chain.filter(newExchange);
        }

    }


    /**
     * 网关转发日志打印
     */
    private void printLog(ServerWebExchange exchange) {
        URI url = exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR);
        Route route = exchange.getAttribute(GATEWAY_ROUTE_ATTR);
        LinkedHashSet<URI> uris = exchange.getAttribute(GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        URI originUri = null;
        if (uris != null) {
            originUri = uris.stream().findFirst().orElse(null);
        }
        if (url != null && route != null && originUri != null) {
            log.info("转发请求：{}://{}{} --> 目标服务：{}，目标地址：{}://{}{}，转发时间：{}",
                    originUri.getScheme(), originUri.getAuthority(), originUri.getPath(),
                    route.getId(), url.getScheme(), url.getAuthority(), url.getPath(), LocalDateTime.now()
            );
        }
    }

    /**
     * 校验是否为不可访问Uri
     */
    private Mono<Void> checkForbidUri(ServerHttpRequest request, ServerHttpResponse response) {
        String uri = request.getPath().toString();
        boolean shouldForward = true;
        String forbidRequestUri = properties.getForbidRequestUri();
        String[] forbidRequestUris = StrUtil.split(forbidRequestUri, ",");
        if (forbidRequestUris != null && ArrayUtil.isNotEmpty(forbidRequestUris)) {
            for (String u : forbidRequestUris) {
                if (pathMatcher.match(u, uri)) {
                    shouldForward = false;
                }
            }
        }
        if (!shouldForward) {
            return makeResponse(response, HttpStatus.FORBIDDEN, R.error("该URI不允许外部访问"));
        }
        return null;
    }

    private Mono<Void> makeResponse(ServerHttpResponse response, HttpStatus httpStatus, R r) {
        response.setStatusCode(httpStatus);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JsonUtil.toJson(r).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }

}
