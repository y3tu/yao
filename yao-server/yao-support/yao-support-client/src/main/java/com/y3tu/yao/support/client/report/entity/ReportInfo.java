package com.y3tu.yao.support.client.report.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.y3tu.tool.web.base.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 报表配置信息
 *
 * @author y3tu
 */
@Data
@Accessors(chain = true)
@TableName("report_info")
public class ReportInfo extends BaseEntity {

    /**
     * 报表ID
     */
    @TableId(value = "ID", type = IdType.INPUT)
    private String id;
    /**
     * 数据源ID
     */
    @TableField("DS_ID")
    private Integer dsId;
    /**
     * 数据源名称
     */
    @TableField("DS_NAME")
    private String dsName;
    /**
     * 报表名称
     */
    @TableField("NAME")
    private String name;
    /**
     * 报表模板名称
     */
    @TableField("TEMPLATE_NAME")
    private String templateName;
    /**
     * 报表状态（00X表示禁用，00A表示启用)
     */
    @TableField("STATUS")
    private String status;
    /**
     * 是否是多sheet页报表标记
     */
    @TableField("MANY_SHEET_FLAG")
    private String manySheetFlag;
    /**
     * 多sheet页区分参数配置
     */
    @TableField("MANY_SHEET_PARAM")
    private String manySheetParam;
    /**
     * 参数配置
     */
    @TableField("PARAMS")
    private String params;
    /**
     * 说明备注
     */
    @TableField("REMARKS")
    private String remarks;
    /**
     * 是否通用报表:0,否，1是
     */
    private Integer isCommon;
    /**
     * 列头
     */
    private String columnHeads;
    /**
     * 报表查询执行的sql
     */
    private String sql;
    /**
     * 报表标题
     */
    private String title;
    /**
     * 报表脚标
     */
    private String footer;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @TableField("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

}
