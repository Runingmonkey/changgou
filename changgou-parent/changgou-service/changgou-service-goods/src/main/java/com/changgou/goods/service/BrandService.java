package com.changgou.goods.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:itheima
 * @Description:Brand业务层接口
 *****/
public interface BrandService {

    /**
     * @description: 根据分类id 查询品牌信息
     * @author: QIXIANG LING
     * @date: 2020/7/3 16:54
     * @param: id
     * @return: java.util.List<com.changgou.goods.pojo.Brand>
     */
    List<Brand> findByCategoryId(Integer id);


    /***
     * Brand多条件分页查询
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(Brand brand, int page, int size);

    /***
     * Brand分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> findPage(int page, int size);

    /***
     * Brand多条件搜索方法
     * @param brand
     * @return
     */
    List<Brand> findList(Brand brand);

    /***
     * 删除Brand
     * @param id
     */
    void delete(Integer id);

    /***
     * 修改Brand数据
     * @param brand
     */
    void update(Brand brand);

    /***
     * 新增Brand
     * @param brand
     */
    void add(Brand brand);

    /**
     * 根据ID查询Brand
     * @param id
     * @return
     */
     Brand findById(Integer id);

    /***
     * 查询所有Brand
     * @return
     */
    List<Brand> findAll();
}
