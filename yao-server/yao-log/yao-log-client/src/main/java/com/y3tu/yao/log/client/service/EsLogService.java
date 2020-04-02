package com.y3tu.yao.log.client.service;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.yao.log.client.dto.SearchDto;
import com.y3tu.yao.log.client.entity.Log;

import java.io.IOException;


/**
 * elasticsearch 操作日志服务
 *
 * @author y3tu
 */
public interface EsLogService {

    /**
     * 添加日志
     *
     * @param esLog
     * @return
     */
    void saveLog(Log esLog) throws IOException;

    /**
     * 通过id删除日志
     *
     * @param id
     */
    void deleteLog(String id);

    /**
     * 删除全部日志
     */
    void deleteAll();

    /**
     * 分页搜索获取日志
     *
     * @param actionType 操作类型
     * @param key
     * @param searchDto
     * @return
     */
    Page<Log> findByCondition(Integer actionType, String key, SearchDto searchDto );
}
