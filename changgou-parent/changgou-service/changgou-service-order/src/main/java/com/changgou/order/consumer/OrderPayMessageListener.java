package com.changgou.order.consumer;

import com.alibaba.fastjson.JSON;
import com.changgou.order.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/2 13:05
 */
@Component
@RabbitListener(queues = "queue.order")
public class OrderPayMessageListener {

    @Autowired
    private OrderService orderService;

    /**
     * @description: 监听到队列 消息 微信回调 我们指定的方法 之后 ; 把消息发送到队列中 ; 用监听类更新订单状态
     * @author: QIXIANG LING
     * @date: 2020/8/2 17:06
     * @param: msg
     * @return: void
     */
    @RabbitHandler
    public void readMessage(String msg){
        Map<String, String> map = JSON.parseObject(msg, Map.class);

        String return_code = map.get("return_code");
        if("SUCCESS".equals(return_code)){
            String result_code = map.get("result_code");
            String out_trade_no = map.get("out_trade_no");
            if ("SUCCESS".equals(result_code)){
                // 业务处理成功; 更新订单状态 ; 将订单持久化到 数据库
                if (out_trade_no != null) {
                    orderService.updateStatus(out_trade_no, map.get("transaction_id"));
                }
            }else{
                // 业务处理失败 ; 从数据库中删除订单
                if (out_trade_no != null) {
                    orderService.delete(out_trade_no);
                }
            }
        }
    }
}
