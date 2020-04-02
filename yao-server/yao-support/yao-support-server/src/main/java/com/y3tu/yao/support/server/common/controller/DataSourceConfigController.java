package com.y3tu.yao.support.server.common.controller;

import com.y3tu.tool.core.pojo.R;
import com.y3tu.tool.web.base.pojo.Page;
import com.y3tu.yao.support.client.common.entity.DataSourceConfig;
import com.y3tu.yao.support.client.common.service.DataSourceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author y3tu
 */
@RestController
@RequestMapping("/common/dataSourceConfig")
public class DataSourceConfigController {

    @Autowired
    DataSourceConfigService dataSourceConfigService;

    @PostMapping("page")
    public R page(@RequestBody Page<DataSourceConfig> page) {
        page = dataSourceConfigService.findDataSourceConfigPage(page);
        return R.success(page);
    }

    @PostMapping("create")
    public R create(@Valid DataSourceConfig dataSourceConfig) {
        dataSourceConfigService.createDataSourceConfig(dataSourceConfig);
        return R.success();
    }

    @PutMapping("update")
    public R update(@Valid DataSourceConfig dataSourceConfig) {
        dataSourceConfigService.updateDataSourceConfig(dataSourceConfig);
        return R.success();
    }

    @DeleteMapping("delete/{ids}")
    public R delete(@NotBlank(message = "{required}") @PathVariable String[] ids) {
        dataSourceConfigService.deleteDataSourceConfig(ids);
        return R.success();
    }

    @GetMapping("testConnect/{id}")
    public R testConnect(@PathVariable String id){
        DataSourceConfig dataSourceConfig = dataSourceConfigService.getById(id);
        return dataSourceConfigService.testConnect(dataSourceConfig);
    }

    @GetMapping("getAll")
    public R getAll(){
        return R.success(dataSourceConfigService.list());
    }

}
