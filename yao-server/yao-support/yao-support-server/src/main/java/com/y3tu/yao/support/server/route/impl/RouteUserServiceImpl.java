package com.y3tu.yao.support.server.route.impl;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.support.client.route.entity.RouteUser;
import com.y3tu.yao.support.client.route.mapper.RouteUserMapper;
import com.y3tu.yao.support.client.route.service.RouteUserService;
import org.springframework.stereotype.Service;

@Service
public class RouteUserServiceImpl extends BaseServiceImpl<RouteUserMapper, RouteUser> implements RouteUserService {

    @Override
    public Page<RouteUser> findRouteUserPage(Page<RouteUser> page) {
        return null;
    }

    @Override
    public void createRouteUser(RouteUser routeUser) {

    }

    @Override
    public void updateRouteUser(RouteUser routeUser) {

    }

    @Override
    public void deleteRouteUser(String[] ids) {

    }

}
