package com.changgou;

import com.changgou.util.FeignInterceptor;
import com.changgou.util.IdWorker;
import com.changgou.util.TokenDecode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/29 9:38
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.changgou.goods.feign","com.changgou.user.feign"})
@MapperScan(basePackages = "com.changgou.order.dao")
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run( OrderApplication .class,args);
    }

    // 创建订单id 需要生成id
    @Bean
    public IdWorker idworker(){
        return  new IdWorker(1,1);
    }

    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }
}
