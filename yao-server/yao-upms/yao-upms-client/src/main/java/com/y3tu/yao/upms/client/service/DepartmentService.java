package com.y3tu.yao.upms.client.service;

import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.upms.client.entity.Department;

/**
 * 部门服务
 * @author y3tu
 */
public interface DepartmentService extends BaseService<Department> {

    void createDepartment(Department department);

    void updateDepartment(Department department);

    void deleteDepartments(String[] departmentIds);
}

