package com.changgou.pay.controller;

import com.alibaba.fastjson.JSON;
import com.changgou.pay.service.WeiXinPayService;
import com.changgou.util.Result;
import com.changgou.util.StatusCode;
import com.github.wxpay.sdk.WXPayUtil;
import com.netflix.discovery.converters.Auto;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/1 22:04
 */
@RestController
@RequestMapping("/weixin/pay")
@CrossOrigin
public class WeixinPayController {

    @Autowired
    private WeiXinPayService weiXinPayService;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    /**
     * @description: 生成二维码
     * @author: QIXIANG LING
     * @date: 2020/8/1 22:09
     * @param: outtradeno  订单号
     * @param: totalfee    支付金额
     * @return: com.changgou.util.Result
     */
    @PostMapping("/create/native")
    public Result craeteNative(@RequestParam Map<String,String> dataMap){
        Map<String, String> map = weiXinPayService.createNative(dataMap);
        return new Result<>(true, StatusCode.OK,"创建二维码成功",map);
    }

    /**
     * @description: 查询订单状态
     * @author: QIXIANG LING
     * @date: 2020/8/2 9:13
     * @param: outtradeno
     * @return: com.changgou.util.Result
     */
    @RequestMapping("/status/query")
    public Result queryStatus(String outtradeno){
        Map<String, String> map = weiXinPayService.queryPayStatus(outtradeno);
        return new Result<>(true, StatusCode.OK,"查询订单状态成功",map);
    }

    /**
     * @description: 响应微信
     * @author: QIXIANG LING
     * @date: 2020/8/2 11:25
     * @param: request
     * @return: java.lang.String
     */
    @RequestMapping("/notify/url")
    public String notifyUrl(HttpServletRequest request) throws Exception {
        // 获取微信回调信息
        ServletInputStream inputStream = request.getInputStream();
        // 网络传输的字节流操作
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 定义缓冲区
        byte[] bytes = new byte[1024];
        int len = 0;
        while((len=inputStream.read(bytes)) != -1){
            byteArrayOutputStream.write(bytes,0,len);
        }
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();

        // 获取数据
        String strXml = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
        Map<String, String> map = WXPayUtil.xmlToMap(strXml);

        Map responseMap = new HashMap();
        // 文档中查看所需返回 参数
        responseMap.put("return_code","SUCCESS");
        responseMap.put("   return_msg","OK");

        // 接收到微信的 回调数据之后把其发送到mq 里面去
//        System.out.println("exchange: exchange.order");
//        System.out.println("routingkey: queue.order" );
        Map<String,String> attach = JSON.parseObject(map.get("attach"), Map.class);
        // 绑定的时候设置了 路由的名称 和 队列名称是 一致的   坑: 注意这里要把数据转化成JSON
        rabbitTemplate.convertAndSend(attach.get("exchange"),attach.get("queue"),JSON.toJSONString(map));

        return WXPayUtil.mapToXml(responseMap);
    }
}
