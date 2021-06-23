package com.changgou.seckill.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/2 17:51
 */
//@Component
public class SeckillGoodsPushTask {
    @Scheduled(cron = "0/5 * * * * ?")
    public void pushGoodsToRedis() {
        System.out.println("每5秒执行一次");
    }
}
