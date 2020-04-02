package com.y3tu.yao.support.server.common.impl;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.impl.BaseServiceImpl;
import com.y3tu.yao.support.client.common.entity.DataSourceConfig;
import com.y3tu.yao.support.client.common.mapper.DataSourceConfigMapper;
import com.y3tu.yao.support.client.common.service.DataSourceConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;

/**
 * @author y3tu
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DataSourceConfigServiceImpl extends BaseServiceImpl<DataSourceConfigMapper, DataSourceConfig> implements DataSourceConfigService {
    @Override
    public Page<DataSourceConfig> findDataSourceConfigPage(Page<DataSourceConfig> page) {
        return this.page(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDataSourceConfig(DataSourceConfig dataSourceConfig) {
        dataSourceConfig.setCreateTime(new Date());
        dataSourceConfig.setModifyTime(new Date());
        this.saveBySnowflakeId(dataSourceConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDataSourceConfig(DataSourceConfig dataSourceConfig) {
        dataSourceConfig.setModifyTime(new Date());
        this.updateById(dataSourceConfig);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDataSourceConfig(String[] ids) {
        this.removeByIds(Arrays.asList(ids));
    }

    @Override
    public R testConnect(DataSourceConfig dataSourceConfig) {
        try {
            Class.forName(dataSourceConfig.getDriverClass());
            //2.获得数据库链接
            Connection conn = DriverManager.getConnection(dataSourceConfig.getJdbcUrl(), dataSourceConfig.getUsername(), dataSourceConfig.getPassword());
            Statement st = conn.createStatement();
        } catch (Exception e) {
            return R.error("连接失败");
        }

        return R.success();
    }


}
