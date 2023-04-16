package com.xz.juctest.test;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
public class TestExecutors {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        ExecutorService pool = Executors.newSingleThreadExecutor();
//        pool.execute(() ->{
//            log.debug("1");
//        });
//        pool.execute(() ->{
//            log.debug("2");
//            int i = 1/0;
//        });
//        pool.execute(() ->{
//            log.debug("3");
//        });

        ExecutorService pool = Executors.newFixedThreadPool(2);
        method1(pool);
        List<Future<String>> futures = pool.invokeAll(Arrays.asList(
                () -> {
                    log.debug("begin");
                    Thread.sleep(1000);
                    return "1";
                },
                () -> {
                    log.debug("begin");
                    Thread.sleep(500);
                    return "2";
                },
                () -> {
                    log.debug("begin");
                    Thread.sleep(2000);
                    return "3";
                }
        ));
        futures.forEach(f->{
            try {
                log.debug("{}",f.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void method1(ExecutorService executorService) throws InterruptedException, ExecutionException {
        Future<String> running = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.debug("running");
                Thread.sleep(1000);
                return "ok";
            }
        });
        log.debug("{}",running.get());
    }
}
