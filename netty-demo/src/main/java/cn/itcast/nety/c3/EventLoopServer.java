package cn.itcast.nety.c3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/7/6 23:06
 */
public class EventLoopServer {
    public static void main(String[] args) {
        //细分2.创建一个独立的eventLoop,处理耗时较长的操作
        DefaultEventLoop group = new DefaultEventLoop();
        new ServerBootstrap()
                //boss和worker
                //细分1.boss只负责ServerSocketChannel上的accept事件，worker只负责socketChannel上的读写
                .group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast("handler1",new ChannelInboundHandlerAdapter(){
                            @Override                                           //ByteBuf类型
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //super.channelRead(ctx, msg);
                                ByteBuf buf = (ByteBuf)msg;
                                System.out.println(buf.toString(Charset.defaultCharset()));
                                //将消息传递给下一个handler
                                ctx.fireChannelRead(msg);
                            }
                        }).addLast(group,"handler2",new ChannelInboundHandlerAdapter(){
                            @Override                                           //ByteBuf类型
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                //super.channelRead(ctx, msg);
                                ByteBuf buf = (ByteBuf)msg;
                                System.out.println(buf.toString(Charset.defaultCharset()));
                            }
                        })
                        ;
                    }
                })
                .bind(8080);
     }
}
