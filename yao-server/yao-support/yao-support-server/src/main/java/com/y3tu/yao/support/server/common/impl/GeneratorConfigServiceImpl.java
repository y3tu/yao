package com.y3tu.yao.support.server.common.impl;

import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.support.client.common.entity.GeneratorConfig;
import com.y3tu.yao.support.client.common.mapper.GeneratorMapper;
import com.y3tu.yao.support.client.common.service.GeneratorConfigService;
import org.springframework.stereotype.Service;

/**
 * 代码生成服务
 *
 * @author y3tu
 */
@Service("generatorService")
public class GeneratorConfigServiceImpl extends BaseServiceImpl<GeneratorMapper, GeneratorConfig> implements GeneratorConfigService {

}
