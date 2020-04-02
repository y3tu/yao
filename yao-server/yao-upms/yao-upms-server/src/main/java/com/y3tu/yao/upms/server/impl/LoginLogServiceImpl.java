package com.y3tu.yao.upms.server.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.y3tu.tool.core.http.IpUtil;
import com.y3tu.tool.core.util.ObjectUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.common.util.YaoUtil;
import com.y3tu.yao.upms.client.entity.LoginLog;
import com.y3tu.yao.upms.client.entity.User;
import com.y3tu.yao.upms.client.mapper.LoginLogMapper;
import com.y3tu.yao.upms.client.service.LoginLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 登录日志service实现
 *
 * @author y3tu
 */
@Service("loginLogService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LoginLogServiceImpl extends BaseServiceImpl<LoginLogMapper, LoginLog> implements LoginLogService {

    @Override
    public Page<LoginLog> findLoginLogPage(Page<LoginLog> Page) {
        QueryWrapper<LoginLog> queryWrapper = new QueryWrapper<>();

        if (ObjectUtil.isNotEmpty(Page.getEntity()) && StrUtil.isNotEmpty(Page.getEntity().getUsername())) {
            queryWrapper.lambda().eq(LoginLog::getUsername, Page.getEntity().getUsername().toLowerCase());
        }
        if (StrUtil.isNotBlank(Page.getEntity().getLoginTimeFrom()) && StrUtil.isNotBlank(Page.getEntity().getLoginTimeTo())) {
            queryWrapper.lambda()
                    .ge(LoginLog::getLoginTime, Page.getEntity().getLoginTimeFrom())
                    .le(LoginLog::getLoginTime, Page.getEntity().getLoginTimeTo());
        }
        queryWrapper.lambda().orderByDesc(LoginLog::getLoginTime);
        return this.page(Page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveLoginLog(LoginLog loginLog) {
        loginLog.setLoginTime(new Date());
        HttpServletRequest request = YaoUtil.getHttpServletRequest();
        String ip = IpUtil.getIpAddr(request);
        loginLog.setIp(ip);
        loginLog.setLocation(IpUtil.getCityInfo(ip));
        this.saveBySnowflakeId(loginLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLoginLogByIds(String[] ids) {
        List<String> list = Arrays.asList(ids);
        baseMapper.deleteBatchIds(list);
    }

    @Override
    public Long findTotalVisitCount() {
        return this.baseMapper.findTotalVisitCount();
    }

    @Override
    public Long findTodayVisitCount() {
        return this.baseMapper.findTodayVisitCount();
    }

    @Override
    public Long findTodayIp() {
        return this.baseMapper.findTodayIp();
    }

    @Override
    public List<Map<String, Object>> findLastTenDaysVisitCount(User user) {
        return this.baseMapper.findLastTenDaysVisitCount(user);
    }

    @Override
    public List<LoginLog> findUserLastSevenLoginLogs(String username) {
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        Page<LoginLog> Page = new Page();
        Page.setEntity(loginLog);
        Page.setCurrent(1);
        // 近7日记录
        Page.setSize(7);
        Page = this.findLoginLogPage(Page);
        return Page.getRecords();
    }
}
