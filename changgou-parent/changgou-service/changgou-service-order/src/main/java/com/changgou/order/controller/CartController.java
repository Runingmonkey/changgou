package com.changgou.order.controller;

import com.changgou.order.pojo.Order;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import com.changgou.util.TokenDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/29 11:08
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;


    /**
     * @description: 新增购物车
     * @author: QIXIANG LING
     * @date: 2020/7/29 13:30
     * @param: num
     * @param: skuid
     * @return: com.changgou.util.Result
     */
    @RequestMapping("/add")
    public Result addOrderItem(Integer num, String skuid){
//        String username = "Mochizuki";
        // 动态获取数据
        String username = TokenDecode.getUserInfo().get("username");
        cartService.addOrderItem(num,skuid,username);
        return new Result(true, StatusCode.OK,"成功");
    }

    /**
     * @description: 查询购物车
     * @author: QIXIANG LING
     * @date: 2020/7/29 13:30
     * @param:
     * @return: com.changgou.util.Result
     */
    @GetMapping("/list")
    public Result list(){
//        String username = "Mochizuki";
        String username = TokenDecode.getUserInfo().get("username");
        List<OrderItem> list = cartService.list(username);
        return new Result(true, StatusCode.OK,"成功",list);
    }


//  新增订单在 订单微服务
//    @RequestMapping("/add")
//    public Result addOrder(Order order){
//        String username = "Mochizuki";
//        // 动态获取数据
//        String username = TokenDecode.getUserInfo().get("username");
//        order.setUsername(username);
//        cartService.addOrder(order);
//        return new Result(true, StatusCode.OK,"成功");
//    }


}
