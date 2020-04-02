package com.y3tu.yao.upms.server.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.yao.common.constant.ServerConstant;
import com.y3tu.yao.common.util.YaoUtil;
import com.y3tu.yao.log.client.annotation.ControllerLog;
import com.y3tu.yao.log.client.constant.ActionTypeEnum;
import com.y3tu.yao.upms.client.entity.LoginLog;
import com.y3tu.yao.upms.client.service.LoginLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 登录日志Controller
 *
 * @author y3tu
 */
@Slf4j
@RestController
@RequestMapping("loginLog")
@ControllerLog(serverName = ServerConstant.UPMS_SERVER, moduleName = "登录日志")
public class LoginLogController {

    @Autowired
    private LoginLogService loginLogService;

    /**
     * 登录日志分页信息
     *
     * @param Page 分页信息
     * @return 分页数据
     */
    @GetMapping
    public R<Page<LoginLog>> page(Page<LoginLog> Page) {
        return R.success(loginLogService.findLoginLogPage(Page));
    }

    /**
     * 获取用户最近7天的登录日志
     *
     * @param username 用户名
     * @return 7天登录日志
     */
    @GetMapping("/{username}")
    public R getUserLastSevenLoginLogs(@NotBlank(message = "{required}") @PathVariable String username) {
        List<LoginLog> userLastSevenLoginLogs = this.loginLogService.findUserLastSevenLoginLogs(username);
        return R.success(userLastSevenLoginLogs);
    }

    @DeleteMapping("{ids}")
    @PreAuthorize("hasAuthority('loginlog:delete')")
    @ControllerLog(actionName = "删除登录日志", exceptionMessage = "删除登录日志失败")
    public void deleteLoginLog(@NotBlank(message = "{required}") @PathVariable String ids) {
        String[] loginLogIds = ids.split(StringPool.COMMA);
        this.loginLogService.deleteLoginLogByIds(loginLogIds);
    }

    @PostMapping("excel")
    @PreAuthorize("hasAuthority('loginlog:export')")
    @ControllerLog(actionName = "导出登录日志数据", exceptionMessage = "导出Excel失败")
    public void export(HttpServletResponse response) {
    }

    @GetMapping("currentUser")
    @ControllerLog(actionName = "获取当前登录用户7天的登录记录", actionType = ActionTypeEnum.VIEW)
    public R getUserLastSevenLoginLogs() {
        String currentUsername = YaoUtil.getCurrentUsername();
        List<LoginLog> userLastSevenLoginLogs = this.loginLogService.findUserLastSevenLoginLogs(currentUsername);
        return R.success(userLastSevenLoginLogs);
    }
}
