package com.y3tu.yao.upms.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.y3tu.yao.common.constant.ServerConstant;
import com.y3tu.yao.log.client.annotation.ControllerLog;
import com.y3tu.yao.log.client.constant.ActionTypeEnum;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.pojo.TreeNode;
import com.y3tu.tool.core.util.TreeUtil;
import com.y3tu.yao.upms.client.entity.Department;
import com.y3tu.yao.upms.client.service.DepartmentService;
import com.y3tu.yao.upms.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门Controller
 *
 * @author y3tu
 */
@RestController
@RequestMapping("/department")
@ControllerLog(serverName = ServerConstant.UPMS_SERVER, moduleName = "部门管理")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private UserService userService;

    @GetMapping("/tree")
    @ControllerLog(actionName = "获取部门树", actionType = ActionTypeEnum.VIEW)
    public R getDepartmentTree() {
        List<Department> list = departmentService.list(
                new LambdaQueryWrapper<Department>().orderByAsc(Department::getOrderNum));
        List<TreeNode<Department>> treeNodeList = list.stream().map(department -> {
            TreeNode<Department> treeNode = new TreeNode<>(department.getDepartmentId(), department.getDepartmentName(), department.getParentId(), department);
            return treeNode;
        }).collect(Collectors.toList());
        return R.success(TreeUtil.buildList(treeNodeList, Department.TREE_ROOT));
    }


    @PostMapping("create")
    @PreAuthorize("hasAuthority('department:create')")
    @ControllerLog(actionName = "新增部门", actionType = ActionTypeEnum.CREATE)
    public R create(@Valid Department department) {
        departmentService.createDepartment(department);
        return R.success();
    }

    @PutMapping("update")
    @PreAuthorize("hasAuthority('department:update')")
    @ControllerLog(actionName = "更新部门", actionType = ActionTypeEnum.UPDATE)
    public R update(@Valid Department department) {
        departmentService.updateDepartment(department);
        return R.success();
    }

    @DeleteMapping(value = "delByIds/{ids}")
    @PreAuthorize("hasAuthority('department:delete')")
    @ControllerLog(actionName = "删除部门", actionType = ActionTypeEnum.DELETE)
    public R delByIds(@NotBlank(message = "{required}") @PathVariable String[] ids) {
        departmentService.deleteDepartments(ids);
        return R.success();
    }

}
