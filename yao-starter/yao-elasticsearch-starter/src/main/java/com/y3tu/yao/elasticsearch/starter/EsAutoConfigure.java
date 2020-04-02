package com.y3tu.yao.elasticsearch.starter;

import cn.hutool.core.lang.Assert;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.elasticsearch.starter.builder.EsClientBuilder;
import com.y3tu.yao.elasticsearch.starter.template.EsTemplate;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * elasticsearch 自动配置
 *
 * @author y3tu
 */
@Configuration
public class EsAutoConfigure {

    @Value("${elasticsearch.nodes}")
    private List<String> nodes;

    @Value("${elasticsearch.schema}")
    private String schema;

    @Value("${elasticsearch.max-connect-total}")
    private Integer maxConnectTotal;

    @Value("${elasticsearch.max-connect-per-route}")
    private Integer maxConnectPerRoute;

    @Value("${elasticsearch.connection-request-timeout-millis}")
    private Integer connectionRequestTimeoutMillis;

    @Value("${elasticsearch.socket-timeout-millis}")
    private Integer socketTimeoutMillis;

    @Value("${elasticsearch.connect-timeout-millis}")
    private Integer connectTimeoutMillis;

    @Bean
    public RestHighLevelClient getRestHighLevelClient() {

        List<HttpHost> httpHosts = new ArrayList<>();

        for (String node : nodes) {
            try {
                String[] parts = StrUtil.split(node, ":");
                Assert.notNull(parts, "Must defined");
                Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                httpHosts.add(new HttpHost(parts[0], Integer.parseInt(parts[1]), schema));
            } catch (RuntimeException ex) {
                throw new IllegalStateException(
                        "Invalid ES nodes " + "property '" + node + "'", ex);
            }
        }

        return EsClientBuilder.build(httpHosts)
                .setConnectionRequestTimeoutMillis(connectionRequestTimeoutMillis)
                .setConnectTimeoutMillis(connectTimeoutMillis)
                .setSocketTimeoutMillis(socketTimeoutMillis)
                .setMaxConnectTotal(maxConnectTotal)
                .setMaxConnectPerRoute(maxConnectPerRoute)
                .create();
    }

    @Bean
    public EsTemplate esTemplate(){
        return new EsTemplate();
    }
}
