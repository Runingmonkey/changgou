package com.changgou.seckill.dao;
import com.changgou.seckill.pojo.SeckillGoods;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/****
 * @Author:itheima
 * @Description:SeckillGoods的Dao
 *****/
public interface SeckillGoodsMapper extends Mapper<SeckillGoods> {

    @Select("select * from tb_seckill_goods WHERE start_time > #{startTime} and end_time < #{endTime} and status = 1 and stock_count > 0")
    List<SeckillGoods> findGoodsByTime(SeckillGoods seckillGood);
}
