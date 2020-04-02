package com.y3tu.yao.upms.client.service;


import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.upms.client.entity.LoginLog;
import com.y3tu.yao.upms.client.entity.User;


import java.util.List;
import java.util.Map;

/**
 * 登录日志service
 *
 * @author y3tu
 */
public interface LoginLogService extends BaseService<LoginLog> {

    /**
     * 获取登录日志分页信息
     *
     * @param Page 传参
     * @return Page<LoginLog>
     */
    Page<LoginLog> findLoginLogPage(Page<LoginLog> Page);

    /**
     * 保存登录日志
     *
     * @param loginLog 登录日志
     */
    void saveLoginLog(LoginLog loginLog);

    /**
     * 删除登录日志
     *
     * @param ids 日志 id集合
     */
    void deleteLoginLogByIds(String[] ids);

    /**
     * 获取系统总访问次数
     *
     * @return Long
     */
    Long findTotalVisitCount();

    /**
     * 获取系统今日访问次数
     *
     * @return Long
     */
    Long findTodayVisitCount();

    /**
     * 获取系统今日访问 IP数
     *
     * @return Long
     */
    Long findTodayIp();

    /**
     * 获取系统近十天来的访问记录
     *
     * @param user 用户
     * @return 系统近十天来的访问记录
     */
    List<Map<String, Object>> findLastTenDaysVisitCount(User user);

    /**
     * 通过用户名获取用户最近7次登录日志
     *
     * @param username 用户名
     * @return 登录日志集合
     */
    List<LoginLog> findUserLastSevenLoginLogs(String username);
}
