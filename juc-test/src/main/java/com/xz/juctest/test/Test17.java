package com.xz.juctest.test;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/7 23:29
 */
@Slf4j(topic = "c.Test17")
public class Test17 {
    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("counter:{}",room.getCounter());
    }


}
class Room{
    private int counter = 0;

    public void increment(){
        synchronized (this){
            counter++;
        }
    }

    public void decrement(){
        synchronized (this){
            counter--;
        }
    }

    public int getCounter(){
        synchronized (this){
            return counter;
        }
    }
}

@Data
class Room2 extends Room{
    String str;
}


