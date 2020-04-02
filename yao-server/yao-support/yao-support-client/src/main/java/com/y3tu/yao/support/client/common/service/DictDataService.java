package com.y3tu.yao.support.client.common.service;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.support.client.common.entity.DictData;

/**
 * 字典数据服务
 * @author y3tu
 */
public interface DictDataService extends BaseService<DictData> {

    Page<DictData> findDictDataPage(Page<DictData> page);

    void createDictData(DictData dictData);

    void updateDictData(DictData dictData);

    void deleteDictData(String[] ids);

}

