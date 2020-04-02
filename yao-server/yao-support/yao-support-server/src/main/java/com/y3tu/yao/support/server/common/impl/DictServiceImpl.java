package com.y3tu.yao.support.server.common.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.y3tu.tool.core.collection.ArrayUtil;
import com.y3tu.tool.core.db.DbUtil;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.yao.support.client.common.emums.DictType;

import com.y3tu.tool.core.exception.BusinessException;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;

import com.y3tu.yao.support.client.common.entity.Dict;
import com.y3tu.yao.support.client.common.entity.DictData;
import com.y3tu.yao.support.client.common.entity.DictSql;
import com.y3tu.yao.support.client.common.mapper.DictMapper;
import com.y3tu.yao.support.client.common.service.DictDataService;
import com.y3tu.yao.support.client.common.service.DictService;
import com.y3tu.yao.support.client.common.service.DictSqlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("dictService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl extends BaseServiceImpl<DictMapper, Dict> implements DictService {

    @Autowired
    DictDataService dictDataService;
    @Autowired
    DictSqlService dictSqlService;
    @Autowired
    DataSource dataSource;

    @Override
    public Page<Dict> findDictPage(Page<Dict> page) {
        return this.page(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDict(Dict dict) {
        dict.setCreateTime(new Date());
        dict.setUpdateTime(new Date());
        this.saveBySnowflakeId(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(Dict dict) {
        dict.setUpdateTime(new Date());
        this.updateById(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(String[] ids) {
        this.removeByIds(Arrays.asList(ids));

        List<DictData> dictDataList = dictDataService.list(new LambdaQueryWrapper<DictData>().in(DictData::getDictId, ids));
        if (dictDataList.size() > 0) {
            List idList = dictDataList.stream().map(dictData -> dictData.getId()).collect(Collectors.toList());
            dictDataService.deleteDictData(ArrayUtil.toArray(idList, String.class));
        }

    }

    @Override
    public Map getDictByCodeAndValue(DictType dictType, String code, Object value) {
        if (dictType == DictType.DICT) {

            Dict dict = this.getOne(new QueryWrapper<Dict>().eq("code", code));
            List<DictData> dictDataList = dictDataService.list(new QueryWrapper<DictData>().eq("dict_id", dict.getId()));
            for (DictData dictData : dictDataList) {
                if (value.toString().equals(dictData.getValue())) {
                    return BeanUtil.beanToMap(dictData);
                }
            }
        } else {
            List<Map> mapList = getListBySqlCode(code);
            for (Map map : mapList) {
                if (value.equals(map.get("value").toString())) {
                    return map;
                }
            }
        }
        return null;
    }

    @Override
    public List<Map> getListBySqlCode(String sqlCode) {
        List<Map> list = null;
        try {
            DictSql dictSql = dictSqlService.getOne(new QueryWrapper<DictSql>().eq("sql_code", sqlCode));
            list = DbUtil.use(dataSource).query(dictSql.getSqlText(), Map.class);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new BusinessException(e);
        }
        return list;
    }
}
