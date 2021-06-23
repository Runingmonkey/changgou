package com.changgou.goods.service;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:itheima
 * @Description:Spu业务层接口
 *****/
public interface SpuService {
    /**
     * @description: 根据spuid 查询 spu 和 listsku
     * @author: QIXIANG LING
     * @date: 2020/7/4 16:27
     * @param: id
     * @return: com.changgou.goods.pojo.Goods
     */
    Goods findGoodsById(String id);

    /**
     * @description: 保存商品
     * @author: QIXIANG LING
     * @date: 2020/7/3 20:12
     * @param: goods
     * @return: void
     */
    void saveGoods(Goods goods);




    /***
     * Spu多条件分页查询
     * @param spu
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(Spu spu, int page, int size);

    /***
     * Spu分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spu> findPage(int page, int size);

    /***
     * Spu多条件搜索方法
     * @param spu
     * @return
     */
    List<Spu> findList(Spu spu);

    /***
     * 删除Spu
     * @param id
     */
    void delete(String id);

    /***
     * 修改Spu数据
     * @param spu
     */
    void update(Spu spu);

    /***
     * 新增Spu
     * @param spu
     */
    void add(Spu spu);

    /**
     * 根据ID查询Spu
     * @param id
     * @return
     */
     Spu findById(String id);

    /***
     * 查询所有Spu
     * @return
     */
    List<Spu> findAll();
}
