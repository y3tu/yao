package com.y3tu.yao.support.server.common.impl;

import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.support.client.common.entity.DictSql;
import com.y3tu.yao.support.client.common.mapper.DictSqlMapper;
import com.y3tu.yao.support.client.common.service.DictSqlService;
import org.springframework.stereotype.Service;


@Service("dictSqlService")
public class DictSqlServiceImpl extends BaseServiceImpl<DictSqlMapper, DictSql> implements DictSqlService {

}
