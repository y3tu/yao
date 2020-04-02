package com.y3tu.yao.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.auth.entity.UserConnection;
import com.y3tu.yao.auth.mapper.UserConnectionMapper;
import com.y3tu.yao.auth.service.UserConnectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户绑定实现
 *
 * @author y3tu
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class UserConnectionServiceImpl extends BaseServiceImpl<UserConnectionMapper, UserConnection> implements UserConnectionService {

    @Override
    public UserConnection selectByCondition(String providerName, String providerUserId) {
        LambdaQueryWrapper<UserConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserConnection::getProviderName, providerName)
                .eq(UserConnection::getProviderUserId, providerUserId);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public List<UserConnection> selectByCondition(String username) {
        LambdaQueryWrapper<UserConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserConnection::getUserName, username);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createUserConnection(UserConnection userConnection) {
        this.saveBySnowflakeId(userConnection);
    }

    @Override
    @Transactional
    public void deleteByCondition(String username, String providerName) {
        LambdaQueryWrapper<UserConnection> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserConnection::getUserName, username);
        queryWrapper.eq(UserConnection::getProviderName, providerName);
        this.remove(queryWrapper);
    }
}
