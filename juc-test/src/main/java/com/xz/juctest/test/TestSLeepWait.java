package com.xz.juctest.test;

import com.xz.juctest.utils.Sleeper;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test19")
public class TestSLeepWait {
    static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() ->{
            synchronized (lock) {
                log.debug("获得琐");
                try {
                    Thread.sleep(20000); //sleep是线程方法，不会释放琐;不强制和synchronized配合使用；状态都是timeWaiting
                    //lock.wait();        //wait是对象方法，进入线程旁边休息区，会释放琐;强制和synchronized配合使用；状态都是timeWaiting
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        },"t1").start();

        Sleeper.sleep(1);
        synchronized (lock){
            log.debug("获得琐");
        }

    }

}
