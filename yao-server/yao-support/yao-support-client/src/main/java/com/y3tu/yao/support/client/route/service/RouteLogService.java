package com.y3tu.yao.support.client.route.service;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.support.client.route.entity.RouteLog;

public interface RouteLogService extends BaseService<RouteLog> {

    Page<RouteLog> findRouteLogPage(Page<RouteLog> page);

    void createRouteLog(RouteLog routeLog);

    void updateRouteLog(RouteLog routeLog);

    void deleteRouteLog(String[] ids);
}
