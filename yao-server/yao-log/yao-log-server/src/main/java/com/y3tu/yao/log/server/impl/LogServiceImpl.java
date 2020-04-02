package com.y3tu.yao.log.server.impl;

import com.y3tu.yao.log.client.entity.Log;
import com.y3tu.yao.log.client.service.LogService;
import com.y3tu.yao.log.server.mapper.LogMapper;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 系统日志服务实现类
 *
 * @author y3tu
 */
@Service
public class LogServiceImpl extends BaseServiceImpl<LogMapper, Log> implements LogService {

}
