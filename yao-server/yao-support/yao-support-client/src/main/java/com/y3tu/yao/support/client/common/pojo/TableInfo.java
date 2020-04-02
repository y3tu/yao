package com.y3tu.yao.support.client.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 表信息
 *
 * @author y3tu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableInfo {
    /**
     * 名称
     */
    private String tableName;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 主键
     */
    private ColumnInfo pk;
    /**
     * 列名
     */
    private List<ColumnInfo> columns;
    /**
     * 驼峰类型
     */
    private String caseClassName;
    /**
     * 普通类型
     */
    private String lowerClassName;

    /**
     * 表单排列
     */
    private int formLineNum;

    /**
     * 数据源配置id
     */
    private String dataSourceConfigId;
}
