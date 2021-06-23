package com.changgou.seckill.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.seckill.dao.SeckillGoodsMapper;
import com.changgou.seckill.dao.SeckillOrderMapper;
import com.changgou.seckill.pojo.SeckillGoods;
import com.changgou.seckill.pojo.SeckillOrder;
import com.changgou.seckill.pojo.SeckillStatus;
import com.changgou.seckill.service.SeckillOrderService;
import com.changgou.util.IdWorker;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.netflix.discovery.converters.Auto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/****
 * @Author:itheima
 * @Description:SeckillOrder业务层接口实现类
 *****/
@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * @description: 用户下单 失败后; 关闭订单
     * @author: QIXIANG LING
     * @date: 2020/8/6 16:20
     * @param: String
     * @return: void
     */
    @Override
    public void closeOrder(String username) {
        // 用户抢单状态
        SeckillStatus seckillStatus = JSON.parseObject(stringRedisTemplate.boundValueOps("SeckillStatus_" + username).get(), SeckillStatus.class);

        // 回滚秒杀商品库存;
        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + seckillStatus.getTime()).get(seckillStatus.getGoodsId());

        // 为什么要设置为空?? 或者直接写sql 去吧对应的商品库存加 1

        // 商品售罄  ; 回滚数据
        if (seckillGoods == null) {
            // 回滚数据 ; 这里是因为 商品售罄的时候同步到了 数据库 ; 支付失败 商品库存回滚的时候需要去 把数据库的库存加1
            seckillGoods = seckillGoodsMapper.selectByPrimaryKey(seckillStatus.getGoodsId());
            seckillGoods.setStockCount(seckillGoods.getStockCount() + 1 );
            // 同步到数据库
            seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
        }else{
            // 未售罄
            // 回滚队列 (重新压入一条数据)
            redisTemplate
                    .boundListOps("SeckillGoodsCountList_" + seckillStatus.getGoodsId())
                    .leftPush(seckillStatus.getGoodsId());   // 压入一条数据就行 了 ; 管它是什么

            // 计数器 加1
            redisTemplate
                    .boundHashOps("SeckillGoodsCount")
                    .increment(seckillStatus.getGoodsId(),1);
        }

        // 删除redis 中的订单
        redisTemplate.boundHashOps("SeckillOrder").delete(seckillStatus.getUsername());


    }

    /**
     * @description: 修改订单状态
     * @author: QIXIANG LING
     * @date: 2020/8/6 14:39
     * @param: username
     * @param: transactionId
     * @return: void
     */
    @Override
    public void updateOSeckillOrderStatus(String username, String transactionId) {
        // redis 中查询订单信息
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.boundHashOps("SeckillOrder").get(username);
        // 完善订单信息 (流水号; 支付时间 ,状态)
        seckillOrder.setTransactionId(transactionId);
        seckillOrder.setPayTime(new Date());
        seckillOrder.setStatus("1");

        // 同步到 mysql 数据库
        seckillOrderMapper.insertSelective(seckillOrder);

        // 删除redis 中的订单
        redisTemplate.boundHashOps("SeckillOrder").delete(username);

        // 修改抢单的状态; 已支付
        SeckillStatus seckillStatus = JSON.parseObject(stringRedisTemplate.boundValueOps("SeckillStatus_" + username).get(),SeckillStatus.class);
        if (seckillStatus != null) {
            seckillStatus.setStatus(4);
        }
        stringRedisTemplate.boundValueOps("SeckillStatus_" +username).set(JSON.toJSONString(seckillStatus));

        // 更新用户状态 (计数器 ; 抢单详情)
        clearSeckillStatus(username);
    }

    /**
     * @description: 清理掉用户排队的信息
     * @author: QIXIANG LING
     * @date: 2020/8/6 8:35
     * @param: pop
     * @return: void
     */
    private void clearSeckillStatus(String username) {
        // 计数器
        redisTemplate.delete("UserQueueCount_" + username);
        // 抢单信息
        stringRedisTemplate.delete("SeckillStatus_" + username);
    }

    /**
     * @description:  用户抢单; 把抢单状态  存入了redis 和mq 中
     * @author: QIXIANG LING
     * @date: 2020/8/4 17:05
     * @param: time
     * @param: seckillId
     * @param: userId
     * @return: java.lang.Boolean
     */
    @Override
    public Boolean add(String time, String seckillId, String userId) {
//        // 从redis1 中获取 秒杀商品的信息
//        SeckillGoods seckillGoods = (SeckillGoods) redisTemplate.boundHashOps("SeckillGoods_" + time).get(seckillId);
//        // 判断是否还有库存
//        Integer stockCount = seckillGoods.getStockCount();
//        if (stockCount == null) {
//            throw new RuntimeException("兄弟, 手速不够快 ,下次再来吧");
//        }
//        // 还有库存; 就进行下单; 把订单信息保存到 redis中
//        SeckillOrder seckillOrder = new SeckillOrder();
//        // 主键
//        seckillOrder.setId("NO"+ idWorker.nextId());
//        // 设置秒杀商品id
//        seckillOrder.setSeckillId(seckillId);
//        seckillOrder.setUserId(userId);
//        seckillOrder.setCreateTime(new Date());
////        seckillOrder.setStatus("0");
////        //设置价格
//        Double costPrice = seckillGoods.getCostPrice();
//        String price = String.valueOf(costPrice);
//        seckillOrder.setMoney(price);
//
//        // 将秒杀 商品订单存到 redis中
//        redisTemplate.boundHashOps("SeckillOrder").put(userId,seckillOrder);
//
//        // 扣减秒杀商品库存
//        seckillGoods.setStockCount(seckillGoods.getStockCount() -1 );
//        if (seckillGoods.getStockCount() < 0) {
//            // 商品库存小于0 ; 删除redis 中 秒杀商品
//            redisTemplate.boundHashOps("SeckillGoods" + time).delete(seckillId);
//            // 同步数据到数据库; 这样这个秒杀商品就不会再继续压入Redis
//            seckillGoodsMapper.updateByPrimaryKey(seckillGoods);
//        }else{
//            // 不小于0 ; 更新库存   put(seckillId , seckillGoods);  秒杀商品id ;  秒杀商品详情
//            redisTemplate.boundHashOps("SeckillGoods_" + time).put(seckillId , seckillGoods);
//        }
//        return true;
        // 封装抢单信息
        // 设置用户抢单基本信息 ; 用户id 和 商品id
        SeckillStatus seckillStatus = new SeckillStatus();
        seckillStatus.setUsername(userId);
        seckillStatus.setCreateTime(new Date());
        seckillStatus.setGoodsId(seckillId);
        seckillStatus.setStatus(1);
        // 秒杀商品商品所在的时间段
        seckillStatus.setTime(time);

        // 状态信息
        String statusJson = JSON.toJSONString(seckillStatus);

        // 队列削峰

        // 设置一个计数器 ; 每次用户排队就会进行 + 1 操作 ; 防止重复排队 ; 一次只能抢购一件秒杀商品
        // 可以抢购多个商品; 不能对同一款重复下单 就用redis的 hash 计数器
        //  redisTemplate.boundHashOps("UserSeckillOrderQueue_" + userId).increment(goodsId, 1);
        Long increment = redisTemplate.boundValueOps("UserQueueCount_" + userId).increment(1);
        // 如果计数器大于1 则不允许通过
        if (increment > 1) {
            throw new RuntimeException("100");
        }

        // 发送 抢单信息到mq
        rabbitTemplate.convertAndSend("seckillOrderExchange","seckillOrderQueue",statusJson);

        // 把抢单信息  存到Redis中
        // 数据类型
        String key = "SeckillStatus_" + userId;
        // 要使用lua 脚本 从 redis 中取数据 的话 ; 不能使用redisTemplate ( key 前面有一段hash串)
        stringRedisTemplate.boundValueOps(key).set(statusJson);
        return true;
    }

    /**
     * SeckillOrder条件+分页查询
     * @param seckillOrder 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public PageInfo<SeckillOrder> findPage(SeckillOrder seckillOrder, int page, int size){
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构建
        Example example = createExample(seckillOrder);
        //执行搜索
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectByExample(example));
    }

    /**
     * SeckillOrder分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<SeckillOrder> findPage(int page, int size){
        //静态分页
        PageHelper.startPage(page,size);
        //分页查询
        return new PageInfo<SeckillOrder>(seckillOrderMapper.selectAll());
    }

    /**
     * SeckillOrder条件查询
     * @param seckillOrder
     * @return
     */
    @Override
    public List<SeckillOrder> findList(SeckillOrder seckillOrder){
        //构建查询条件
        Example example = createExample(seckillOrder);
        //根据构建的条件查询数据
        return seckillOrderMapper.selectByExample(example);
    }


    /**
     * SeckillOrder构建查询对象
     * @param seckillOrder
     * @return
     */
    public Example createExample(SeckillOrder seckillOrder){
        Example example=new Example(SeckillOrder.class);
        Example.Criteria criteria = example.createCriteria();
        if(seckillOrder!=null){
            // 主键
            if(!StringUtils.isEmpty(seckillOrder.getId())){
                criteria.andEqualTo("id",seckillOrder.getId());
            }
            // 秒杀商品ID
            if(!StringUtils.isEmpty(seckillOrder.getSeckillId())){
                criteria.andEqualTo("seckillId",seckillOrder.getSeckillId());
            }
            // 支付金额
            if(!StringUtils.isEmpty(seckillOrder.getMoney())){
                criteria.andEqualTo("money",seckillOrder.getMoney());
            }
            // 用户
            if(!StringUtils.isEmpty(seckillOrder.getUserId())){
                criteria.andEqualTo("userId",seckillOrder.getUserId());
            }
            // 创建时间
            if(!StringUtils.isEmpty(seckillOrder.getCreateTime())){
                criteria.andEqualTo("createTime",seckillOrder.getCreateTime());
            }
            // 支付时间
            if(!StringUtils.isEmpty(seckillOrder.getPayTime())){
                criteria.andEqualTo("payTime",seckillOrder.getPayTime());
            }
            // 状态，0未支付，1已支付
            if(!StringUtils.isEmpty(seckillOrder.getStatus())){
                criteria.andEqualTo("status",seckillOrder.getStatus());
            }
            // 收货人地址
            if(!StringUtils.isEmpty(seckillOrder.getReceiverAddress())){
                criteria.andEqualTo("receiverAddress",seckillOrder.getReceiverAddress());
            }
            // 收货人电话
            if(!StringUtils.isEmpty(seckillOrder.getReceiverMobile())){
                criteria.andEqualTo("receiverMobile",seckillOrder.getReceiverMobile());
            }
            // 收货人
            if(!StringUtils.isEmpty(seckillOrder.getReceiver())){
                criteria.andEqualTo("receiver",seckillOrder.getReceiver());
            }
            // 交易流水
            if(!StringUtils.isEmpty(seckillOrder.getTransactionId())){
                criteria.andEqualTo("transactionId",seckillOrder.getTransactionId());
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
        seckillOrderMapper.deleteByPrimaryKey(id);
    }

    /**
     * 修改SeckillOrder
     * @param seckillOrder
     */
    @Override
    public void update(SeckillOrder seckillOrder){
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /**
     * 增加SeckillOrder
     * @param seckillOrder
     */
//    @Override
//    public void add(SeckillOrder seckillOrder){
//        seckillOrderMapper.insert(seckillOrder);
//    }

    /**
     * 根据ID查询SeckillOrder
     * @param id
     * @return
     */
    @Override
    public SeckillOrder findById(String id){
        return  seckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询SeckillOrder全部数据
     * @return
     */
    @Override
    public List<SeckillOrder> findAll() {
        return seckillOrderMapper.selectAll();
    }
}
