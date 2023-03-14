package com.xz.juctest.test;

import com.xz.juctest.utils.Sleeper;
import lombok.extern.slf4j.Slf4j;
import sun.rmi.runtime.Log;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/14 22:22
 */
@Slf4j
public class TestWaitNotify {
    static final Object room = new Object();
    static boolean hasCigar = false;
    static boolean hasTakeout = false;

    //标准格式
//    synchronized (lock){
//        while(条件不成立){
//            lock.wait();
//        }
//        //干活
//    }
//    //另一个线程
//    synchronized(lock){
//        lock.notifyAll();
//    }

    public static void main(String[] args) {
        new Thread(()->{
            synchronized (room){
                log.debug("有烟没？[{}]",hasCigar);
                while (!hasCigar){
                    log.debug("没烟，我去歇会");
                    try {
                        room.wait();
                        log.debug("还没烟吗？[{}]",hasCigar);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没？[{}]",hasCigar);
                if(hasCigar){
                    log.debug("可以开始干活了");
                }else {
                    log.debug("没干成活。。。");
                }
            }
        },"小南").start();

        new Thread(()->{
        synchronized (room){
            log.debug("有外卖没？[{}]",hasTakeout);
            while (!hasTakeout){
                log.debug("没烟，我去歇会");
                try {
                    room.wait();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("有外卖没？[{}]",hasTakeout);
            if(hasTakeout){
                log.debug("可以开始干活了");
            }else {
                log.debug("没干成活。。。");
            }
        }
    },"小女").start();

        Sleeper.sleep(1);

        new Thread(()->{
            synchronized (room){
                hasTakeout = true;
                log.debug("外卖到了");
                room.notifyAll();
            }
        },"送外卖的").start();
}
}
