package com.y3tu.yao.support.client.route.service;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.support.client.route.entity.RouteUser;

public interface RouteUserService extends BaseService<RouteUser> {

    Page<RouteUser> findRouteUserPage(Page<RouteUser> page);

    void createRouteUser(RouteUser routeUser);

    void updateRouteUser(RouteUser routeUser);

    void deleteRouteUser(String[] ids);

}
