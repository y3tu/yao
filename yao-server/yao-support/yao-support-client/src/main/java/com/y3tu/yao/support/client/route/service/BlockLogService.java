package com.y3tu.yao.support.client.route.service;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.support.client.route.entity.BlockLog;

public interface BlockLogService extends BaseService<BlockLog> {

    Page<BlockLog> findBlockLogPage(Page<BlockLog> page);

    void createBlockLog(BlockLog blockLog);

    void updateBlockLog(BlockLog blockLog);

    void deleteBlockLog(String[] ids);
}
