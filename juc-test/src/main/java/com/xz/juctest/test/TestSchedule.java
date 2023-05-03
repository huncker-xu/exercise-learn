package com.xz.juctest.test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TestSchedule {

    //让每周四18点定时执行任务
    public static void main(String[] args) {
        //当前时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        //周四时间
        LocalDateTime time = now.withHour(17).withMinute(25).withSecond(0).withNano(0).with(DayOfWeek.TUESDAY);
        System.out.println(time);
        //如果当前时间大于本周周四，必须找到下周周四
        if(now.compareTo(time) > 0){
            time = time.plusWeeks(1);
        }

        System.out.println(Duration.between(now, time).toMillis());
        long initailDelay  = Duration.between(now, time).toMillis();
        long period = 1000 * 60 * 60 * 24 * 7;
        //initailDelay 代表当前时间和周四的时间差
        //period 一周的时间间隔
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(()->{
            System.out.println("running");
        },initailDelay,period, TimeUnit.MILLISECONDS);
    }
}
