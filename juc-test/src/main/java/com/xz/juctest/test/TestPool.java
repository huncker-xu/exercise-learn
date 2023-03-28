package com.xz.juctest.test;

import lombok.extern.slf4j.Slf4j;

import java.net.ConnectException;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.locks.Condition;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/28 22:05
 */
public class TestPool {
    public static void main(String[] args) {
        Pool pool = new Pool(2);
        for (int i = 0; i < 5; i++) {
            new Thread(() ->{
                Condition conn = pool.borrow();
                try {
                    Thread.sleep(new Random().nextInt(1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pool.free(conn);
            }).start();
        }
    }
}

@Slf4j
class Pool{
    //连接池大小
    private final int poolSize;
    //连接对象数组
    private Condition[] conditions;

    //0空闲，1繁忙
    private AtomicIntegerArray states;

    public Pool(int poolSize){
        this.poolSize = poolSize;
        this.conditions = new Condition[poolSize];
        this.states = new AtomicIntegerArray(new int[poolSize]);
        for (int i = 0; i < poolSize; i++) {
            conditions[i] = new MockConnection("连接"+i);
        }
    }

    //借连接
    public Condition borrow(){
        while (true){
            for (int i = 0; i < poolSize; i++) {
                //获取空闲连接
                if(states.get(i) == 0){
                    if(states.compareAndSet(i,0,1)) {
                        log.debug("borrow {}",conditions[i]);
                        return conditions[i];
                    }
                }
            }
            //没有空闲连接,当前线程等待
            synchronized (this){
                try {
                    log.debug("wait...");
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    //归还连接
    public void free(Condition conn){
        for (int i = 0; i < poolSize; i++) {
            if(conditions[i] == conn){
                //归还连接时候，就是连接的持有者
                states.set(i,0);
                synchronized (this){
                    log.debug("free {}",conn);
                    this.notifyAll();
                }
                break;
            }
        }

    }


}

class MockConnection implements Condition{
    private String name;

    public MockConnection(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MockConnection{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public void await() throws InterruptedException {

    }

    @Override
    public void awaitUninterruptibly() {

    }

    @Override
    public long awaitNanos(long nanosTimeout) throws InterruptedException {
        return 0;
    }

    @Override
    public boolean await(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public boolean awaitUntil(Date deadline) throws InterruptedException {
        return false;
    }

    @Override
    public void signal() {

    }

    @Override
    public void signalAll() {

    }
}