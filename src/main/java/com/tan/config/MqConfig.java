package com.tan.config;

import com.tan.utils.MqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqConfig {

    /**
     * 交换机
     * @return
     */
    @Bean
    public TopicExchange topicExchange(){
        return new TopicExchange(MqConstants.FILE_EXCHANGE, true, false);
    }

    /**
     * 添加或更新队列
     * @return
     */
    @Bean
    public Queue insertQueue(){
        return new Queue(MqConstants.FILE_INSERT_QUEUE,true);
    }

    /**
     * 删除队列
     * @return
     */
    @Bean
    public Queue deleteQueue(){
        return new Queue(MqConstants.FILE_DELETE_QUEUE,true);
    }

    //绑定关系
    @Bean
    public Binding bindingInsertQueue(){
        return BindingBuilder.bind(
                insertQueue())
                .to(topicExchange())
                .with(MqConstants.FILE_INSERT_KEY);
    }

    @Bean
    public Binding bindingDeleteQueue(){
        return BindingBuilder.bind(
                        deleteQueue())
                .to(topicExchange())
                .with(MqConstants.FILE_DELETE_KEY);
    }

}
