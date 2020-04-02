package com.y3tu.yao.upms.client.service;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.upms.client.entity.Role;

/**
 * 角色表 服务类
 *
 * @author y3tu
 */
public interface RoleService extends BaseService<Role>{

    /**
     * 创建角色
     * @param role
     */
    void createRole(Role role);

    /**
     * 更新角色
     * @param role
     */
    void updateRole(Role role);

    /**
     * 根据roleId删除角色
     * @param roleIds
     * @return r
     */
    R deleteRole(String[] roleIds);

}
