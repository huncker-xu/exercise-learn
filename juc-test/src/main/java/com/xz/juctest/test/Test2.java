package com.xz.juctest.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test2 {
    public static void main(String[] args) {
        //任务
        Runnable r = () -> {
                log.debug("running");
            };
        //线程
        Thread t = new Thread(r,"t2");
        t.start();
    }
}
