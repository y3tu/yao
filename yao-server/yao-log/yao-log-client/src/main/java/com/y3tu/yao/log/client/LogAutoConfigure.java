package com.y3tu.yao.log.client;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.y3tu.yao.log.client.aspect.ControllerLogAspect;
import com.y3tu.yao.log.client.configure.LogProperties;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author y3tu
 */
@Configuration
@ConditionalOnClass(ControllerLogAspect.class)
@EnableConfigurationProperties(LogProperties.class)
public class LogAutoConfigure {

    @Bean
    @ConditionalOnMissingBean(ControllerLogAspect.class)
    public ControllerLogAspect logAspect() {
        return new ControllerLogAspect();
    }

    /**
     * rabbitMQ 传输对象转换器
     * @param objectMapper
     * @return
     */
    @Bean
    public MessageConverter jsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

}
