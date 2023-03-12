package com.xz.juctest.utils;

import java.util.concurrent.TimeUnit;

public class Sleeper {
    public static void sleep(int i){
        try {
            TimeUnit.SECONDS.sleep(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sleep(double i){
        try {
            TimeUnit.MICROSECONDS.sleep((int)(i*1000));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
