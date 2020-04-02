package com.y3tu.yao.support.server.common.util;

import cn.hutool.db.ds.DataSourceWrapper;
import cn.hutool.db.ds.druid.DruidDSFactory;
import cn.hutool.setting.Setting;
import com.alibaba.druid.pool.DruidDataSource;
import com.y3tu.yao.support.client.common.entity.DataSourceConfig;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author y3tu
 */
public class DataSourceUtil {

    /**
     * 根据数据源配置获取数据源
     *
     * @param dataSourceConfig
     * @return
     */
    public static DataSource getDataSource(DataSourceConfig dataSourceConfig) {

        Setting setting = new Setting();

        setting.set("url", dataSourceConfig.getJdbcUrl())
                .set("driver", dataSourceConfig.getDriverClass())
                .set("user", dataSourceConfig.getUsername())
                .set("pass", dataSourceConfig.getPassword());
        DataSourceWrapper dataSourceWrapper = (DataSourceWrapper) new DruidDSFactory(setting).getDataSource();
        DruidDataSource druidDataSource = (DruidDataSource) dataSourceWrapper.getRaw();
        //解决mysql数据库表备注无法查询的问题
        Properties properties = new Properties();
        properties.setProperty("remarks", "true");
        properties.setProperty("useInformationSchema", "true");
        druidDataSource.setConnectProperties(properties);
        return druidDataSource;
    }
}
