package com.y3tu.yao.support.client.route.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.y3tu.tool.web.base.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 路由用户
 *
 * @author y3tu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_route_user")
public class RouteUser extends BaseEntity {

    @TableId(value = "id" , type = IdType.INPUT)
    private String id;

    private String username;

    private String password;

    private String roles;

    @TableField("create_time")
    private String createTime;
}
