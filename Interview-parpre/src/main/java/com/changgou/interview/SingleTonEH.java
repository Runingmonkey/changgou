package com.changgou.interview;

/**
 * @description: 懒汉模式
 * @author: QIXIANG LING
 * @date: 2020/8/16 9:54
 */
public class SingleTonEH {
    private static SingleTonEH INSTANCE = null;

    private SingleTonEH(){};

    public static SingleTonEH getInstance(){
        if (INSTANCE == null){
            SingleTonEH INSTANCE = new SingleTonEH();
        }
        return INSTANCE;
    }
}
