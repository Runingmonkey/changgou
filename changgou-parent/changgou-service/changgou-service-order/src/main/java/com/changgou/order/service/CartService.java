package com.changgou.order.service;

import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;

import java.util.List;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/29 10:42
 */
public interface CartService {

    /**
     * @description: 添加购物车数据
     * @author: QIXIANG LING
     * @date: 2020/7/29 11:32
     * @param: num
     * @param: skuid
     * @param: username
     * @return: void
     */
    void addOrderItem(Integer num, String skuid, String username);

    /**
     * @description:  查询用户购物车数据
     * @author: QIXIANG LING
     * @date: 2020/7/29 11:33
     * @param: username
     * @return: java.util.List<com.changgou.order.pojo.OrderItem>
     */
    List<OrderItem> list(String username);



    /**
     * @description: 新增订单
     * @author: QIXIANG LING
     * @date: 2020/7/30 20:02
     * @param: order
     * @return: void
     */
    Order addOrder(Order order);

}
