package com.y3tu.yao.log.server.listener;

import com.rabbitmq.client.Channel;
import com.y3tu.yao.log.client.entity.Log;
import com.y3tu.yao.log.client.constant.LogQueueNameConstant;
import com.y3tu.tool.core.date.DateUtil;
import com.y3tu.yao.log.client.service.EsLogService;
import com.y3tu.yao.log.client.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 监听日志存储请求
 *
 * @author y3tu
 */
@Slf4j
@Component
public class LogReceiveListener {

    @Autowired
    private LogService logService;
    @Autowired
    private EsLogService esLogService;


    /**
     * 日志队列消费端，存数据库
     *
     * @param logDTO
     * @param channel
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = LogQueueNameConstant.DB_LOG_QUEUE)
    public void handlerDB(Log logDTO, Channel channel, Message message) {
        // 确认消息接受
        logDTO.setCreateTime(DateUtil.date());
        logService.saveBySnowflakeId(logDTO);
        LogReceiveListener.log.info("系统日志 DB 消费端成功消费信息：log={}", logDTO);

    }


    /**
     * 日志队列消费端，存elasticsearch
     *
     * @param logDTO
     * @param channel
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = LogQueueNameConstant.ES_LOG_QUEUE)
    public void handlerEs(Log logDTO, Channel channel, Message message) throws IOException {
        // 确认消息接受
        logDTO.setCreateTime(DateUtil.date());
        esLogService.saveLog(logDTO);
        LogReceiveListener.log.info("系统日志 ES 消费端成功消费信息：log={}", logDTO);
    }


}
