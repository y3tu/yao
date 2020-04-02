package com.y3tu.yao.elasticsearch.starter.template;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class EsTemplate {

    @Autowired
    RestHighLevelClient client;

    /**
     * 根据QueryBuilder来查询全部的条数
     *
     * @param indexName 索引
     * @param typeName  类型
     * @param id        id
     * @return 条数
     * @throws IOException 异常
     */
    public SearchResponse idQuery(String[] indexName, String typeName, String id) throws IOException {
        SearchRequest searchRequest = new SearchRequest(filterIndexName(indexName));
        searchRequest.types(typeName);
        searchRequest.source().query(QueryBuilders.idsQuery().addIds(id));
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     * 根据QueryBuilder来查询全部的条数
     *
     * @param indexNameArray 索引
     * @param typeName       类型
     * @param builder        查询构建器
     * @return 条数
     * @throws IOException 异常
     */
    public Long getTotalRecords(String[] indexNameArray, String typeName, QueryBuilder builder) throws IOException {
        String[] indexArr = filterIndexName(indexNameArray);
        if (indexArr.length == 0){
            return 0L;
        }
        SearchRequest searchRequest = new SearchRequest(indexArr);
        searchRequest.types(typeName);
        searchRequest.source().query(builder);
        SearchResponse response = client.search(searchRequest);
        return response.getHits().getTotalHits();
    }

    /**
     * 查询全部
     *
     * @param indexName 索引
     * @param typeName  类型
     * @param builder   查询构建器
     * @return SearchResponse
     * @throws IOException 异常
     */
    public SearchResponse findAll(String indexName, String typeName, QueryBuilder builder) throws IOException {
        SearchRequest request = new SearchRequest(indexName);
        request.types(typeName);
        request.source().query(builder);
        return client.search(request, RequestOptions.DEFAULT);
    }


    /**
     * 分页
     *
     * @param indexNameArray 索引
     * @param typeName       类型
     * @param queryBuilder   参数
     * @param from           从第几条
     * @param size           页面条数
     * @param sortBuilder    排序
     * @return response
     * @throws IOException 异常
     */
    public SearchResponse page(String[] indexNameArray, String typeName, QueryBuilder queryBuilder, int from, int size, SortBuilder sortBuilder)
            throws IOException {
        String[] indexArr = filterIndexName(indexNameArray);
        if (indexArr.length == 0) {
            log.error("选择的时间区间中没有数据");
            return null;
        }
        SearchRequest request = new SearchRequest(indexArr).types(typeName);
        //构建搜寻器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //参数
        sourceBuilder.query(queryBuilder);
        //排序
        sourceBuilder.sort(sortBuilder);
        //分页
        sourceBuilder.from(from);
        sourceBuilder.size(size);

        request.source(sourceBuilder);
        return client.search(request, RequestOptions.DEFAULT);
    }


    /**
     * 批量插入数据
     *
     * @param indexName 索引
     * @param typeName  类型
     * @param valueMap  map
     * @return BulkResponse
     * @throws IOException 异常
     */
    public BulkResponse insert(String indexName, String typeName, Map<String, String> valueMap) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        Set<String> keySet = valueMap.keySet();
        for (String id : keySet) {
            IndexRequest request = new IndexRequest(indexName);
            request.type(typeName);
            request.index(indexName).id(id).source(valueMap.get(id), XContentType.JSON);
            bulkRequest.add(request);
        }
        return client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    /**
     * 更新- 根据id进行更新(插入时根据id进行覆盖)
     */
    public void update(String indexName, String typeName, String id, String jsonStr) throws IOException {
        insert(indexName, typeName, id, jsonStr);
    }

    /**
     * 批量更新
     */
    public void update(String indexName, String typeName, Map<String, String> valueMap) throws IOException {
        insert(indexName, typeName, valueMap);
    }

    /**
     * 根据id进行删除
     *
     * @param indexName 索引
     * @param typeName  类型
     * @param id        id
     * @throws IOException 异常
     */
    public void delete(String indexName, String typeName, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(indexName, typeName, id);
        client.delete(deleteRequest, RequestOptions.DEFAULT);
    }

    public SearchResponse selectAll(String[] indexName, String typeName) throws IOException {
        SearchRequest searchRequest = new SearchRequest(filterIndexName(indexName));
        searchRequest.types(typeName);
        searchRequest.source().query(QueryBuilders.boolQuery());
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    /**
     * Es文档数据插入(插入前会自动生成index)
     *
     * @param indexName 索引名称
     * @param typeName  类型名称
     * @param id        id
     * @param jsonStr   json数据
     * @return IndexResponse
     * @throws IOException 异常
     */
    public IndexResponse insert(String indexName, String typeName, String id, String jsonStr) throws IOException {
        IndexRequest indexRequest = new IndexRequest(indexName, typeName, id);
        indexRequest.source(jsonStr, XContentType.JSON);
        return client.index(indexRequest, RequestOptions.DEFAULT);
    }

    /**
     * 过滤不存在的indexName
     *
     * @param indexNameArray 索引数组
     * @return 索引数组
     * @throws IOException 异常
     */
    private String[] filterIndexName(String[] indexNameArray) throws IOException {
        List<String> resultList = new ArrayList<>();
        for (String indexName : indexNameArray) {
            GetIndexRequest request = new GetIndexRequest().indices(indexName);
            boolean exists = client.indices().exists(request);
            if (exists) {
                resultList.add(indexName);
            }
        }
        int size = resultList.size();
        return resultList.toArray(new String[size]);
    }
}
