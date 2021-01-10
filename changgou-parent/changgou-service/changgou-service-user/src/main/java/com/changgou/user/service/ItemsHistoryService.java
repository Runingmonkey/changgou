package com.changgou.user.service;

import com.changgou.user.pojo.ItemsHistory;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:itheima
 * @Description:ItemsHistory业务层接口
 *****/
public interface ItemsHistoryService {

    /***
     * ItemsHistory多条件分页查询
     * @param itemsHistory
     * @param page
     * @param size
     * @return
     */
    PageInfo<ItemsHistory> findPage(ItemsHistory itemsHistory, int page, int size);

    /***
     * ItemsHistory分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<ItemsHistory> findPage(int page, int size);

    /***
     * ItemsHistory多条件搜索方法
     * @param itemsHistory
     * @return
     */
    List<ItemsHistory> findList(ItemsHistory itemsHistory);

    /***
     * 删除ItemsHistory
     * @param id
     */
    void delete(String id);

    /***
     * 修改ItemsHistory数据
     * @param itemsHistory
     */
    void update(ItemsHistory itemsHistory);

    /***
     * 新增ItemsHistory
     * @param itemsHistory
     */
    void add(ItemsHistory itemsHistory);

    /**
     * 根据ID查询ItemsHistory
     * @param id
     * @return
     */
     ItemsHistory findById(String id);

    /***
     * 查询所有ItemsHistory
     * @return
     */
    List<ItemsHistory> findAll();
}
