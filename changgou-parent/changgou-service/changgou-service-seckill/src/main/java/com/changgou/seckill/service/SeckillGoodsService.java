package com.changgou.seckill.service;

import com.changgou.seckill.pojo.SeckillGoods;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:itheima
 * @Description:SeckillGoods业务层接口
 *****/
public interface SeckillGoodsService {




    /***
     * SeckillGoods多条件分页查询
     * @param seckillGoods
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillGoods> findPage(SeckillGoods seckillGoods, int page, int size);

    /***
     * SeckillGoods分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<SeckillGoods> findPage(int page, int size);

    /***
     * SeckillGoods多条件搜索方法
     * @param seckillGoods
     * @return
     */
    List<SeckillGoods> findList(SeckillGoods seckillGoods);

    /***
     * 删除SeckillGoods
     * @param id
     */
    void delete(String id);

    /***
     * 修改SeckillGoods数据
     * @param seckillGoods
     */
    void update(SeckillGoods seckillGoods);

    /***
     * 新增SeckillGoods
     * @param seckillGoods
     */
    void add(SeckillGoods seckillGoods);

    /**
     * 根据ID查询SeckillGoods
     * @param id
     * @return
     */
     SeckillGoods findById(String id);

    /***
     * 查询所有SeckillGoods
     * @return
     */
    List<SeckillGoods> findAll();

    /**
     * @description: 根据时间段查询秒杀商品列表 key 是时间
     * @author: QIXIANG LING
     * @date: 2020/8/4 11:22
     * @param:
     * @return: java.util.List<com.changgou.seckill.pojo.SeckillGoods>
     */
    List<SeckillGoods> findSeckillGoods(String key);

    SeckillGoods goodsDetails(String time, String id);
}
