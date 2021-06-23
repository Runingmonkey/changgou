package com.changgou.user.service;

import com.changgou.user.pojo.Comments;
import com.github.pagehelper.PageInfo;

import java.util.List;

/****
 * @Author:itheima
 * @Description:Comments业务层接口
 *****/
public interface CommentsService {

    /***
     * Comments多条件分页查询
     * @param comments
     * @param page
     * @param size
     * @return
     */
    PageInfo<Comments> findPage(Comments comments, int page, int size);

    /***
     * Comments分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Comments> findPage(int page, int size);

    /***
     * Comments多条件搜索方法
     * @param comments
     * @return
     */
    List<Comments> findList(Comments comments);

    /***
     * 删除Comments
     * @param id
     */
    void delete(String id);

    /***
     * 修改Comments数据
     * @param comments
     */
    void update(Comments comments);

    /***
     * 新增Comments
     * @param comments
     */
    void add(Comments comments);

    /**
     * 根据ID查询Comments
     * @param id
     * @return
     */
     Comments findById(String id);

    /***
     * 查询所有Comments
     * @return
     */
    List<Comments> findAll();
}
