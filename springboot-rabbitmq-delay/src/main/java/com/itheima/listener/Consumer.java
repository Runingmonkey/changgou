package com.itheima.listener;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RabbitListener(queues = "queue2")
public class Consumer {

    /**
     * 监听延迟队列消息
     * @param message
     */
    @RabbitHandler
    public void test(String message){

        System.out.println("收到消息的时间为:" + new Date());
        System.out.println("消息的内容为:" + message);
    }
}
