package com.y3tu.yao.support.client.common.entity;

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
 * 数据源配置
 *
 * @author y3tu
 */
@Data
@Accessors(chain = true)
@TableName("data_source_config")
public class DataSourceConfig extends BaseEntity {

    @TableId(value = "id" , type = IdType.INPUT)
    private String id;

    /**
     * 数据源名称
     */
    private String name;

    /**
     * 数据库类型
     */
    private String type;

    /**
     * 驱动
     */
    @TableField("driver_class")
    private String driverClass;

    /**
     * url
     */
    @TableField("jdbc_url")
    private String jdbcUrl;

    private String username;

    private String password;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("modify_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

}
