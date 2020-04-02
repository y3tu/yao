package com.y3tu.yao.upms.client.mapper;

import com.y3tu.tool.web.base.mapper.BaseMapper;
import com.y3tu.yao.upms.client.entity.Resource;

import java.util.List;

/**
 * 资源Mapper
 *
 * @author y3tu
 */
public interface ResourceMapper extends BaseMapper<Resource> {

    /**
     * 根据用户名查询资源集合
     * @param username
     * @return
     */
    List<Resource> findResourceByUsername(String username);
    /**
     * 根据角色code查询资源集合
     * @param roleCode
     * @return
     */
    List<Resource> findResourceByRoleCode(String roleCode);

    /**
     * 根据用户名获取用户权限
     * @param username
     * @return
     */
    List<String> findUserPermission(String username);

    /**
     * 根据用户名获取用户资源
     * @param username
     * @return
     */
    List<Resource> findUserResource(String username);

}