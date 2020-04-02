package com.y3tu.yao.upms.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.tool.core.exception.ServerException;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.upms.client.entity.Resource;
import com.y3tu.yao.upms.client.entity.RoleResource;
import com.y3tu.yao.upms.client.mapper.ResourceMapper;
import com.y3tu.tool.core.pojo.TreeNode;
import com.y3tu.tool.core.util.TreeUtil;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.upms.client.service.ResourceService;
import com.y3tu.yao.upms.client.service.RoleResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 资源表 服务实现类
 *
 * @author y3tu
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ResourceServiceImpl extends BaseServiceImpl<ResourceMapper, Resource> implements ResourceService {

    @Autowired
    private RoleResourceService roleResourceService;

    @Override
    public List<String> getUserPermissions(String username) {
        return this.baseMapper.findUserPermission(username);
    }

    @Override
    public List<Resource> getUserResource(String username) {
        return this.baseMapper.findUserResource(username);
    }

    @Override
    public List<TreeNode<Resource>> getUserResourceTree(String username) {
        //1.首选获取所有角色的资源集合
        List<Resource> resources = this.baseMapper.findUserResource(username);
        //2.找出类型为菜单类型的 然后排序
        List<Resource> newResources = resources.stream()
                //3.筛选出菜单
                .filter(resource -> Resource.RESOURCE_TYPE_MENU == resource.getType()
                        ||Resource.RESOURCE_TYPE_CATALOG == resource.getType())
                //4.给菜单排序
                .sorted(Comparator.comparingInt(Resource::getOrderNum))
                .collect(Collectors.toList());
        //5.构建树
        List<TreeNode<Resource>> treeNodeList = newResources.stream().map(resource -> {
            TreeNode<Resource> treeNode = new TreeNode<>(resource.getResourceId(), resource.getResourceName(), resource.getParentId(), resource);
            return treeNode;
        }).collect(Collectors.toList());
        return TreeUtil.buildList(treeNodeList, Resource.TREE_ROOT + "");
    }

    @Override
    public List<Resource> getResourceByRoleCode(List<String> roleCodes) {
        List<Resource> resourceList = new ArrayList<>();
        roleCodes.forEach(roleCode -> {
            resourceList.addAll(this.baseMapper.findResourceByRoleCode(roleCode));
        });
        return resourceList;
    }

    @Override
    public List<Resource> getResourceByRoleCode(String roleCode) {
        return this.baseMapper.findResourceByRoleCode(roleCode);
    }

    @Override
    public List<TreeNode<Resource>> getResourceTree() {
        List<Resource> resources = this.list(new LambdaQueryWrapper<Resource>().orderByAsc(Resource::getOrderNum));
        List<TreeNode<Resource>> treeNodeList = resources.stream().map(resource -> {
            TreeNode<Resource> treeNode = new TreeNode<>(resource.getResourceId(), resource.getResourceName(), resource.getParentId(), resource);
            return treeNode;
        }).collect(Collectors.toList());
        return TreeUtil.buildList(treeNodeList, Resource.TREE_ROOT);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R createResource(Resource resource) {
        List<Resource> list = this.list(new QueryWrapper<Resource>().lambda().eq(Resource::getResourceName, resource.getResourceName()));
        if (list != null && list.size() > 0) {
            return R.warn("名称已存在");
        }
        resource.setCreateTime(DateUtil.date());
        resource.setModifyTime(DateUtil.date());
        if(StrUtil.isEmpty(resource.getParentId())){
            resource.setParentId(Resource.TREE_ROOT);
        }
        this.saveBySnowflakeId(resource);
        return R.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateResource(Resource resource) {
        Resource resourceOld = this.getById(resource.getResourceId());
        if (!resourceOld.getResourceName().equals(resource.getResourceName())) {
            List<Resource> list;
            list = this.list(new QueryWrapper<Resource>().lambda().eq(Resource::getResourceName, resource.getResourceName()));
            if (list != null && list.size() > 0) {
                return R.warn("名称已存在");
            }
        }
        resource.setModifyTime(DateUtil.date());
        this.updateById(resource);
        return R.success();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteResource(String[] resourceIds) {
        try {
            delete(Arrays.asList(resourceIds));
        }catch (ServerException e){
            return R.warn(e.getMessage());
        }
        return R.success();
    }

    private void delete(List<String> resourceIds) {
        LambdaQueryWrapper<RoleResource> queryRoleResourceWrapper = new LambdaQueryWrapper<>();
        queryRoleResourceWrapper.in(RoleResource::getResourceId,resourceIds);

        List<RoleResource> list = this.roleResourceService.list(queryRoleResourceWrapper);
        if (list != null && list.size() > 0) {
            throw new ServerException("删除失败，包含正被角色使用关联的菜单或权限");
        }
        removeByIds(resourceIds);
        LambdaQueryWrapper<Resource> queryResourceWrapper = new LambdaQueryWrapper<>();
        queryResourceWrapper.in(Resource::getParentId, resourceIds);
        List<Resource> resourceList = baseMapper.selectList(queryResourceWrapper);
        if (CollectionUtils.isNotEmpty(resourceList)) {
            List<String> resourceIdList = new ArrayList<>();
            resourceList.forEach(resource -> resourceIdList.add(String.valueOf(resource.getResourceId())));
            this.delete(resourceIdList);
        }
    }

}
