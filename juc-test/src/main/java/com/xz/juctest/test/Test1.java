package com.xz.juctest.test;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test2")
public class Test1 {
    public static void main(String[] args) {

        Thread t = new Thread(){
            @Override
            public void run() {
                log.debug("runnning");
            }
        };
        t.setName("t1");
        t.start();
        log.debug("running");
    }
}
