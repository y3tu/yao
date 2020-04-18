package com.y3tu.yao.upms.server.controller;

import com.alibaba.excel.support.ExcelTypeEnum;
import com.y3tu.tool.web.excel.ExcelUtil;
import com.y3tu.yao.common.constant.ServerConstant;
import com.y3tu.yao.common.util.YaoUtil;
import com.y3tu.yao.log.client.annotation.ControllerLog;
import com.y3tu.yao.log.client.constant.ActionTypeEnum;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.yao.upms.client.entity.*;
import com.y3tu.yao.upms.client.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.*;

/**
 * 用户表 前端控制器
 *
 * @author y3tu
 */
@RestController
@RequestMapping("/user")
@Slf4j
@ControllerLog(serverName = ServerConstant.UPMS_SERVER, moduleName = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private LoginLogService loginLogService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("success")
    @ControllerLog(actionName = "用户登录成功 更新用户登录时间", actionType = ActionTypeEnum.UPDATE)
    public void loginSuccess(HttpServletRequest request) {
        String currentUsername = YaoUtil.getCurrentUsername();
        // update last login time
        this.userService.updateLoginTime(currentUsername);
        // save login log
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(currentUsername);
        loginLog.setSystemBrowserInfo(request.getHeader("user-agent"));
        this.loginLogService.saveLoginLog(loginLog);
    }

    @GetMapping("index")
    @ControllerLog(actionName = "获取系统访问记录", actionType = ActionTypeEnum.VIEW)
    public R index() {
        Map<String, Object> data = new HashMap<>();
        // 获取系统访问记录
        Long totalVisitCount = loginLogService.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = loginLogService.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = loginLogService.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastTenVisitCount = loginLogService.findLastTenDaysVisitCount(null);
        data.put("lastTenVisitCount", lastTenVisitCount);
        User param = new User();
        param.setUsername(YaoUtil.getCurrentUsername());
        List<Map<String, Object>> lastTenUserVisitCount = loginLogService.findLastTenDaysVisitCount(param);
        data.put("lastTenUserVisitCount", lastTenUserVisitCount);
        return R.success(data);
    }

    @PostMapping("page")
    @PreAuthorize("hasAuthority('user:view')")
    @ControllerLog(actionName = "查询用户分页数据", actionType = ActionTypeEnum.VIEW)
    public R page(@RequestBody Page<User> page) {
        return R.success(userService.page(page));
    }


    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('user:create')")
    @ControllerLog(actionName = "新增用户", actionType = ActionTypeEnum.CREATE)
    public R create(User user) {
        return this.userService.createUser(user);
    }

    @PutMapping(value = "/update")
    @PreAuthorize("hasAuthority('user:update')")
    @ControllerLog(actionName = "编辑更新用户", actionType = ActionTypeEnum.UPDATE)
    public R update(User user) {
        return this.userService.updateUser(user);
    }

    @DeleteMapping(value = "/delByIds/{userIds}")
    @PreAuthorize("hasAuthority('user:delete')")
    @ControllerLog(actionName = "删除用户", actionType = ActionTypeEnum.DELETE)
    public R delByIds(@PathVariable String userIds) {
        String[] ids = StrUtil.split(userIds, StrUtil.COMMA);
        this.userService.deleteUsers(ids);
        return R.success();
    }

    @PutMapping("profile")
    @ControllerLog(actionName = "修改当前登录用户个人信息", actionType = ActionTypeEnum.UPDATE)
    public R updateProfile(@Valid User user) {
        return this.userService.updateProfile(user);
    }

    @PutMapping("avatar")
    @ControllerLog(actionName = "修改当前登录用户头像", actionType = ActionTypeEnum.UPDATE)
    public void updateAvatar(@NotBlank(message = "{required}") String avatar) {
        this.userService.updateAvatar(avatar);
    }

    @GetMapping("checkPassword")
    @ControllerLog(actionName = "检验密码是否正确", actionType = ActionTypeEnum.VIEW)
    public R checkPassword(@NotBlank(message = "{required}") String password) {
        String currentUsername = YaoUtil.getCurrentUsername();
        User user = userService.findUserByUsername(currentUsername);
        boolean check = user != null && passwordEncoder.matches(password, user.getPassword());
        return R.success(check);
    }

    @GetMapping("checkUsername/{username}")
    @ControllerLog(actionName = "检验用户名是否唯一", actionType = ActionTypeEnum.VIEW)
    public R checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return R.success(this.userService.findUserByUsername(username) == null);
    }

    @GetMapping("checkMobile/{mobile}")
    @ControllerLog(actionName = "检验手机号是否唯一", actionType = ActionTypeEnum.VIEW)
    public R checkMobile(@NotBlank(message = "{required}") @PathVariable String mobile) {
        return R.success(this.userService.findUserByMobile(mobile) == null);
    }

    @PutMapping("password")
    @ControllerLog(actionName = "修改当前登录用户密码", actionType = ActionTypeEnum.UPDATE)
    public void updatePassword(@NotBlank(message = "{required}") String password) {
        userService.updatePassword(password);
    }

    @PutMapping("password/reset")
    @PreAuthorize("hasAuthority('user:reset')")
    @ControllerLog(actionName = "重置用户密码", actionType = ActionTypeEnum.UPDATE)
    public void resetPassword(@NotBlank(message = "{required}") String usernames) {
        String[] usernameArr = usernames.split(StrUtil.COMMA);
        this.userService.resetPassword(usernameArr);
    }

    @PostMapping(value = "/register")
    public R register(@ModelAttribute User u,
                      @RequestParam String verify,
                      @RequestParam String captchaId) {
        return null;
    }

    /**
     * 导出全量用户数据
     *
     * @param response
     * @throws IOException
     */
    @PostMapping(value = "/export")
    public void export(HttpServletResponse response) throws Exception {
        List<User> list = userService.list();
        ExcelUtil.downExcel("用户", "用户", list, User.class, ExcelTypeEnum.XLSX, response);
    }

}
