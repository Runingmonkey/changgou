package com.changgou.goods.dao;
import com.changgou.goods.pojo.Brand;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:itheima
 * @Description:Brand的Dao
 *****/
public interface BrandMapper extends Mapper<Brand> {

    /**
     * @description: 根据 分类id 查询品牌
     * @author: QIXIANG LING
     * @date: 2020/7/26 10:19
     * @param: id
     * @return: java.util.List<com.changgou.goods.pojo.Brand>
     */
    @Select("select * from tb_brand where id in (select  brand_id from tb_category_brand where category_id = #{id})")
    List<Brand> findByCategoryId(Integer id);
}
