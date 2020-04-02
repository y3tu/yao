package com.y3tu.yao.auth.service;

import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.auth.entity.UserConnection;

import java.util.List;

/**
 * 用户绑定service
 *
 * @author y3tu
 */
public interface UserConnectionService extends BaseService<UserConnection> {

    /**
     * 根据条件查询关联关系
     *
     * @param providerName   平台名称
     * @param providerUserId 平台用户ID
     * @return 关联关系
     */
    UserConnection selectByCondition(String providerName, String providerUserId);

    /**
     * 根据条件查询关联关系
     *
     * @param username 用户名
     * @return 关联关系
     */
    List<UserConnection> selectByCondition(String username);

    /**
     * 新增
     *
     * @param userConnection userConnection
     */
    void createUserConnection(UserConnection userConnection);

    /**
     * 删除
     *
     * @param username     username 用户名
     * @param providerName providerName 平台名称
     */
    void deleteByCondition(String username, String providerName);

}
