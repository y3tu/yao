package com.y3tu.yao.elasticsearch.starter.builder;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.List;

@Data
@Accessors(chain = true)
public class EsClientBuilder {

    private int connectTimeoutMillis = 1000;
    private int socketTimeoutMillis = 30000;
    private int connectionRequestTimeoutMillis = 500;
    private int maxConnectPerRoute = 10;
    private int maxConnectTotal = 30;

    private final List<HttpHost> httpHosts;


    private EsClientBuilder(List<HttpHost> httpHosts) {
        this.httpHosts = httpHosts;
    }

    public static EsClientBuilder build(List<HttpHost> httpHosts) {
        return new EsClientBuilder(httpHosts);
    }

    public RestHighLevelClient create() {

        HttpHost[] httpHostArr = httpHosts.toArray(new HttpHost[0]);
        RestClientBuilder builder = RestClient.builder(httpHostArr);

        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeoutMillis);
            requestConfigBuilder.setSocketTimeout(socketTimeoutMillis);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeoutMillis);
            return requestConfigBuilder;
        });

        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectTotal);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpClientBuilder;
        });

        return new RestHighLevelClient(builder);
    }
}
