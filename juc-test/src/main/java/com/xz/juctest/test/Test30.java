package com.xz.juctest.test;

import netscape.javascript.JSUtil;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/21 23:07
 */
public class Test30 {
    public static void main(String[] args) {
        AwaitRoom awaitRoom = new AwaitRoom(5);
        Condition condition = awaitRoom.newCondition();
        Condition condition1 = awaitRoom.newCondition();
        Condition condition2 = awaitRoom.newCondition();
        new Thread(()->{
            awaitRoom.print("a",condition,condition1);
        }).start();
        new Thread(()->{
            awaitRoom.print("b",condition1,condition2);
        }).start();
        new Thread(()->{
            awaitRoom.print("c",condition2,condition);
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        awaitRoom.lock();
        try {
            System.out.println("开始...");
            condition.signal();
        }finally {
            awaitRoom.unlock();
        }

    }
}

class AwaitRoom extends ReentrantLock{
    private int loopNumber;

    public AwaitRoom(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Condition current,Condition next){
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                current.await();
                System.out.println(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                unlock();
            }
        }
    }

}
