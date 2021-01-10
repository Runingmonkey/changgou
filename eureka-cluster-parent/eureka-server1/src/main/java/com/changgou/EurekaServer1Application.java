package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/9 20:02
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer1Application {
    public static void main(String[] args) {
        SpringApplication.run( EurekaServer1Application.class,args);
    }
}
