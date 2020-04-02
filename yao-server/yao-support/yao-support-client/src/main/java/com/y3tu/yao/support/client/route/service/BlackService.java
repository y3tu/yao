package com.y3tu.yao.support.client.route.service;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.support.client.route.entity.Black;

public interface BlackService extends BaseService<Black> {

    Page<Black> findBlackPage(Page<Black> page);

    void createBlack(Black black);

    void updateBlack(Black black);

    void deleteBlack(String[] ids);
}
