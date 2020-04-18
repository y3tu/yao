package com.y3tu.yao.upms.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.y3tu.tool.core.collection.CollectionUtil;
import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.upms.client.entity.Role;
import com.y3tu.yao.upms.client.entity.RoleResource;
import com.y3tu.yao.upms.client.entity.UserRole;
import com.y3tu.yao.upms.client.mapper.RoleMapper;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.upms.client.service.RoleResourceService;
import com.y3tu.yao.upms.client.service.RoleService;
import com.y3tu.yao.upms.client.service.UserRoleService;
import com.y3tu.yao.upms.server.exception.UpmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色表 服务实现类
 *
 * @author y3tu
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends BaseServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleResourceService roleResourceService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createRole(Role role) {
        role.setCreateTime(DateUtil.date());
        role.setModifyTime(DateUtil.date());
        this.saveBySnowflakeId(role);
        setRoleResource(role, role.getResourceIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(Role role) {
        role.setModifyTime(DateUtil.date());
        this.updateById(role);
        roleResourceService.remove(new LambdaQueryWrapper<RoleResource>().eq(RoleResource::getRoleId, role.getRoleId()));
        setRoleResource(role, role.getResourceIds());
    }

    @Override
    @Transactional
    public R deleteRole(String[] roleIds) {
        for (String id : roleIds) {
            List<UserRole> list = userRoleService.list(new QueryWrapper<UserRole>().eq("role_id", id));
            if (list != null && list.size() > 0) {
                return R.warn("删除失败，包含正被用户使用关联的角色");
            }
        }
        for (String id : roleIds) {
            this.removeById(id);
            //删除关联菜单权限
            roleResourceService.remove(new QueryWrapper<RoleResource>().eq("role_id", id));
        }
        this.removeByIds(CollectionUtil.toList(roleIds));
        return R.success();
    }

    private void setRoleResource(Role role, String resourceIds) {
        List<RoleResource> roleResourceList = new ArrayList<>();
        String[] resourceIdArr = StrUtil.split(resourceIds, ",");
        Arrays.stream(resourceIdArr).forEach(resourceId -> {
            RoleResource roleResource = new RoleResource();
            if (StrUtil.isNotEmpty(resourceId)) {
                roleResource.setRoleId(role.getRoleId());
                roleResource.setResourceId(resourceId);
                roleResourceList.add(roleResource);
            }
        });
        if (roleResourceList.size() < 1) {
            throw new UpmsException("角色至少需要一个资源权限");
        }
        this.roleResourceService.saveBatchBySnowflakeId(roleResourceList, 1000);
    }

}
