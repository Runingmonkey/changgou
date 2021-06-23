package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillOrder;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:itheima
 * @Description:SeckillOrder业务层接口
 *****/
public interface SeckillOrderService {

    /**
     * @description: 用户下单 失败后; 关闭订单
     * @author: QIXIANG LING
     * @date: 2020/8/6 16:20
     * @param:  String
     * @return:  void
     */
    void closeOrder(String username);


    /**
     * @description: 修改订单状态
     * @author: QIXIANG LING
     * @date: 2020/8/6 14:39
     * @param: username
     * @param: transactionId
     * @return: void
     */
    void updateOSeckillOrderStatus(String username , String transactionId);

    /**
     * @description:  用户下单是否成功
     * @author: QIXIANG LING
     * @date: 2020/8/4 17:05
     * @param: time
     * @param: seckillId
     * @param: userId
     * @return: java.lang.Boolean
     */
    Boolean add(String time, String seckillId ,String userId);

    /***
     * SeckillOrder多条件分页查询
     * @param seckillOrder
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size);

    /***
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillOrder> findPage(int page, int size);

    /***
     * SeckillOrder多条件搜索方法
     * @param seckillOrder
     * @return
     */
    List<SeckillOrder> findList(SeckillOrder seckillOrder);

    /***
     * 删除SeckillOrder
     * @param id
     */
    void delete(String id);

    /***
     * 修改SeckillOrder数据
     * @param seckillOrder
     */
    void update(SeckillOrder seckillOrder);

    /***
     * 新增SeckillOrder
     * @param seckillOrder
     */
//    void add(SeckillOrder seckillOrder);

    /**
     * 根据ID查询SeckillOrder
     * @param id
     * @return
     */
     SeckillOrder findById(String id);

    /***
     * 查询所有SeckillOrder
     * @return
     */
    List<SeckillOrder> findAll();
}
