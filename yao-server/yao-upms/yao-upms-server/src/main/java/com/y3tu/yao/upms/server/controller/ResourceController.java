package com.y3tu.yao.upms.server.controller;

import com.y3tu.tool.core.pojo.TreeNode;
import com.y3tu.yao.common.constant.ServerConstant;
import com.y3tu.yao.log.client.annotation.ControllerLog;
import com.y3tu.yao.log.client.constant.ActionTypeEnum;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.yao.upms.client.entity.Resource;
import com.y3tu.yao.upms.client.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源表 前端控制器
 *
 * @author y3tu
 */
@RestController
@RequestMapping("/resource")
@ControllerLog(serverName = ServerConstant.UPMS_SERVER, moduleName = "资源管理")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 根据用户名获取用户资源菜单树路由和权限
     *
     * @param username
     * @return
     */
    @GetMapping("/getUserRouter/{username}")
    @ControllerLog(actionName = "根据用户名获取用户资源菜单树路由和权限",actionType = ActionTypeEnum.VIEW)
    public R getUserRouter(@NotBlank(message = "{required}") @PathVariable String username) {
        Map<String, Object> result = new HashMap<>();
        List<String> permissions = resourceService.getUserPermissions(username);
        List<TreeNode<Resource>> routes = resourceService.getUserResourceTree(username);
        result.put("routes", routes);
        result.put("permissions", permissions);
        return R.success(result);
    }

    @GetMapping("/getResourceTree")
    @ControllerLog(actionName = "获取所有的资源树",actionType = ActionTypeEnum.VIEW)
    public R getResourceTree() {
        return R.success(resourceService.getResourceTree());
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasAuthority('resource:create')")
    @ControllerLog(actionName = "新增资源", actionType = ActionTypeEnum.CREATE)
    public R save(@Valid Resource resource) {
        return resourceService.createResource(resource);
    }

    @PutMapping(value = "/update")
    @PreAuthorize("hasAuthority('resource:update')")
    @ControllerLog(actionName = "更新资源", actionType = ActionTypeEnum.UPDATE)
    public R update(@Valid Resource resource) {
        return resourceService.updateResource(resource);
    }


    @DeleteMapping(value = "/delete/{ids}")
    @PreAuthorize("hasAuthority('resource:delete')")
    @ControllerLog(actionName = "删除资源", actionType = ActionTypeEnum.DELETE)
    public R delByIds(@PathVariable String[] ids) {
        return resourceService.deleteResource(ids);
    }

}
