package com.changgou.user.service;

import com.changgou.user.pojo.ItemsCollect;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:itheima
 * @Description:ItemsCollect业务层接口
 *****/
public interface ItemsCollectService {

    /***
     * ItemsCollect多条件分页查询
     * @param itemsCollect
     * @param page
     * @param size
     * @return
     */
    PageInfo<ItemsCollect> findPage(ItemsCollect itemsCollect, int page, int size);

    /***
     * ItemsCollect分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<ItemsCollect> findPage(int page, int size);

    /***
     * ItemsCollect多条件搜索方法
     * @param itemsCollect
     * @return
     */
    List<ItemsCollect> findList(ItemsCollect itemsCollect);

    /***
     * 删除ItemsCollect
     * @param id
     */
    void delete(String id);

    /***
     * 修改ItemsCollect数据
     * @param itemsCollect
     */
    void update(ItemsCollect itemsCollect);

    /***
     * 新增ItemsCollect
     * @param itemsCollect
     */
    void add(ItemsCollect itemsCollect);

    /**
     * 根据ID查询ItemsCollect
     * @param id
     * @return
     */
     ItemsCollect findById(String id);

    /***
     * 查询所有ItemsCollect
     * @return
     */
    List<ItemsCollect> findAll();
}
