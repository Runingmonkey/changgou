package com.changgou.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WeiXinPayService;
import com.changgou.util.HttpClient;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/1 21:30
 */
@Service
public class WeiXinPayServiceImpl implements WeiXinPayService {
    // 微信公众账号
    @Value("${weixin.appid}")
    private String appid;
    // 商户账号
    @Value("${weixin.partner}")
    private String partner;
    // 商户秘钥
    @Value("${weixin.partnerkey}")
    private String partnerkey;
    // 回调地址
    @Value("${weixin.notifyurl}")
    private String notifyurl;

    /**
     * @description:
     * @author: QIXIANG LING
     * @date: 2020/8/1 21:35
     * @param: out_trade_no 商户订单编号
     * @param: total_fee   支付金额
     * @return: java.util.Map<java.lang.String, java.lang.String>
     */
    @Override
    public Map<String, String> createNative(Map<String, String> parameterMap) {

        try {
            // 根据param 和 秘钥生成 签名
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            Map<String, String> param = new HashMap<>();
            param.put("appid",appid);
            param.put("mch_id",partner);
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            param.put("body","畅购商城测试支付");

            // 订单号 和 价格
            param.put("out_trade_no",parameterMap.get("outtradeno"));
            param.put("total_fee",parameterMap.get("totalfee"));

            // attach:附加数据,String 在查询API和支付通知中原样返回，可作为自定义参数使用。
            Map<Object, Object> attachMap = new HashMap<>();

            // 队列和交换机
            attachMap.put("queue",parameterMap.get("queue"));
            attachMap.put("exchange",parameterMap.get("exchange"));
            // 用户名;
            String username = parameterMap.get("username");
            if(!StringUtils.isEmpty(username)){
                attachMap.put("username",username);
            }

            String attach = JSON.toJSONString(attachMap);

            // 保存附加数据
            param.put("attach",attach);

            param.put("spbill_create_ip","192.168.211.1");
            param.put("notify_url",notifyurl);
            param.put("trade_type","NATIVE ");

            String paramXml = WXPayUtil.generateSignedXml(param, partnerkey);
            HttpClient httpClient = new HttpClient(url);
            httpClient.setHttps(true);
            httpClient.setXmlParam(paramXml);
            httpClient.post();

            // 获取参数
            String content = httpClient.getContent();
            Map<String, String> stringXmlMap = WXPayUtil.xmlToMap(content);
            System.out.println("getContent得到的xml文本结果: "+ stringXmlMap);

            // 获取部分页面所需参数
            Map<String, String> resultMap = new HashMap<>();
            resultMap.put("code_url",stringXmlMap.get("code_url"));
            resultMap.put("out_trade_no",stringXmlMap.get("out_trade_no"));
            resultMap.put("total_fee",stringXmlMap.get("total_fee"));

            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @description: 查询订单状态
     * @author: QIXIANG LING
     * @date: 2020/8/2 8:49
     * @param: out_trade_no
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {
        try {
            // 查询接口
            String url = "https://api.mch.weixin.qq.com/pay/orderquery";
            Map<String, String> param = new HashMap<>();
            param.put("appid",appid);
            param.put("mch_id",partner);
            param.put("out_trade_no",out_trade_no);
            param.put("nonce_str",WXPayUtil.generateNonceStr());

            String signedXml = WXPayUtil.generateSignedXml(param, partnerkey);
            HttpClient httpClient = new HttpClient(url);
            httpClient.setXmlParam(signedXml);
            httpClient.setHttps(true);
            httpClient.post();

            // 处理响应数据
            String content = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);
            return  resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
