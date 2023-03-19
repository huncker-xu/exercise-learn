package com.xz.juctest.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/19 23:43
 */
@Slf4j
public class TestSequence {
    static ReentrantLock reentrantLock = new ReentrantLock();
    static Condition waitSet = reentrantLock.newCondition();

    static final Object lock = new Object();
    //表示t2是否运行过
    static boolean t2runned = false;

    //保证线程先后打印顺序
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                while (!t2runned) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            log.debug("1");
        }, "t1");

        Thread t2 = new Thread(()->{
            synchronized (lock){
                log.debug("2");
                t2runned = true;
                lock.notify();
            }
        },"t2");

//        t1.start();
//        t2.start();

        Thread t3 = new Thread(() -> {
            reentrantLock.lock();
            while (!t2runned) {
                try {
                    waitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("1");
        }, "t3");

        Thread t4 = new Thread(() -> {
            reentrantLock.lock();
            log.debug("2");
            try {
                t2runned = true;
                waitSet.signal();
            }finally {
                reentrantLock.unlock();
            }

        }, "t4");
        t3.start();
        t4.start();

    }
}
