package com.changgou.user.service.impl;

import com.changgou.user.dao.CommentsMapper;
import com.changgou.user.pojo.Comments;
import com.changgou.user.service.CommentsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/****
 * @Author:itheima
 * @Description:Comments业务层接口实现类
 *****/
@Service
public class CommentsServiceImpl implements CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;


    /**
     * Comments条件+分页查询
     * @param comments 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<Comments> findPage(Comments comments, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(comments);
        //执行搜索
        return new PageInfo<Comments>(commentsMapper.selectByExample(example));
    }

    /**
     * Comments分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Comments> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<Comments>(commentsMapper.selectAll());
    }

    /**
     * Comments条件查询
     * @param comments
     * @return
     */
    @Override
    public List<Comments> findList(Comments comments){
        //构建查询条件
        Example example = createExample(comments);
        //根据构建的条件查询数据
        return commentsMapper.selectByExample(example);
    }


    /**
     * Comments构建查询对象
     * @param comments
     * @return
     */
    public Example createExample(Comments comments){
        Example example=new Example(Comments.class);
        Example.Criteria criteria = example.createCriteria();
        if(comments!=null){
            // 
            if(!StringUtils.isEmpty(comments.getId())){
                    criteria.andEqualTo("id",comments.getId());
            }
            // 
            if(!StringUtils.isEmpty(comments.getUsername())){
                    criteria.andLike("username","%"+comments.getUsername()+"%");
            }
            // 
            if(!StringUtils.isEmpty(comments.getContent())){
                    criteria.andEqualTo("content",comments.getContent());
            }
            // 
            if(!StringUtils.isEmpty(comments.getCreatetime())){
                    criteria.andEqualTo("createtime",comments.getCreatetime());
            }
            // 
            if(!StringUtils.isEmpty(comments.getImages())){
                    criteria.andEqualTo("images",comments.getImages());
            }
            // 
            if(!StringUtils.isEmpty(comments.getCount())){
                    criteria.andEqualTo("count",comments.getCount());
            }
            // 
            if(!StringUtils.isEmpty(comments.getLevel())){
                    criteria.andEqualTo("level",comments.getLevel());
            }
            // 
            if(!StringUtils.isEmpty(comments.getScore())){
                    criteria.andEqualTo("score",comments.getScore());
            }
            // 类型，1：用户评价，2：客户回复
            if(!StringUtils.isEmpty(comments.getType())){
                    criteria.andEqualTo("type",comments.getType());
            }
            // 客服回复的评价id
            if(!StringUtils.isEmpty(comments.getPid())){
                    criteria.andEqualTo("pid",comments.getPid());
            }
            // 
            if(!StringUtils.isEmpty(comments.getSpuid())){
                    criteria.andEqualTo("spuid",comments.getSpuid());
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
        commentsMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改Comments
     * @param comments
     */
    @Override
    public void update(Comments comments){
        commentsMapper.updateByPrimaryKey(comments);
    }

    /**
     * 增加Comments
     * @param comments
     */
    @Override
    public void add(Comments comments){
        commentsMapper.insert(comments);
    }

    /**
     * 根据ID查询Comments
     * @param id
     * @return
     */
    @Override
    public Comments findById(String id){
        return  commentsMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询Comments全部数据
     * @return
     */
    @Override
    public List<Comments> findAll() {
        return commentsMapper.selectAll();
    }
}
