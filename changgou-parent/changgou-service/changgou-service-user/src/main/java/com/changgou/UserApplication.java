package com.changgou;

import com.changgou.util.FeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/24 13:21
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.changgou.user.dao")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run( UserApplication.class,args);
    }

    @Bean
    public FeignInterceptor feignInterceptor(){
        return new FeignInterceptor();
    }
}
