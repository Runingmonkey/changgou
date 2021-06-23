package com.changgou.goods.service;

import com.changgou.goods.pojo.Sku;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:itheima
 * @Description:Sku业务层接口
 *****/
public interface SkuService {

    /**
     * @description: 下单成功后更新库存数据
     * @author: QIXIANG LING
     * @date: 2020/8/1 9:45
     * @param: id
     * @param: num
     * @return: int
     */
    void decrCount(String username);


    /**
     * @description: 根据状态查询Sku 列表
     * @author: QIXIANG LING
     * @date: 2020/7/14 19:15
     * @param: status
     * @return: java.util.List<com.changgou.goods.pojo.Sku>
     */
    List<Sku> findByStatus(String status);

    /***
     * Sku多条件分页查询
     * @param sku
     * @param page
     * @param size
     * @return
     */
    PageInfo<Sku> findPage(Sku sku, int page, int size);

    /***
     * Sku分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Sku> findPage(int page, int size);

    /***
     * Sku多条件搜索方法
     * @param sku
     * @return
     */
    List<Sku> findList(Sku sku);

    /***
     * 删除Sku
     * @param id
     */
    void delete(String id);

    /***
     * 修改Sku数据
     * @param sku
     */
    void update(Sku sku);

    /***
     * 新增Sku
     * @param sku
     */
    void add(Sku sku);

    /**
     * 根据ID查询Sku
     * @param id
     * @return
     */
     Sku findById(String id);

    /***
     * 查询所有Sku
     * @return
     */
    List<Sku> findAll();
}
