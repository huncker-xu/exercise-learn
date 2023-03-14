package com.xz.juctest.test;

import lombok.extern.slf4j.Slf4j;

import java.util.Vector;

@Slf4j(topic = "c.TestBiased")
public class TestBiased {
    static Thread t1,t2,t3;

    public static void test4(){
        Vector<Room> list = new Vector<>();

        int loopNumber = 39;
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < loopNumber; i++) {
                Room room = new Room();
                list.add(room);
                synchronized (room){
                    //log.debug(i+"\t"+ClassLayout);
                }
            }
        }, "t1");
        t1.start();
    }
}
