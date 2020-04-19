package com.y3tu.yao.upms.client.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.y3tu.tool.web.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 资源表
 *
 * @author y3tu
 */
@Data
@TableName("t_resource")
@AllArgsConstructor
@NoArgsConstructor
public class Resource extends BaseEntity {

    /**
     * 资源树的根节点
     */
    public static final String TREE_ROOT = "-1";

    /**
     * 资源类型-按钮
     */
    public static final int RESOURCE_TYPE_BUTTON= 1;
    /**
     * 资源类型-菜单
     */
    public static final int RESOURCE_TYPE_MENU= 0;
    /**
     * 资源类型-目录
     */
    public static final int RESOURCE_TYPE_CATALOG= 2;


    /**
     * 主键
     */
    @TableId(value = "resource_id", type = IdType.INPUT)
    private String resourceId;
    /**
     * 资源父Id
     */
    @TableField("parent_id")
    private String parentId;
    /**
     * 资源名称
     */
    @TableField("resource_name")
    private String resourceName;
    /**
     * 菜单路径
     */
    private String path;
    /**
     * 前台页面组件
     */
    private String component;
    /**
     * 组件名称
     */
    @TableField("component_name")
    private String componentName;
    /**
     * 权限编码
     */
    private String permission;
    /**
     * 资源类型 0菜单 1按钮 2目录
     */
    private Integer type;
    /**
     * 排列权重顺序
     */
    @TableField("order_num")
    private int orderNum;

    /**
     * 图标
     */
    private String icon;
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

    /**
     * iframe地址
     */
    @TableField("iframe")
    private String iframe;

    /**
     * 是否隐藏
     */
    @TableField("hidden")
    private boolean hidden;

    /**
     * 是否缓存
     */
    @TableField("cache")
    private boolean cache;


}
