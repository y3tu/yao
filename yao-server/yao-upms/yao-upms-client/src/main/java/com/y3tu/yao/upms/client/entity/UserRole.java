package com.y3tu.yao.upms.client.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.y3tu.tool.web.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 用户角色关联表
 *
 * @author y3tu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user_role")
public class UserRole extends BaseEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.INPUT)
    private String id;
    /**
     * 角色ID
     */
    @TableField("role_id")
    private String roleId;
    /**
     * 用户ID
     */
    @TableField("user_id")
    private String userId;

}
