package com.changgou.pay.service;

import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/1 21:30
 */
public interface WeiXinPayService {

    /**
     * @description:
     * @author: QIXIANG LING
     * @date: 2020/8/1 21:35
     * @param: out_trade_no 商户订单编号
     * @param: total_fee   支付金额
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    Map<String, String> createNative(Map<String ,String> dataMap);


    /**
     * @description: 查询订单状态; 需要订单号
     * @author: QIXIANG LING
     * @date: 2020/8/2 8:48
     * @param: out_trade_no
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    Map<String,String> queryPayStatus(String out_trade_no);

}
