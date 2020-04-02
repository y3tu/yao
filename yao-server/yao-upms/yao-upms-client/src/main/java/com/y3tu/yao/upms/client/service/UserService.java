package com.y3tu.yao.upms.client.service;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.upms.client.entity.User;


/**
 * 用户表 服务类
 *
 * @author y3tu
 */
public interface UserService extends BaseService<User> {

    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 根据手机号查询用户
     * @param mobile
     * @return
     */
    User findUserByMobile(String mobile);
    /**
     * 更新用户登录时间
     *
     * @param username username
     */
    void updateLoginTime(String username);

    /**
     * 新增用户
     *
     * @param user user
     */
    R createUser(User user);

    /**
     * 修改用户
     *
     * @param user user
     */
    R updateUser(User user);

    /**
     * 删除用户
     *
     * @param userIds 用户 id数组
     */
    void deleteUsers(String[] userIds);

    /**
     * 更新个人信息
     *
     * @param user 个人信息
     */
    R updateProfile(User user);

    /**
     * 更新用户头像
     *
     * @param avatar 用户头像
     */
    void updateAvatar(String avatar);

    /**
     * 更新用户密码
     *
     * @param password 新密码
     */
    void updatePassword(String password);

    /**
     * 重置密码
     *
     * @param usernames 用户集合
     */
    void resetPassword(String[] usernames);


}
