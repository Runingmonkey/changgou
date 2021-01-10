package com.changgou.util;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * @description: 微服务互相认证的工具类, 拦截restTemplate 生成一个临时的token; 要开启信号量隔离
 * @author: QIXIANG LING
 * @date: 2020/7/29 21:03
 */
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            // 使用RequestContextHolder工具获取request相关变量
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                //取出request
                HttpServletRequest request = attributes.getRequest();
                //获取所有头文件信息的key
                Enumeration<String> headerNames = request.getHeaderNames();
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        //头文件的key
                        String name = headerNames.nextElement();
                        //头文件的value
                        String values = request.getHeader(name);
                        //将令牌数据添加到头文件中
                        requestTemplate.header(name, values);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
