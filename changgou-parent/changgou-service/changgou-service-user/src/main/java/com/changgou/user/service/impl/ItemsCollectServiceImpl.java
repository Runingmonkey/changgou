package com.changgou.user.service.impl;

import com.changgou.user.dao.ItemsCollectMapper;
import com.changgou.user.pojo.ItemsCollect;
import com.changgou.user.service.ItemsCollectService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/****
 * @Author:itheima
 * @Description:ItemsCollect业务层接口实现类
 *****/
@Service
public class ItemsCollectServiceImpl implements ItemsCollectService {

    @Autowired
    private ItemsCollectMapper itemsCollectMapper;


    /**
     * ItemsCollect条件+分页查询
     * @param itemsCollect 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<ItemsCollect> findPage(ItemsCollect itemsCollect, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(itemsCollect);
        //执行搜索
        return new PageInfo<ItemsCollect>(itemsCollectMapper.selectByExample(example));
    }

    /**
     * ItemsCollect分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<ItemsCollect> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<ItemsCollect>(itemsCollectMapper.selectAll());
    }

    /**
     * ItemsCollect条件查询
     * @param itemsCollect
     * @return
     */
    @Override
    public List<ItemsCollect> findList(ItemsCollect itemsCollect){
        //构建查询条件
        Example example = createExample(itemsCollect);
        //根据构建的条件查询数据
        return itemsCollectMapper.selectByExample(example);
    }


    /**
     * ItemsCollect构建查询对象
     * @param itemsCollect
     * @return
     */
    public Example createExample(ItemsCollect itemsCollect){
        Example example=new Example(ItemsCollect.class);
        Example.Criteria criteria = example.createCriteria();
        if(itemsCollect!=null){
            // 
            if(!StringUtils.isEmpty(itemsCollect.getId())){
                    criteria.andEqualTo("id",itemsCollect.getId());
            }
            // 
            if(!StringUtils.isEmpty(itemsCollect.getName())){
                    criteria.andLike("name","%"+itemsCollect.getName()+"%");
            }
            // 
            if(!StringUtils.isEmpty(itemsCollect.getSkuid())){
                    criteria.andEqualTo("skuid",itemsCollect.getSkuid());
            }
            // 
            if(!StringUtils.isEmpty(itemsCollect.getImage())){
                    criteria.andEqualTo("image",itemsCollect.getImage());
            }
            // 
            if(!StringUtils.isEmpty(itemsCollect.getUserid())){
                    criteria.andEqualTo("userid",itemsCollect.getUserid());
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
        itemsCollectMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改ItemsCollect
     * @param itemsCollect
     */
    @Override
    public void update(ItemsCollect itemsCollect){
        itemsCollectMapper.updateByPrimaryKey(itemsCollect);
    }

    /**
     * 增加ItemsCollect
     * @param itemsCollect
     */
    @Override
    public void add(ItemsCollect itemsCollect){
        itemsCollectMapper.insert(itemsCollect);
    }

    /**
     * 根据ID查询ItemsCollect
     * @param id
     * @return
     */
    @Override
    public ItemsCollect findById(String id){
        return  itemsCollectMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询ItemsCollect全部数据
     * @return
     */
    @Override
    public List<ItemsCollect> findAll() {
        return itemsCollectMapper.selectAll();
    }
}
