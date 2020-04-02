package com.y3tu.yao.support.server.common.controller;

import cn.hutool.db.meta.Table;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.y3tu.tool.core.db.MetaUtil;
import com.y3tu.tool.core.exception.ToolException;
import com.y3tu.tool.core.io.IoUtil;
import com.y3tu.tool.core.pojo.R;
import com.y3tu.yao.support.client.common.entity.DataSourceConfig;
import com.y3tu.yao.support.client.common.entity.GeneratorConfig;
import com.y3tu.yao.support.client.common.pojo.ColumnInfo;
import com.y3tu.yao.support.client.common.pojo.TableInfo;
import com.y3tu.yao.support.client.common.service.DataSourceConfigService;
import com.y3tu.yao.support.client.common.service.GeneratorConfigService;
import com.y3tu.yao.support.server.common.util.DataSourceUtil;
import com.y3tu.yao.support.server.common.util.GeneratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成器Controller
 *
 * @author y3tu
 */
@RestController
@RequestMapping("common/generator")
@Slf4j
public class GeneratorController {

    @Autowired
    GeneratorConfigService generatorConfigService;
    @Autowired
    DataSourceConfigService dataSourceConfigService;

    /**
     * 查询数据库所有表的信息
     */
    @GetMapping("getTables")
    public R getTables(String dataSourceConfigId) {
        DataSource dataSource = getDataSource(dataSourceConfigId);
        return R.success(MetaUtil.getTables(dataSource));
    }

    /**
     * 根据对应数据源和表名获取表的字段信息
     */
    @GetMapping("getTableColumn")
    public R getTableColumn(String dataSourceConfigId, String tableName) {
        DataSource dataSource = getDataSource(dataSourceConfigId);
        Table table = MetaUtil.getTableMeta(dataSource, tableName);
        return R.success(table);
    }

    /**
     * 获取代码生成配置信息
     */
    @GetMapping("getGeneratorConfig")
    public R getGeneratorConfig() {
        GeneratorConfig generatorConfig = generatorConfigService.getOne(new QueryWrapper<>());
        return R.success(generatorConfig);
    }

    /**
     * 更新代码生成配置
     */
    @PutMapping("updateGeneratorConfig")
    public R updateGeneratorConfig(GeneratorConfig generatorConfig) {
        generatorConfigService.updateById(generatorConfig);
        return R.success();
    }

    @PostMapping("/genPreview")
    public R genPreview(@RequestBody TableInfo tableInfo) {
        tableInfo = buildTableInfo(tableInfo);
        GeneratorConfig generatorConfig = generatorConfigService.getOne(new QueryWrapper<>());
        List<Map<String, Object>> code =  GeneratorUtil.genPreview(tableInfo, generatorConfig);
        return R.success(code);
    }

    /**
     * 生成代码并打包下载
     */
    @PostMapping("/genZip")
    public void genZip(@RequestBody TableInfo tableInfo, HttpServletResponse response) {

        tableInfo = buildTableInfo(tableInfo);
        GeneratorConfig generatorConfig = generatorConfigService.getOne(new QueryWrapper<>());

        //文件生成和下载设置
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(outputStream);
            GeneratorUtil.genZip(tableInfo, generatorConfig, zip);
            IoUtil.close(zip);
            byte[] data = outputStream.toByteArray();

            response.reset();
            response.setHeader("Content-Disposition", String.format("attachment; filename=%s.zip", tableInfo.getTableName()));
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");

            IoUtil.write(response.getOutputStream(), Boolean.TRUE, data);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ToolException("代码生成失败!");
        }

    }

    /**
     * 根据数据源配置id获取DataSource
     */
    private DataSource getDataSource(String dataSourceConfigId) {
        DataSourceConfig dataSourceConfig = dataSourceConfigService.getById(dataSourceConfigId);
        if (dataSourceConfig != null) {
            return DataSourceUtil.getDataSource(dataSourceConfig);
        }
        return null;
    }

    /**
     * 处理TableInfo信息
     * @param tableInfo
     * @return
     */
    private TableInfo buildTableInfo(TableInfo tableInfo) {
        DataSource dataSource = getDataSource(tableInfo.getDataSourceConfigId());
        Table table = MetaUtil.getTableMeta(dataSource, tableInfo.getTableName());

        Set<String> pkNames = table.getPkNames();
        for (ColumnInfo columnInfo : tableInfo.getColumns()) {
            for (String pkName : pkNames) {
                if (columnInfo.getName().equals(pkName)) {
                    tableInfo.setPk(columnInfo);
                }
            }
        }
        return tableInfo;
    }
}
