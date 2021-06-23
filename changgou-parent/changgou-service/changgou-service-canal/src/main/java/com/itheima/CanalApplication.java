package com.itheima;

import com.xpand.starter.canal.annotation.EnableCanalClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description: 使用canal 实现 广告缓存的同步
 * @author: QIXIANG LING
 * @date: 2020/7/12 17:52
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})  // 不需要加载数据库的配置
@EnableEurekaClient
@EnableCanalClient
@EnableFeignClients(basePackages = "com.changgou.content.feign")
public class CanalApplication {
    public static void main(String[] args) {
        SpringApplication.run( CanalApplication.class,args);
    }
}
