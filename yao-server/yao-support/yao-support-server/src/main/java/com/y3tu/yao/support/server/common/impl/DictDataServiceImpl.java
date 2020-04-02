package com.y3tu.yao.support.server.common.impl;

import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.support.client.common.entity.DictData;
import com.y3tu.yao.support.client.common.mapper.DictDataMapper;
import com.y3tu.yao.support.client.common.service.DictDataService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;


@Service("dictDataService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictDataServiceImpl extends BaseServiceImpl<DictDataMapper, DictData> implements DictDataService {

    @Override
    public Page<DictData> findDictDataPage(Page<DictData> page) {
        return this.page(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDictData(DictData dictData) {
        dictData.setCreateTime(new Date());
        dictData.setUpdateTime(new Date());
        this.saveBySnowflakeId(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictData(DictData dictData) {
        dictData.setUpdateTime(new Date());
        this.updateById(dictData);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictData(String[] ids) {
        this.removeByIds(Arrays.asList(ids));
    }
}
