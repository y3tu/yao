package com.y3tu.yao.upms.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.core.util.ObjectUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.common.util.YaoUtil;
import com.y3tu.yao.upms.client.entity.User;
import com.y3tu.yao.upms.client.entity.UserRole;
import com.y3tu.yao.upms.client.mapper.UserMapper;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.upms.client.service.RoleService;
import com.y3tu.yao.upms.client.service.UserRoleService;
import com.y3tu.yao.upms.client.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 用户表 服务实现类
 *
 * @author y3tu
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User findUserByUsername(String username) {
        return this.baseMapper.findUserByName(username);
    }

    @Override
    public User findUserByMobile(String mobile) {
        return this.baseMapper.findUserByMobile(mobile);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginTime(String username) {
        User user = new User();
        user.setLastLoginTime(new Date());
        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R createUser(User user) {
        // 创建用户
        if(ObjectUtil.isNotEmpty(this.findUserByUsername(user.getUsername()))){
            return R.warn("您不能使用当前用户名！此用户名已被使用");
        }
        if(ObjectUtil.isNotEmpty(this.findUserByMobile(user.getMobile()))){
            return R.warn("您不能使用当前手机号！此手机号已被使用");
        }
        user.setCreateTime(new Date());
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setPassword(passwordEncoder.encode(User.DEFAULT_PASSWORD));
        saveBySnowflakeId(user);
        // 保存用户角色
        String[] roles = user.getRoleId().split(StrUtil.COMMA);
        setUserRoles(user, roles);
        return R.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateUser(User user) {
        // 更新用户
        user.setPassword(null);
        //老记录
        User userOld = this.getById(user.getUserId());
        if(!userOld.getUsername().equals(user.getUsername())){
            if(ObjectUtil.isNotEmpty(this.findUserByUsername(user.getUsername()))){
                return R.warn("您不能使用当前用户名！此用户名已被使用");
            }
        }

        if(!userOld.getMobile().equals(user.getMobile())){
            if(ObjectUtil.isNotEmpty(this.findUserByMobile(user.getMobile()))){
                return R.warn("您不能使用当前手机号！此手机号已被使用");
            }
        }

        user.setCreateTime(null);
        user.setModifyTime(new Date());
        updateById(user);

        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));
        String[] roles = user.getRoleId().split(StrUtil.COMMA);
        setUserRoles(user, roles);
        return R.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(String[] userIds) {
        List<String> list = Arrays.asList(userIds);
        removeByIds(list);
        // 删除用户角色
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().in(UserRole::getUserId, list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateProfile(User user) {
        user.setPassword(null);
        user.setUsername(null);
        user.setStatus(null);
        if (user.getUserId().equals(YaoUtil.getCurrentUser().getUserId())) {
            updateById(user);
        } else {
            return R.warn("您无权修改别人的账号信息！");
        }
        return R.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(String avatar) {
        User user = new User();
        user.setAvatar(avatar);
        String currentUsername = YaoUtil.getCurrentUsername();
        this.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, currentUsername));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String password) {
        User user = new User();
        user.setPassword(passwordEncoder.encode(password));
        String currentUsername = YaoUtil.getCurrentUsername();
        this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, currentUsername));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String[] usernames) {
        User params = new User();
        params.setPassword(passwordEncoder.encode(User.DEFAULT_PASSWORD));

        List<String> list = Arrays.asList(usernames);
        this.baseMapper.update(params, new LambdaQueryWrapper<User>().in(User::getUsername, list));
    }

    private void setUserRoles(User user, String[] roles) {
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.stream(roles).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(roleId);
            userRoles.add(userRole);
        });
        userRoleService.saveBatchBySnowflakeId(userRoles, 1000);
    }
}
