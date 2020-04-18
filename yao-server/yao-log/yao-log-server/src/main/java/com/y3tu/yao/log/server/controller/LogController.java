package com.y3tu.yao.log.server.controller;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.y3tu.tool.web.excel.ExcelUtil;
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

import javax.servlet.http.HttpServletResponse;

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
    public R page(@RequestBody Page page) {
        return R.success(service.page(page));
    }

    /**
     * 导出日志数据
     * 因为日志数据比较大，采用分页查询并写入excel 防止运行时oom
     *
     * @param page     分页信息
     * @param response 响应信息
     */
    @ControllerLog(actionName = "导出用户操作日志信息", serverName = ServerConstant.LOG_SERVER, moduleName = MODULE_NAME)
    @PostMapping("/export")
    public void export(@RequestBody Page page, HttpServletResponse response) throws Exception {
        Page pageResult = service.page(page);
        response = ExcelUtil.decorateResponse("操作日志",ExcelTypeEnum.XLSX,response);
        ExcelWriter excelWriter = ExcelUtil.write(response.getOutputStream()).excelType(ExcelTypeEnum.XLSX).build();
        ExcelUtil.pageWrite(excelWriter, "日志", 1, pageResult.getTotal(), (currentPage, pageSize) -> {
            page.setCurrent(currentPage);
            page.setSize(pageSize);
            return service.page(page).getRecords();

        });

    }

}