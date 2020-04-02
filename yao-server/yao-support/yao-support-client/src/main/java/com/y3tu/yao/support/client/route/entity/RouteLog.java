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
 * 路由日志
 *
 * @author y3tu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_route")
public class RouteLog extends BaseEntity {

    @TableId(value = "id" , type = IdType.INPUT)
    private String id;
    /**
     * 请求IP
     */
    private String ip;
    /**
     * 请求URI
     */
    @TableField("request_uri")
    private String requestUri;
    /**
     * 目标URI
     */
    @TableField("target_uri")
    private String targetUri;
    /**
     * 请求方法
     */
    @TableField("request_method")
    private String requestMethod;
    /**
     * 目标服务
     */
    @TableField("target_server")
    private String targetServer;
    /**
     * 请求时间点
     */
    @TableField("create_time")
    private String createTime;
    /**
     * 请求地点
     */
    private String location;

}
