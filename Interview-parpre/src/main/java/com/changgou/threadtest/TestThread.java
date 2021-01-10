package com.changgou.threadtest;

/**
 * @description:
 * @author: QIXIANG LING
 * @date: 2020/8/18 10:14
 */
public class TestThread {
    public static void main(String[] args) {
        Thread thread = new Thread() {
            public void run() {
                pong();
            }
        };
        thread.run();
        System.out.println("ping");
    }

    private static void pong() {
        System.out.println("pong");
    }
}
