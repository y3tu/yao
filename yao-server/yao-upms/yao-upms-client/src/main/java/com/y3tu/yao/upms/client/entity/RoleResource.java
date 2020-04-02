package com.y3tu.yao.upms.client.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.y3tu.tool.web.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 角色权限关联表
 *
 * @author y3tu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_role_resource")
public class RoleResource extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    private String id;
    /**
     * 资源ID
     */
    @TableField("resource_id")
    private String resourceId;
    /**
     * 角色ID
     */
    @TableField("role_id")
    private String roleId;

}
