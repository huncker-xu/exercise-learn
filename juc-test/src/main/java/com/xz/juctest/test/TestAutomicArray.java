package com.xz.juctest.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/26 23:03
 */
public class TestAutomicArray {

    public static void main(String[] args) {
        demo(
                () -> new int[10],
                (array) -> array.length,
                (array,index) -> array[index]++,
                array -> System.out.println(Arrays.toString(array))
        );
        demo(
                () -> new AtomicIntegerArray(10),
                (array) -> array.length(),
                (array,index) -> array.getAndIncrement(index),
                array -> System.out.println(array)
        );
    }

    public static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T,Integer> lengthFun,
            BiConsumer<T,Integer> putConsumer,
            Consumer<T> printConsumer
    ){
        List<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        Integer lenth = lengthFun.apply(array);
        for (int i = 0; i < lenth; i++) {
            ts.add(new Thread(() ->{
                for (int j = 0; j < 10000; j++) {
                    putConsumer.accept(array,j%lenth);
                }
            }));
        }

        ts.forEach(t -> t.start());
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        printConsumer.accept(array);

    }
}
