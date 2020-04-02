package com.y3tu.yao.support.client.route.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.y3tu.tool.web.base.entity.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 黑名单
 */
@Data
@Accessors(chain = true)
@TableName("t_black")
public class Black extends BaseEntity {

    public static final String CLOSE = "0";
    public static final String OPEN = "1";

    public static final String METHOD_ALL = "all";

    @TableId(value = "id" , type = IdType.INPUT)
    private String id;
    /**
     * 黑名单ip
     */
    private String ip;
    /**
     * 请求URI
     */
    @TableField("request_uri")
    private String requestUri;
    /**
     * 请求方法，如果为ALL则表示对所有方法生效
     */
    @TableField("request_method")
    private String requestMethod;
    /**
     * 限制时间起
     */
    @TableField("limit_from")
    private String limitFrom;
    /**
     * 限制时间止
     */
    @TableField("limit_to")
    private String limitTo;
    /**
     * ip对应地址
     */
    private String location;
    /**
     * 状态，0关闭，1开启
     */
    private String status;
    /**
     * 规则创建时间
     */
    @TableField("create_time")
    private String createTime;

}
