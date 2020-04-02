package com.y3tu.yao.support.server.report.controller;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.druid.DruidConfig;
import com.y3tu.tool.core.pojo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.concurrent.ScheduledExecutorService;


/**
 * data_source Controller
 *
 * @author y3tu
 */
@RestController
@RequestMapping("/report/dataSource")
public class DataSourceController{


    @Autowired
    DynamicDataSourceCreator dynamicDataSourceCreator;

    @GetMapping("get")
    public R getDataSource(){

        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setUrl("jdbc:mysql://cdb-d5ge3ujs.cd.tencentcdb.com:10003/auth??allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8");
        dataSourceProperty.setUsername("root");
        dataSourceProperty.setPassword("y3tu46262966");
        dataSourceProperty.setDriverClassName("com.mysql.jdbc.Driver");
        DruidDataSource dataSource= (DruidDataSource) dynamicDataSourceCreator.createDruidDataSource(dataSourceProperty);
        if(!dataSource.isClosed()){
            dataSource.close();
            dataSource.close();
        }
        return R.success();
    }

}
