package com.xz.juctest.test;

import com.xz.juctest.utils.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.TreeMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/19 23:18
 */
@Slf4j
public class TestReentrantLock  {
    static final Object room = new Object();
    static boolean hasCigar = false;
    static boolean hasTakeout = false;
    static ReentrantLock ROOM = new ReentrantLock();
    //等待烟的休息室
    static Condition waitCigareteSet = ROOM.newCondition();
    //等待外卖的休息室
    static Condition waitTakeoutSet = ROOM.newCondition();

    public static void main(String[] args) {
        new Thread(()->{
            ROOM.lock();
            try {
                log.debug("有烟没？[{}]",hasCigar);
                while (!hasCigar){
                    log.debug("没烟，我去歇会");
                    try {
                        waitCigareteSet.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(hasCigar){
                    log.debug("可以开始干活了");
                }else {
                    log.debug("没干成活。。。");
                }
            }finally {
                ROOM.unlock();
            }
        },"小南").start();

        new Thread(() ->{
            ROOM.lock();
            log.debug("外卖送到没？[{}]",hasTakeout);
            while (!hasTakeout){
                log.debug("没外卖，我去歇会");
                try {
                    waitTakeoutSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("可以开始干活了");
        },"小女").start();

        Sleeper.sleep(1);
        new Thread(()->{
            ROOM.lock();
            try {
                hasCigar = true;
                waitCigareteSet.signal();
            }finally {
                ROOM.unlock();
            }
        },"送烟的").start();


        Sleeper.sleep(1);
        new Thread(()->{
            ROOM.lock();
            try {
                hasTakeout = true;
                waitTakeoutSet.signal();
            }finally {
                ROOM.unlock();
            }
        },"送外卖的").start();



    }
}
