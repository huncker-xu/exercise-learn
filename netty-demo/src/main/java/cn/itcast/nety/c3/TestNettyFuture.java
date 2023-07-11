package cn.itcast.nety.c3;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/7/11 21:18
 */
@Slf4j
public class TestNettyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        EventLoop eventLoop = group.next();
        Future<Object> future = eventLoop.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                System.out.println("执行计算");
                Thread.sleep(1000);
                return 70;
            }
        });
//        System.out.println("等待结果");
//        System.out.println("结果是："+future.get());
        future.addListener(new GenericFutureListener<Future<? super Object>>() {
            @Override
            public void operationComplete(Future<? super Object> future) throws Exception {
                System.out.println("接受结果："+future.getNow());//立刻获取结果，由于回调方法被执行，则肯定获取了结果
            }
        });
    }
}
