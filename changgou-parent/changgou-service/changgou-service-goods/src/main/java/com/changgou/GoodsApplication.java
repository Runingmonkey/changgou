package com.changgou;

import com.changgou.util.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/7/2 11:16
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.changgou.goods.dao"})
public class GoodsApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodsApplication.class, args);
    }

    /**
     * @description: 随机生成id
     * @author: QIXIANG LING
     * @date: 2020/7/3 12:14
     * @param:
     * @return: com.changgou.utils.IdWorker
     */
    @Bean
    public IdWorker idWorker(){
        return new IdWorker(0,0);
    }
}