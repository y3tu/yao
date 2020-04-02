package com.y3tu.yao.upms.client.mapper;

import com.y3tu.tool.web.base.mapper.BaseMapper;
import com.y3tu.yao.upms.client.entity.User;

import java.util.List;

/**
 * 用户表 Mapper 接口
 *
 * @author y3tu
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户
     */
    User findUserByName(String username);

    /**
     * 根据手机号查询用户
     * @param mobile 手机号
     * @return 用户
     */
    User findUserByMobile(String mobile);

}