package com.y3tu.yao.log.server.impl;

import com.y3tu.tool.core.util.IdUtil;
import com.y3tu.tool.core.util.JsonUtil;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.yao.elasticsearch.starter.template.EsTemplate;
import com.y3tu.yao.log.client.dto.SearchDto;
import com.y3tu.yao.log.client.entity.Log;
import com.y3tu.yao.log.client.service.EsLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * elasticsearch 操作日志服务实现
 *
 * @author y3tu
 */
@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class EsLogServiceImpl implements EsLogService {


    @Autowired
    EsTemplate esTemplate;

    @Override
    public void saveLog(Log esLog) throws IOException {
        String id = IdUtil.createSnowflake(1, 1).nextIdStr();
        esLog.setId(id);
        esTemplate.insert("yao-es-log", "_doc", id, JsonUtil.toJson(esLog));
    }

    @Override
    public void deleteLog(String id) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public Page<Log> findByCondition(Integer actionType, String key, SearchDto searchDto) {
        return null;
    }


}
