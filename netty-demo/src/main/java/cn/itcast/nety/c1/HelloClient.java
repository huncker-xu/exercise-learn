package cn.itcast.nety.c1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/7/5 22:45
 */
public class HelloClient {
    public static void main(String[] args) throws InterruptedException {
        //7.启动类
        new Bootstrap()
                //8.添加EventLoop
                .group(new NioEventLoopGroup())
                //9.选择客户端channel实现
                .channel(NioSocketChannel.class)
                //10.添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    //12.在建立连接后被调用
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                        //15.把要发送的数据转为ByteBuf
                    }
                })
                //11.连接到服务器
                .connect(new InetSocketAddress("localhost",8080))
                //13.阻塞方法，直到连接建立
                .sync()
                //代表连接对象
                .channel()
                //14.发送数据
                .writeAndFlush("hello world");
    }
}
