package cn.itcast.nety.c1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/7/5 22:32
 */
public class HelloServer {
    /*
    * channel是数据的通道
    * msg是流动的数据
    * handler是数据处理的工序
    * event是处理数据的工人
    * */


    public static void main(String[] args) {
        //1.启动器，负责组装netty组件，启动服务器
        new ServerBootstrap()
                //2.BossEventLoop，WorkEventLoop(selector,thread),group组
                //不断循环，看看有没有事件 关心accept/read事件，看看有没有连接和发送的数据
                //16.由某个EventLoop处理read事件，接受到ByteBuf，交给下面自定义的handler处理
                .group(new NioEventLoopGroup())
                //3.选择服务器的ServerSocketChannel实现 OIO BIO
                .channel(NioServerSocketChannel.class)
                //4.boss 负责处理连接 worker(child) 负责处理读写，决定了work(child)能执行哪些操作(handler)
                .childHandler(
                        //5.channel 代表和客户端进行数据读写的通道Initializer 初始化，负责添加到别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                            //12.连接建立后调用初始化方法
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                //6.添加具体handler
                                //将ByteBuf转换为
                                //17.将ByteBuf还原为原字符串
                                ch.pipeline().addLast(new StringDecoder());
                                //
                                //18.执行read方法，打印字符串
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                    //读事件
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx,Object msg){
                                        //打印上一步转换好的字符串
                                        System.out.println(msg);
                                    }
                                });
                            }
                        }
                )
                //6.绑定监听端口
                .bind(8080);
    }
}
