package com.y3tu.yao.upms.client.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.y3tu.tool.web.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 部门实体
 *
 * @author y3tu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_department")
@Accessors(chain = true)
public class Department extends BaseEntity {

    /**
     * 部门树的根节点
     */
    public static final String TREE_ROOT = "0";
    /**
     * 主键
     */
    @TableId(value = "department_id", type = IdType.INPUT)
    private String departmentId;
    /**
     * 部门父id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 排序
     */
    private BigDecimal orderNum;

    /**
     * 部门名称
     */
    @TableField("DEPARTMENT_NAME")
    private String departmentName;
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
