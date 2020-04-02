package com.y3tu.yao.support.client.route.service;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.support.client.route.entity.RateLimitLog;

public interface RateLimitLogService extends BaseService<RateLimitLog> {

    Page<RateLimitLog> findRateLimitLogPage(Page<RateLimitLog> page);

    void createRateLimitLog(RateLimitLog rateLimitLog);

    void updateRateLimitLog(RateLimitLog rateLimitLog);

    void deleteRateLimitLog(String[] ids);
}
