package com.changgou.user.service.impl;

import com.changgou.user.dao.ItemsHistoryMapper;
import com.changgou.user.pojo.ItemsHistory;
import com.changgou.user.service.ItemsHistoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/****
 * @Author:itheima
 * @Description:ItemsHistory业务层接口实现类
 *****/
@Service
public class ItemsHistoryServiceImpl implements ItemsHistoryService {

    @Autowired
    private ItemsHistoryMapper itemsHistoryMapper;


    /**
     * ItemsHistory条件+分页查询
     * @param itemsHistory 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<ItemsHistory> findPage(ItemsHistory itemsHistory, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(itemsHistory);
        //执行搜索
        return new PageInfo<ItemsHistory>(itemsHistoryMapper.selectByExample(example));
    }

    /**
     * ItemsHistory分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<ItemsHistory> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<ItemsHistory>(itemsHistoryMapper.selectAll());
    }

    /**
     * ItemsHistory条件查询
     * @param itemsHistory
     * @return
     */
    @Override
    public List<ItemsHistory> findList(ItemsHistory itemsHistory){
        //构建查询条件
        Example example = createExample(itemsHistory);
        //根据构建的条件查询数据
        return itemsHistoryMapper.selectByExample(example);
    }


    /**
     * ItemsHistory构建查询对象
     * @param itemsHistory
     * @return
     */
    public Example createExample(ItemsHistory itemsHistory){
        Example example=new Example(ItemsHistory.class);
        Example.Criteria criteria = example.createCriteria();
        if(itemsHistory!=null){
            // 
            if(!StringUtils.isEmpty(itemsHistory.getId())){
                    criteria.andEqualTo("id",itemsHistory.getId());
            }
            // 
            if(!StringUtils.isEmpty(itemsHistory.getName())){
                    criteria.andLike("name","%"+itemsHistory.getName()+"%");
            }
            // 
            if(!StringUtils.isEmpty(itemsHistory.getSkuid())){
                    criteria.andEqualTo("skuid",itemsHistory.getSkuid());
            }
            // 
            if(!StringUtils.isEmpty(itemsHistory.getImage())){
                    criteria.andEqualTo("image",itemsHistory.getImage());
            }
            // 
            if(!StringUtils.isEmpty(itemsHistory.getUserid())){
                    criteria.andEqualTo("userid",itemsHistory.getUserid());
            }
        }
        return example;
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        itemsHistoryMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改ItemsHistory
     * @param itemsHistory
     */
    @Override
    public void update(ItemsHistory itemsHistory){
        itemsHistoryMapper.updateByPrimaryKey(itemsHistory);
    }

    /**
     * 增加ItemsHistory
     * @param itemsHistory
     */
    @Override
    public void add(ItemsHistory itemsHistory){
        itemsHistoryMapper.insert(itemsHistory);
    }

    /**
     * 根据ID查询ItemsHistory
     * @param id
     * @return
     */
    @Override
    public ItemsHistory findById(String id){
        return  itemsHistoryMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询ItemsHistory全部数据
     * @return
     */
    @Override
    public List<ItemsHistory> findAll() {
        return itemsHistoryMapper.selectAll();
    }
}
