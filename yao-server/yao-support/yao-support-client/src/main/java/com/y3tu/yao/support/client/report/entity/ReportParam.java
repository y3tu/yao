package com.y3tu.yao.support.client.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.y3tu.tool.web.base.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 报表查询参数配置
 *
 * @author y3tu
 */
@Data
@Accessors(chain = true)
@TableName("report_param")
public class ReportParam extends BaseEntity {
    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;
    /**
     * 报表外键
     */
    private String reportInfoId;
    /**
     * 参数英文名称
     */
    private String paramName;
    /**
     * 参数中文名称
     */
    private String paramNameZh;
    /**
     * 关系表达式
     * =,!=,>,<,>=,<=,in,like, not in, not like
     */
    private String relation;
    /**
     * 取值类型
     * 1. 输入取值；2. 月份选择; 3. 日期选择; 4. 字典下拉; 5. 字典下拉取多值; 6.sql下拉; 7. sql下拉取多值; 8.多sheet页参数
     */
    private String valueType;
    /**
     * 1. where条件; 2. 分表条件
     */
    private Integer paramType;

}
