package com.y3tu.yao.upms.client.service;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.pojo.TreeNode;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.upms.client.entity.Resource;

import java.util.List;

/**
 * 资源表 服务类
 *
 * @author y3tu
 */
public interface ResourceService extends BaseService<Resource> {

    /**
     * 根据用户名获取用户权限信息
     * @param username
     * @return
     */
    List<String> getUserPermissions(String username);

    /**
     * 根据用户名获取资源
     * @param username
     * @return
     */
    List<Resource> getUserResource(String username);

    /**
     * 根据用户名称获取用户资源菜单树
     *
     * @param username
     * @return
     */
    List<TreeNode<Resource>> getUserResourceTree(String username);

    /**
     * 根据角色codes查询菜单列表
     *
     * @param roleCodes
     * @return
     */
    List<Resource> getResourceByRoleCode(List<String> roleCodes);

    /**
     * 根据角色code查询资源信息
     *
     * @param roleCode
     * @return
     */
    List<Resource> getResourceByRoleCode(String roleCode);

    /**
     * 查询所有的资源
     *
     * @return
     */
    List<TreeNode<Resource>> getResourceTree();

    /**
     * 创建资源
     * @param resource
     */
    R createResource(Resource resource);

    /**
     * 更新资源
     * @param resource
     * @return
     */
    R updateResource(Resource resource);
    /**
     * 删除资源以及子资源
     *
     * @param resourceIds
     * @return
     */
    R deleteResource(String[] resourceIds);


}
