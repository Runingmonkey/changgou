package com.changgou.pay.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @description:  rabbit 的 队列 ; 交换机 ; 绑定
 * @author: QIXIANG LING
 * @date: 2020/8/2 11:48
 */
@Configuration
public class RabbitConfig {

    // 创建队列
    @Bean
    public Queue payQueue(){
        return new Queue("queue.order", true);
    }

    // 创建交换机
    @Bean
    public Exchange payExchange(){
        return new DirectExchange("exchange.order",true, false);
    }

    // 创建秒杀 队列
    @Bean
    public Queue seckillQueue(){
        return new Queue("queueSeckillOrder", true);
    }

    // 秒杀队列绑定到交换机
    @Bean
    public Binding seckillQueueBind2Exchange(Exchange payExchange ,Queue seckillQueue ){
        return BindingBuilder.bind(seckillQueue).to(payExchange).with("queueSeckillOrder").noargs();
    }


    // 队列绑定到交换机
    @Bean
    public Binding bindQueueToExchange(Queue payQueue, Exchange payExchange){
        return BindingBuilder.bind(payQueue).to(payExchange).with("queue.order").noargs();
    }
}
