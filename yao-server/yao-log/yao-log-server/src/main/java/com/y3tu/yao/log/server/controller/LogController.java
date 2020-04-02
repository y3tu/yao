package com.y3tu.yao.log.server.controller;

import com.y3tu.yao.common.constant.ServerConstant;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.base.controller.BaseController;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.yao.log.client.annotation.ControllerLog;
import com.y3tu.yao.log.client.entity.Log;
import com.y3tu.yao.log.client.service.LogService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志Controller
 *
 * @author y3tu
 */
@RestController
@RequestMapping("/log")
public class LogController extends BaseController<LogService, Log> {

    private static final String MODULE_NAME = "系统日志模块";

    @ControllerLog(actionName = "查看用户操作日志信息", serverName = ServerConstant.LOG_SERVER, moduleName = MODULE_NAME)
    @PostMapping("/page")
    @Override
    public R page(@RequestBody Page Page) {
        return R.success(service.page(Page));
    }

}