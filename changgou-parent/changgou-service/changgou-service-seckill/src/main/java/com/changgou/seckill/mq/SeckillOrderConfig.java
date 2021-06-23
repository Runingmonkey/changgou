package com.changgou.seckill.mq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/5 8:32
 */
@Configuration
public class SeckillOrderConfig {

    // 交换机
    @Bean
    public Exchange seckillOrderExchange(){
        return new DirectExchange("seckillOrderExchange",true , false);
    }

    // 队列
    @Bean
    public Queue seckillOrderQueue(){
        return new Queue("seckillOrderQueue",true);
    }

    // 绑定
    @Bean
    public Binding bindingOrder(Exchange seckillOrderExchange,Queue seckillOrderQueue){
        return BindingBuilder.bind(seckillOrderQueue).to(seckillOrderExchange).with("seckillOrderQueue").noargs();
    }

}
