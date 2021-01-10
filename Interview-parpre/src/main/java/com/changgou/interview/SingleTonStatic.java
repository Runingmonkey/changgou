package com.changgou.interview;

/**
 * @description: 静态内部类
 * @author: QIXIANG LING
 * @date: 2020/8/16 10:00
 */
public class SingleTonStatic {
    private SingleTonStatic(){}

    private static class SingleTonHolder{
        private static SingleTonStatic INSTANCE = new SingleTonStatic();
    }

    public static SingleTonStatic getInstance(){
        return SingleTonHolder.INSTANCE;
    }

}
