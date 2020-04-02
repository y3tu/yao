package com.y3tu.yao.support.client.common.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 列的数据信息
 *
 * @author y3tu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColumnInfo {

    /**
     * 数据库字段名称
     **/
    private String name;

    /**
     * 允许空值
     **/
    private boolean isNullable;

    /**
     * 数据库字段类型
     **/
    private String typeName;

    /**
     * 数据库字段注释
     **/
    private String comment;

    /**
     * 是否在表格中显示
     **/
    private boolean enableShow;

    /**
     * 是否可增加编辑
     */
    private boolean enableEdit;

    /**
     * 表单编辑类型
     */
    private int formType;

    /**
     * 表单非空验证
     */
    private boolean enableValidate;

    /**
     * 是否可搜索
     */
    private boolean enableSearch;
    /**
     * 搜索框类型
     */
    private int searchType;

    /**
     * 是否可排序
     */
    private boolean enableSort;

}
