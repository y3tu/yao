package com.y3tu.yao.auth.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.y3tu.tool.core.util.IdUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.common.entity.AuthUser;
import com.y3tu.yao.auth.service.AuthService;
import com.y3tu.yao.common.constant.Constant;
import com.y3tu.yao.common.constant.LoginTypeConstant;
import com.y3tu.yao.upms.client.entity.Resource;
import com.y3tu.yao.upms.client.entity.Role;
import com.y3tu.yao.upms.client.entity.User;
import com.y3tu.yao.upms.client.entity.UserRole;
import com.y3tu.yao.upms.client.mapper.ResourceMapper;
import com.y3tu.yao.upms.client.mapper.RoleMapper;
import com.y3tu.yao.upms.client.mapper.UserMapper;
import com.y3tu.yao.upms.client.mapper.UserRoleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务
 *
 * @author y3tu
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ResourceMapper resourceMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public boolean decide(HttpServletRequest authRequest) {
        return false;
    }

    @Override
    @DS("upms")
    public User findUserByName(String username) {
        return userMapper.findUserByName(username);
    }

    @Override
    @DS("upms")
    public User findUserByMobile(String mobile) {
        return userMapper.findUserByMobile(mobile);
    }

    @Override
    @DS("upms")
    public String findUserPermissions(String username) {
        List<Resource> userPermissions = resourceMapper.findResourceByUsername(username);
        return userPermissions.stream().map(Resource::getPermission).collect(Collectors.joining(","));
    }

    @Override
    @Transactional
    @DS("upms")
    public User registerUser(String username, String password) {
        User user = new User();
        user.setUserId(IdUtil.getSnowflake(1, 1).nextIdStr());
        user.setUsername(username);
        user.setPassword(password);
        user.setCreateTime(new Date());
        user.setStatus(User.STATUS_VALID);
        user.setSex(User.SEX_UNKNOW);
        user.setAvatar(User.DEFAULT_AVATAR);
        user.setDescription("注册用户");
        this.userMapper.insert(user);

        //获取默认角色
        List<Role> roleList = roleMapper.selectList(new QueryWrapper<Role>().lambda().eq(Role::getDefaultRole, true));
        if (roleList != null && roleList.size() > 0) {
            for (Role role : roleList) {
                UserRole ur = new UserRole();
                ur.setUserId(user.getUserId());
                ur.setRoleId(role.getRoleId());
                ur.setId(IdUtil.createSnowflake(1, 1).nextId() + "");
                userRoleMapper.insert(ur);
            }
        }
        return user;
    }

    @Override
    @DS("upms")
    public AuthUser transformAuthUser(String username, String mobile, String loginType) {
        User user = null;
        if (StrUtil.isNotEmpty(username)) {
            user = this.findUserByName(username);
        } else if (StrUtil.isNotEmpty(mobile)) {
            user = this.findUserByMobile(mobile);
        }

        if (user != null) {
            String permissions = this.findUserPermissions(user.getUsername());
            boolean notLocked = false;
            if (StrUtil.equals(User.STATUS_VALID, user.getStatus()))
                notLocked = true;
            String password = user.getPassword();
            if (StrUtil.equals(loginType, LoginTypeConstant.SOCIAL_LOGIN)) {
                password = passwordEncoder.encode(Constant.SOCIAL_LOGIN_PASSWORD);
            }
            AuthUser authUser = new AuthUser(user.getUsername(), password, true, true, true, notLocked,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));

            BeanUtils.copyProperties(user, authUser);
            return authUser;
        } else {
            log.error("登录失败，用户不存在");
            throw new UsernameNotFoundException("登录失败，用户不存在");
        }
    }
}
