package com.y3tu.yao.support.client.route.service;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.support.client.route.entity.RateLimitRule;

public interface RateLimitRuleService extends BaseService<RateLimitRule> {

    Page<RateLimitRule> findRateLimitRulePage(Page<RateLimitRule> page);

    void createRateLimitRule(RateLimitRule rateLimitRule);

    void updateRateLimitRule(RateLimitRule rateLimitRule);

    void deleteRateLimitRule(String[] ids);
}
