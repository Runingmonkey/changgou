package com.changgou.interview;

/**
 * @description: 饿汉模式
 * @author: QIXIANG LING
 * @date: 2020/8/16 9:49
 */
public class SingleTonLH {
    private static SingleTonLH INSTANCE = new SingleTonLH();

    private SingleTonLH(){};

    public static SingleTonLH getInstance(){
        return INSTANCE;
    }
}
