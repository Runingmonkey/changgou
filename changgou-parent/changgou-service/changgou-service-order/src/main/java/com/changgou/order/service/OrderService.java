package com.changgou.order.service;

import com.changgou.order.pojo.Order;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:itheima
 * @Description:Order业务层接口
 *****/
public interface OrderService {

    /**
     * @description: 更新订单状态
     * @author: QIXIANG LING
     * @date: 2020/8/2 9:53
     * @param: orderId
     * @param: transactionId
     * @return: void
     */
    void updateStatus(String orderId, String transactionId);


    /**
     * @description:  支付失败 ;删除订单
     * @author: QIXIANG LING
     * @date: 2020/8/2 10:10
     * @param: orderId
     * @return: void
     */
    void deleteOrder(String orderId);


    /***
     * Order多条件分页查询
     * @param order
     * @param page
     * @param size
     * @return
     */
    PageInfo<Order> findPage(Order order, int page, int size);

    /***
     * Order分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Order> findPage(int page, int size);

    /***
     * Order多条件搜索方法
     * @param order
     * @return
     */
    List<Order> findList(Order order);

    /***
     * 删除Order
     * @param id
     */
    void delete(String id);

    /***
     * 修改Order数据
     * @param order
     */
    void update(Order order);

    /***
     * 新增Order
     * @param order
     */
    void add(Order order);

    /**
     * 根据ID查询Order
     * @param id
     * @return
     */
     Order findById(String id);

    /***
     * 查询所有Order
     * @return
     */
    List<Order> findAll();
}
