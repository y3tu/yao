package com.y3tu.yao.support.server.route.controller;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.yao.support.client.route.entity.RouteUser;
import com.y3tu.yao.support.client.route.service.RouteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author y3tu
 */
@RestController
@RequestMapping("route/auth/user")
public class RouteUserController {

    @Autowired
    private RouteUserService routeUserService;

    @PostMapping("page")
    public R page(Page<RouteUser> page) {
        page =  routeUserService.findRouteUserPage(page);
        return R.success(page);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin')")
    public R createRouteUser(RouteUser routeUser) {
        routeUserService.createRouteUser(routeUser);
        return R.success();
    }


}
