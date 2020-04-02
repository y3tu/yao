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
 * 限流日志
 *
 * @author y3tu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_rate_limit_log")
public class RateLimitLog extends BaseEntity {

    @TableId(value = "id" , type = IdType.INPUT)
    private String id;
    /**
     * 被拦截请求IP
     */
    private String ip;
    /**
     * 被拦截请求URI
     */
    @TableField("request_uri")
    private String requestUri;
    /**
     * 被拦截请求方法
     */
    @TableField("request_method")
    private String requestMethod;
    /**
     * IP对应地址
     */
    private String location;
    /**
     * 拦截时间点
     */
    @TableField("create_time")
    private String createTime;

}
