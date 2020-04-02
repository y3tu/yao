package com.y3tu.yao.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.auth.entity.OAuthClientDetails;
import com.y3tu.yao.auth.exception.OAuthClientDetailsException;
import com.y3tu.yao.auth.mapper.OAuthClientDetailsMapper;
import com.y3tu.yao.auth.service.OAuthClientDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 客户端实现
 *
 * @author y3tu
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OAuthClientDetailsServiceImpl extends BaseServiceImpl<OAuthClientDetailsMapper, OAuthClientDetails> implements OAuthClientDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisClientDetailsService redisClientDetailsService;

    @Override
    public Page<OAuthClientDetails> findOAuthClientDetailsPage(Page<OAuthClientDetails> page) {
        LambdaQueryWrapper<OAuthClientDetails> queryWrapper = new LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(page.getEntity().getClientId())) {
            queryWrapper.eq(OAuthClientDetails::getClientId, page.getEntity().getClientId());
        }

        Page<OAuthClientDetails> result = page(page, queryWrapper);
        List<OAuthClientDetails> records = new ArrayList<>();
        result.getRecords().forEach(o -> {
            o.setOriginSecret(null);
            o.setClientSecret(null);
            records.add(o);
        });
        result.setRecords(records);
        return result;
    }

    @Override
    public OAuthClientDetails findById(String clientId) {
        return this.baseMapper.selectById(clientId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOAuthClientDetails(OAuthClientDetails oauthClientDetails) throws OAuthClientDetailsException {
        OAuthClientDetails byId = this.findById(oauthClientDetails.getClientId());
        if (byId != null) {
            throw new OAuthClientDetailsException("该Client已存在");
        }
        oauthClientDetails.setOriginSecret(oauthClientDetails.getClientSecret());
        oauthClientDetails.setClientSecret(passwordEncoder.encode(oauthClientDetails.getClientSecret()));
        boolean saved = this.save(oauthClientDetails);
        if (saved) {
            log.info("缓存Client -> {}", oauthClientDetails);
            this.redisClientDetailsService.loadClientByClientId(oauthClientDetails.getClientId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOAuthClientDetails(OAuthClientDetails oauthClientDetails) {
        String clientId = oauthClientDetails.getClientId();

        LambdaQueryWrapper<OAuthClientDetails> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OAuthClientDetails::getClientId, oauthClientDetails.getClientId());

        oauthClientDetails.setClientId(null);
        oauthClientDetails.setClientSecret(null);
        boolean updated = this.update(oauthClientDetails, queryWrapper);
        if (updated) {
            log.info("更新Client -> {}", oauthClientDetails);
            this.redisClientDetailsService.removeRedisCache(clientId);
            this.redisClientDetailsService.loadClientByClientId(clientId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOAuthClientDetails(String clientIds) {
        Object[] clientIdArray = StrUtil.split(clientIds, ",");
        LambdaQueryWrapper<OAuthClientDetails> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(OAuthClientDetails::getClientId, clientIdArray);
        boolean removed = this.remove(queryWrapper);
        if (removed) {
            log.info("删除ClientId为({})的Client", clientIds);
            Arrays.stream(clientIdArray).forEach(c -> this.redisClientDetailsService.removeRedisCache(String.valueOf(c)));
        }
    }
}
