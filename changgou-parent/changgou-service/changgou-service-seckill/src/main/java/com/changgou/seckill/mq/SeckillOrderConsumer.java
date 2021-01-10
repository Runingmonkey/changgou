package com.changgou.seckill.mq;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.util.IdWorker;
import com.netflix.discovery.converters.Auto;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @description: 监听到抢单信息; 符合条件就生成订单; 修改抢单状态
 * @author: QIXIANG LING
 * @date: 2020/8/5 9:21
 */
@Component
@RabbitListener(queues = "seckillOrderQueue")
public class SeckillOrderConsumer {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private IdWorker idWorker;

    @RabbitHandler
    public void readOrderMessage(String message){
        // 获取抢单信息
        SeckillStatus seckillStatus = JSON.parseObject(message, SeckillStatus.class);

        // 是否有未支付的订单;  我们会在 生成抢单信息的时候就会 排除掉用户重复排队
//        List userSeckillOrder = redisTemplate.boundHashOps("SeckillOrder").values();
//        if (userSeckillOrder != null && userSeckillOrder.size() > 0) {
//            System.out.println("存在订单还未支付");
//            return;
//        }

        // 商品是否有库存
        // 用户抢单 ; 需要从 List 中弹出一个商品
        Object pop = redisTemplate.boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId()).rightPop();
        if (pop == null) {
            // 如果redis 列表为空 ; 则说明商品已经售罄
            clearSeckillStatus(seckillStatus);
            return;
        }

        // 获取时间段内的商品数据
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).get(seckillStatus.getGoodsId());

        // 商品不为空 ; 创建订单
        if (seckillGoods != null && seckillGoods.getStockCount() > 0) {
            // 创建订单对象
            SeckillOrder seckillOrder = new SeckillOrder();
            // 主键
            seckillOrder.setId("NO"+ idWorker.nextId());
            // 设置秒杀商品id
            seckillOrder.setSeckillId(seckillStatus.getGoodsId());
            seckillOrder.setUserId(seckillStatus.getUsername());
            seckillOrder.setCreateTime(new Date());
            Double money = seckillGoods.getPrice();
            String price = String.valueOf(money);
            seckillOrder.setMoney(price);
            seckillOrder.setStatus("0");

            // 把订单数据存入到redis 中
            redisTemplate.boundHashOps("SeckillOrder").put(seckillStatus.getUsername(),seckillOrder);

            // 库存递减
//            seckillGoods.setStockCount(seckillGoods.getStockCount() -1 );

            // 计数器递减 ; 更新Redis 中的商品库存
            Long count = redisTemplate.boundHashOps("SeckillGoodsCount").increment(seckillStatus.getGoodsId(), -1);
            // 售罄处理; 如果计数器为 0 ; 防止重复压入数据库; 需要售罄的信息同步到数据库
            if (count == 0) {
                // 从redis 中移除
                redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).delete();
                // 同步到数据库
                seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
            }else{
                // 设置redis中商品的剩余库存
                seckillGoods.setStockCount(count.intValue());

                // 更新redis 中 库存商品
                redisTemplate
                        .boundHashOps("SeckillGoods_" + seckillStatus.getTime())
                        .put(seckillStatus.getGoodsId() ,seckillGoods);
            }

            // 更新下单详情数据 statusStatus
            seckillStatus.setStatus(2);
            seckillStatus.setOrderId(seckillOrder.getId());
            // 设置订单详情 金额
            seckillStatus.setMoney(Float.valueOf(seckillOrder.getMoney()));

            //  redis 中更新下单状态
            //  数据类型 , string 下单状态+用户名  : seckillStatus 下单状态详情
            // JSON.toJSONString(seckillStatus)
            stringRedisTemplate.boundValueOps("SeckillStatus_" + seckillStatus.getUsername()).set(JSON.toJSONString(seckillStatus));

        }
    }

    /**
     * @description: 清理掉用户排队的信息 ()
     * @author: QIXIANG LING
     * @date: 2020/8/6 8:35
     * @param: pop
     * @return: void
     */
    private void clearSeckillStatus(SeckillStatus seckillStatus) {
        // 计数器
        redisTemplate.delete("UserQueueCount_" + seckillStatus.getUsername());
        // 抢单信息
        stringRedisTemplate.delete("SeckillStatus_" + seckillStatus.getUsername());
    }
}
