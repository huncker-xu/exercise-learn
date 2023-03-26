package com.xz.juctest.test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/**
 * @author  Xu Zeng
 * @date  2023/3/26 21:30
 * @version 2020.3
 */
public class TestAutomic {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);
        atomicInteger.updateAndGet(value -> value * 2);
        updateAndGet(atomicInteger,p->p/2);
        System.out.println(atomicInteger);

    }

    public static void updateAndGet(AtomicInteger i, IntUnaryOperator intUnaryOperator){
        while (true){
            int prev = i.get();
            int next = intUnaryOperator.applyAsInt(prev);
            if(i.compareAndSet(prev,next)){
                break;
            }
        }
    }


}
