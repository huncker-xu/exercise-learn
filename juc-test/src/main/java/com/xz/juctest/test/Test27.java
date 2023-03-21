package com.xz.juctest.test;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/21 22:24
 */
@Slf4j
public class Test27 {
    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1, 5);
        new Thread(()->{
            waitNotify.print("a",1,2);
        }).start();
        new Thread(()->{
            waitNotify.print("b",2,3);
        }).start();
        new Thread(()->{
            waitNotify.print("c",3,1);
        }).start();

    }
}

class WaitNotify{
    //打印
    public void print(String str,int waitFlag,int nextFlag){
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this){
                while (flag != waitFlag){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }

    }

    //等待标记
    private int flag;
    //循环次数
    private int loopNumber;

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }
}
