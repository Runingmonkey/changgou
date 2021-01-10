package com.itheima;

import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/7 13:53
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class Test {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @org.junit.Test
    public void send(){
        System.out.println("延迟消息发送的时间为:"+ new Date());
        rabbitTemplate.convertAndSend("queue1", (Object) "这是一条延时消息测试发送", new MessagePostProcessor() {
            /**
             * 设置延迟消息的属性
             * @param message
             * @return
             * @throws AmqpException
             */
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //消息的过期时间:单位是毫秒
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
    }

}
