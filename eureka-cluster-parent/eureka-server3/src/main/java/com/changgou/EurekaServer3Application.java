package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/9 20:04
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServer3Application {
    public static void main(String[] args) {
        SpringApplication.run( EurekaServer3Application.class,args);
    }
}
