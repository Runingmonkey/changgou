package com.changgou;

import com.changgou.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/2 17:49
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = "com.changgou.seckill.dao")
@EnableScheduling
public class SeckillApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class,args);
    }
    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }
}
