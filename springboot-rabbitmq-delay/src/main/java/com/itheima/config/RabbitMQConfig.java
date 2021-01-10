package com.itheima.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * 创建一个死信队列
     * @return
     */
    @Bean
    public Queue queue1(){
        return QueueBuilder
                .durable("queue1")
                .withArgument("x-dead-letter-routing-key","queue2")
                .withArgument("x-dead-letter-exchange","delayExchange")
                .build();
    }

    /***
     * 创建Queue2,真正读取消息的队列
     */
    @Bean
    public Queue queue2(){
        return new Queue("queue2");
    }

    /***
     * 创建交换机
     */
    @Bean
    public DirectExchange delayExchange(){
        return new DirectExchange("delayExchange");
    }


    /**
     * 绑定Queue2
     */
    @Bean
    public Binding bindQueu2DelayExchange(Queue queue2, Exchange delayExchange){
        return BindingBuilder.bind(queue2).to(delayExchange).with("queue2").noargs();
    }
}
