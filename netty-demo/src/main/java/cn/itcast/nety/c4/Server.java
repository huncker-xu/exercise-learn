package cn.itcast.nety.c4;

import cn.itcast.nety.utils.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/18 1:04
 */
public class Server {

    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        //1创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //2绑定监听端口
        ssc.bind(new InetSocketAddress(8080));
        //3连接集合
        List<SocketChannel> channels = new ArrayList<>();
        while (true){
            System.out.println("connecting");
            SocketChannel sc = ssc.accept();//阻塞方法
            channels.add(sc);
            System.out.println("connected... "+sc);
            for (SocketChannel channel : channels) {
                System.out.println("before read... "+channel);
                channel.read(buffer);//阻塞方法
                buffer.flip();//读模式
                ByteBufferUtil.debugRead(buffer);
                buffer.clear();//写模式
                System.out.println("after read..."+channel);
            }
        }


    }
}
