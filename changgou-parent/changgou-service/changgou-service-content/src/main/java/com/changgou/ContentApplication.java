package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/6/10 16:21
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.changgou.content.dao")
public class ContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContentApplication.class,args);
    }
}
