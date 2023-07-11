package cn.itcast.nety.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;

import java.util.concurrent.ExecutionException;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/7/11 21:45
 */
public class TestNettyPromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //1.准备EventLoop对象
        EventLoop eventLoop = new NioEventLoopGroup().next();
        //2.可以主动创建promise,结果容器
        final DefaultPromise<Integer> promise = new DefaultPromise<>(eventLoop);
        new Thread(()->{
            //3.任意一个线程执行计算，计算完毕后向promise 填充结果
            try {
                int i = 1/0;
                Thread.sleep(1000);
                promise.setSuccess(80);
            }catch (Exception e){
                e.printStackTrace();
                promise.setFailure(e);
            }
        }).start();
        //4.接受结果的线程
        System.out.println("等待结果。。。");
        System.out.println("结果是："+promise.get());
    }
}
