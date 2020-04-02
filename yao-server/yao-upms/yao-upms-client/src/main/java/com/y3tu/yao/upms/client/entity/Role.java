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
 * 角色表
 *
 * @author y3tu
 */
@Data
@TableName("t_role")
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "ROLE_ID", type = IdType.INPUT)
    private String roleId;

    /**
     * 角色编码
     */
    @TableField("role_code")
    private String roleCode;
    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;
    /**
     * 是否默认角色
     */
    @TableField("DEFAULT_ROLE")
    private Boolean defaultRole;
    /**
     * 角色描述
     */
    private String remark;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("modify_time")
    private Date modifyTime;

    /**
     * 角色拥有的资源ID
     */
    @TableField(exist = false)
    private String resourceIds;

}
