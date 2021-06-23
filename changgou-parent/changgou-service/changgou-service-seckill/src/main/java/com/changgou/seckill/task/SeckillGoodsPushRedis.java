package com.changgou.seckill.task;

import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @description:  把秒杀商品数据存入 redis 中
 * @author: QIXIANG LING
 * @date: 2020/8/2 19:12
 */
@Component
public class SeckillGoodsPushRedis {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Scheduled(cron = "0/15 * * * * ?")
    public void pushGoodsToRedis(){
//        // 获取当前时间段 以及之后的 4 个时间段
//        List<Date> dateMenus = DateUtil.getDateMenus();
//        // 将该 5 个时间段 中的商品 压入redis 中
//        for (Date startTime : dateMenus) {
//            // 将日期转换成String
//            String start = DateUtil.data2str(startTime, DateUtil.PATTERN_YYYYMMDDHH);
//            Date endTime = DateUtil.addDateHour(startTime, 2);
//            Example example = new Example(SeckillGoods.class);
//            Example.Criteria criteria = example.createCriteria();
//            // 商品库存大于0
//            criteria.andGreaterThan("stockCount",0);
//            // 商品状态已上架
//            criteria.andEqualTo("status","1");
//            // 起始时间 大于 当前时间段的
//            criteria.andGreaterThanOrEqualTo("startTime",startTime);
//            // 结束时间 小于 时间段
//            criteria.andLessThanOrEqualTo("endTime",endTime);
//
//            //  根据 时间段查询
////            SeckillGoods seckillGoo = new SeckillGoods();
////            seckillGoo.setCreateTime(dateMenu);
////            seckillGoo.setEndTime(DateUtil.addDateHour(dateMenu,2));
//            // 已经写入的商品 不需要重复写入
//            Set ids = redisTemplate.boundHashOps("SeckillGoods_" + start).keys();
//            // 这里必须要做非空判断 ; 因为 如果 ids 为 null
//            if (ids != null && ids.size() > 0) {
//                criteria.andNotIn("id",ids);
//            }
//
//            // 根据条件获取秒杀商品列表
//             List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);
//            if (seckillGoods != null) {
//                for (SeckillGoods seckillGood : seckillGoods) {
//                    redisTemplate.boundHashOps("SeckillGoods_" + start ).put(seckillGood.getId(),seckillGood);
//                }
//            }
//        }

        //获取当前时间所在的对应的5个时间段
        List<Date> dateMenus = DateUtil.getDateMenus();
        for (Date startTime : dateMenus) {
            //获取结束时间
            Date endTime = DateUtil.addDateHour(startTime, 2);
            //日期格式转换
            String start = DateUtil.data2str(startTime, DateUtil.PATTERN_YYYY_MM_DDHHMM);
            String end = DateUtil.data2str(endTime, DateUtil.PATTERN_YYYY_MM_DDHHMM);

            //拼接查询的条件
            Example example = new Example(SeckillGoods.class);
            Example.Criteria criteria = example.createCriteria();
            //状态为审核通过的
            criteria.andEqualTo("status", "1");
            //库存大于0
            criteria.andGreaterThan("stockCount" , 0);
            //开始时间
            criteria.andGreaterThanOrEqualTo("startTime" , start);
            //结束时间
            criteria.andLessThanOrEqualTo("endTime", end);
            //redis中获取所有的商品的id ; 防止重复压入
            Set keys = redisTemplate.boundHashOps("SeckillGoods_" + DateUtil.data2str(startTime, DateUtil.PATTERN_YYYYMMDDHH)).keys();
            //非空判断
            if(keys != null && keys.size() > 0) {
                criteria.andNotIn("id", keys);
            }
            //查询数据
            List<SeckillGoods> seckillGoods = seckillGoodsMapper.selectByExample(example);
            // 秒杀商品数据存入redis
            for (SeckillGoods seckillGood : seckillGoods) {
                // 解决超卖问题 ; 需要把商品存入redis 数组 ;key 可以使任意; 只要长度和 商品库存一致就行了
                String[] ids = pushIds(seckillGood.getStockCount(), seckillGood.getId());
                redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillGood.getId()).leftPushAll(ids);

                // 自增计数器; 展示正确的库存数据 ; 设置初始的大小 为 商品的库存
                redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillGood.getId(),seckillGood.getStockCount());

                //  将商品数据压入redis
                redisTemplate.boundHashOps("SeckillGoods_" + DateUtil.data2str(startTime, DateUtil.PATTERN_YYYYMMDDHH)).put(seckillGood.getId(), seckillGood);
                System.out.println(seckillGood.getId());
            }
        }

    }

    /***
     * 将商品ID存入到数组中
     * @param len:长度
     * @param id :值
     * @return
     */
    public String[] pushIds(int len,String id){
        String[] ids = new String[len];
        for (int i = 0; i <ids.length ; i++) {
            ids[i]=id;
        }
        return ids;
    }
}
