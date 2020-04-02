package com.y3tu.yao.upms.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.common.constant.ServerConstant;
import com.y3tu.yao.log.client.annotation.ControllerLog;
import com.y3tu.yao.log.client.constant.ActionTypeEnum;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.yao.upms.client.entity.Role;
import com.y3tu.yao.upms.client.service.ResourceService;
import com.y3tu.yao.upms.client.service.RoleResourceService;
import com.y3tu.yao.upms.client.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * 角色表 前端控制器
 *
 * @author y3tu
 */
@RestController
@RequestMapping("/role")
@ControllerLog(serverName = ServerConstant.UPMS_SERVER, moduleName = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private ResourceService resourceService;

    @PostMapping("/page")
    @PreAuthorize("hasAuthority('role:view')")
    @ControllerLog(actionName = "查询角色分页数据", actionType = ActionTypeEnum.VIEW)
    public R page(@RequestBody Page<Role> page) {
        return R.success(roleService.page(page));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('role:create')")
    @ControllerLog(actionName = "新增角色", actionType = ActionTypeEnum.CREATE)
    public R create(Role role) {
        this.roleService.createRole(role);
        return R.success();
    }

    @PutMapping("/update")
    @PreAuthorize("hasAuthority('role:update')")
    @ControllerLog(actionName = "更新角色", actionType = ActionTypeEnum.UPDATE)
    public R update(Role role) {
        roleService.updateRole(role);
        return R.success();
    }

    @DeleteMapping(value = "/delete/{ids}")
    @PreAuthorize("hasAuthority('role:delete')")
    @ControllerLog(actionName = "删除角色", actionType = ActionTypeEnum.DELETE)
    public R delByIds(@PathVariable String[] ids) {
        return roleService.deleteRole(ids);
    }

    @GetMapping("options")
    @ControllerLog(actionName = "获取角色所有选项", actionType = ActionTypeEnum.VIEW)
    public R roles() {
        List<Role> roleList = roleService.list();
        return R.success(roleList);
    }

    @GetMapping("check/{roleName}")
    @ControllerLog(actionName = "检验角色名是否可用")
    public R checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName) {
        Role result = this.roleService.getOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, roleName));
        return R.success(result == null);
    }
}
