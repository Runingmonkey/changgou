package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.dao.OrderItemMapper;
import com.changgou.order.dao.OrderMapper;
import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import com.changgou.user.feign.UserFeign;
import com.changgou.util.IdWorker;
import com.changgou.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/29 10:43
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SpuFeign spuFeign;

    @Autowired
    private UserFeign userFeign;

    @Autowired
    private CartService cartService;

    @Autowired
    private IdWorker idworker;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * @description: 查询用户购物车数据
     * @author: QIXIANG LING
     * @date: 2020/7/29 11:33
     * @param: username
     * @return: java.util.List<com.changgou.order.pojo.OrderItem>
     */
    @Override
    public List<OrderItem> list(String username) {
        List<OrderItem> o1rderItemList = null;
        if(null != username && username.length() > 0){
            o1rderItemList = redisTemplate.boundHashOps("cart_" + username).values();
        }
        return o1rderItemList;
    }

    /**
     * @description: 新增订单
     * @author: QIXIANG LING
     * @date: 2020/7/30 20:02
     * @param: order
     * @return: void
     */
    @Override
    public Order addOrder(Order order) {
        // 获取 购物车数据
        List<OrderItem> orderItems = cartService.list(order.getUsername());

        // 保存订单数据
        long id = idworker.nextId();
        order.setId("NO"+ String.valueOf(id));  // 主键
        int totalNum = 0;
        int totalMoney = 0;       // 总金额
        int totalPayMoney = 0;    // 优惠金额
        for (OrderItem orderItem : orderItems) {
            totalNum += orderItem.getNum();
            totalMoney += orderItem.getMoney();
            totalPayMoney += orderItem.getPayMoney();
        }

        order.setTotalNum(totalNum);
        order.setTotalMoney(totalMoney);
        order.setPayMoney(totalPayMoney);
        order.setPreMoney(totalMoney - totalPayMoney);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setBuyerRate("0");   // 是否评价
        order.setSourceType("1");  // 订单来源
        order.setOrderStatus("0");
        order.setPayStatus("0");   // 支付状态
        order.setConsignStatus("0");  // 是否发货

        // 保存订单
        orderMapper.insertSelective(order);

        // 保存订单明细
        for (OrderItem orderItem : orderItems) {
            orderItem.setId("NO" + idworker.nextId());
            orderItem.setOrderId(order.getId());
            orderItemMapper.insertSelective(orderItem);
        }

        //  feign 调用 商品微服务 ; 扣减 库存 ; 需要 传用户 username
        skuFeign.decrSkuNum(order.getUsername());

        // 增加 用户积分; 微服务之间进行调用 需要 feign调用之前拦截; 把token 写入请求头
        userFeign.addUserPoint(10);

        // 支付成功之后把订单信息保存到redis ;
        redisTemplate.boundHashOps("Order").put(order.getId(), order);

//         删除购物车数据
//        redisTemplate.delete("cart_"+ order.getUsername());
        return  order;
    }

    /**
     * @description: 用户添加商品到购物车
     * @author: QIXIANG LING
     * @date: 2020/7/30 20:00
     * @param: num
     * @param: skuid
     * @param: username
     * @return: void
     */
    @Override
    public void addOrderItem(Integer num, String skuid, String username) {
        // 如果传入的num 小于 0 就根据 skuid 删除 username的 购物车数据
        if (num == null) {
            redisTemplate.boundHashOps("cart_" + username).delete(skuid);
        }

        // 查询 sku
        Result<Sku> result = skuFeign.findById(skuid);
        Sku sku = result.getData();


        // 封装数据, 把订单数据保存到 redis 中
        if (sku != null) {
            // 查询spu
            String spuId = sku.getSpuId();
            Spu spu = spuFeign.findById(spuId).getData();
            OrderItem orderitem = goods2OrderItem(spu, sku, num);
            // 保存到 redis中
            redisTemplate.boundHashOps("cart_"+ username).put(skuid, orderitem);
        }
    }

    /**
     * @description: 封装 订单详情 数据 保存到 用户购物车 (redis)
     * @author: QIXIANG LING
     * @date: 2020/7/29 10:50
     * @param: spu
     * @param: sku
     * @param: num
     * @return: Orderitem
     */
    private OrderItem goods2OrderItem(Spu spu, Sku sku, Integer num) {
        OrderItem orderItem = new OrderItem();
        IdWorker idWorker = new IdWorker();
        long orderItemId = idWorker.nextId();
        // 主键
        orderItem.setId("NO" + String.valueOf(orderItemId));
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSpuId(spu.getId());
        orderItem.setSkuId(sku.getId());
        orderItem.setName(sku.getName());           // 商品名称
        orderItem.setPrice(sku.getPrice());         // 商品单价
        orderItem.setNum(num);                      // 购买数量
        orderItem.setMoney(sku.getPrice() * num);   // 总金额
        orderItem.setPayMoney(sku.getPrice() * num);// 实付金额 = 总金额 - 优惠券 - e卡等
        orderItem.setImage(sku.getImage());         // 商品图片
        orderItem.setWeight(sku.getWeight());       // 商品重量
        orderItem.setPostFee(9);                    // 商品运费 = 总金额判断是否超过 99   金牌会员
        orderItem.setIsReturn("0");                 // 商品的退货状态
        return orderItem;
    }

}
