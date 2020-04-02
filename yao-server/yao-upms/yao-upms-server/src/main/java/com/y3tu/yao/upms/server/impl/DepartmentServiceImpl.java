package com.y3tu.yao.upms.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.y3tu.yao.upms.client.entity.Department;
import com.y3tu.yao.upms.client.mapper.DepartmentMapper;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.upms.client.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @author y3tu
 */
@Service("departmentService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DepartmentServiceImpl extends BaseServiceImpl<DepartmentMapper, Department> implements DepartmentService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDepartment(Department department) {
        if (department.getParentId() == null)
            department.setParentId(Department.TREE_ROOT);
        department.setCreateTime(new Date());
        this.saveBySnowflakeId(department);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDepartment(Department department) {
        if (department.getParentId() == null)
            department.setParentId(Department.TREE_ROOT);
        department.setModifyTime(new Date());
        this.baseMapper.updateById(department);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepartments(String[] departmentIds) {
        this.delete(Arrays.asList(departmentIds));
    }

    private void delete(List<String> departmentIds) {
        removeByIds(departmentIds);
        LambdaQueryWrapper<Department> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Department::getParentId, departmentIds);
        List<Department> departmentList = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(departmentList)) {
            List<String> departmentIdList = new ArrayList<>();
            departmentList.forEach(department -> departmentIdList.add(String.valueOf(department.getDepartmentId())));
            this.delete(departmentIdList);
        }
    }
}
