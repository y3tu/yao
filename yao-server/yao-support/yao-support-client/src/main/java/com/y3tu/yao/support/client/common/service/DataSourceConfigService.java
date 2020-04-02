package com.y3tu.yao.support.client.common.service;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.tool.web.base.service.BaseService;
import com.y3tu.yao.support.client.common.entity.DataSourceConfig;

/**
 * @author y3tu
 */
public interface DataSourceConfigService extends BaseService<DataSourceConfig> {

    Page<DataSourceConfig> findDataSourceConfigPage(Page<DataSourceConfig> page);

    void createDataSourceConfig(DataSourceConfig dataSourceConfig);

    void updateDataSourceConfig(DataSourceConfig dataSourceConfig);

    void deleteDataSourceConfig(String[] ids);

    R testConnect(DataSourceConfig dataSourceConfig);

}
