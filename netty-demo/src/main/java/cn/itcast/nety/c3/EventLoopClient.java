package cn.itcast.nety.c3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/7/6 23:14
 */
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        //2.带有future promise的类型都是和异步方法配套使用，用来处理结果
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    //12.在建立连接后被调用
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //1.连接到服务器，异步非阻塞
                .connect(new InetSocketAddress("localhost", 8080));
//        //2.1使用sync方法同步处理结果(主线程发起的调用，主线程等待异步线程响应结果)
//        channelFuture.sync();//阻塞住当前线程，知道nio线程连接建立完毕
//        Channel channel = channelFuture.channel();
//        System.out.println(channel);
//        channel.writeAndFlush("hello world");

        //2.2 使用addListener（回调对象）方法异步处理结果（等待连接响应结果，处理响应结果，都交给异步线程处理）
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            //在nio 线程连接建立后，会调用operationComplete
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                Channel channel = channelFuture.channel();
                System.out.println(channel);
                channel.writeAndFlush("hello world");
            }
        });

    }
}
