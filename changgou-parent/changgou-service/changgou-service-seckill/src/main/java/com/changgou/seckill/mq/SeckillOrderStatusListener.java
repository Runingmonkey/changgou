package com.changgou.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.service.SeckillGoodsService;
import com.changgou.seckill.service.SeckillOrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description: 监听到订单支付成功; 就订单信息持久化到数据库中
 * @author: QIXIANG LING
 * @date: 2020/8/6 14:30
 */
@Component
@RabbitListener(queues = "queueSeckillOrder")
public class SeckillOrderStatusListener {

    // 监听到消息之后需要去操作 秒杀订单状态
    @Autowired
    private SeckillOrderService seckillOrderService;

    @RabbitHandler
    public void getPayStatus(String message){
        System.out.println("message: " + message);
        Map<String,String> resultMap = JSON.parseObject(message, Map.class);
        // 从attach 中获取 用户名
        Map<String, String> attachMap = JSON.parseObject(resultMap.get("attach"), Map.class);
        String username = attachMap.get("username");
        // 返回的 支付状态码 result_code
        String result_code = resultMap.get("result_code");
        // 支付订单号;
        if ("SUCCESS".equals(result_code)) {
            String transaction_id = resultMap.get("transaction_id");
            seckillOrderService.updateOSeckillOrderStatus(username, transaction_id);
        }else{
            // 支付失败; 删除订单
            seckillOrderService.closeOrder(attachMap.get("username"));
        }
    }
}
