package cn.itcast.nety.c3;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/7/6 22:47
 */
@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        //1.创建事件循环组
        EventLoopGroup eventExecutors = new NioEventLoopGroup(2); //io事件，普通/定时任务
        //EventLoopGroup group = new DefaultEventLoop();//普通/定时任务
        //System.out.println(NettyRuntime.availableProcessors());
        //2.获取下一个事件循环对象
        System.out.println(eventExecutors.next());
        System.out.println(eventExecutors.next());
        System.out.println(eventExecutors.next());
        System.out.println(eventExecutors.next());
        //3.执行普通任务
        eventExecutors.next().execute(() ->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("ok");
        });

        //4.执行普通任务
        eventExecutors.next().scheduleAtFixedRate(()->{
            log.debug("ok");
        },0,1, TimeUnit.SECONDS);
        log.debug("main");
    }
}
