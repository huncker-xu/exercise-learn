package cn.itcast.nety.c4;

import cn.itcast.nety.c1.utils.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/18 14:27
 */
public class ServerSelector {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        System.out.println("register key:"+sscKey);

        ssc.bind(new InetSocketAddress(8080));
        while (true){
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                System.out.println("key:"+key);
                iterator.remove();
                if(key.isAcceptable()){
                    ServerSocketChannel channel = (ServerSocketChannel)key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    scKey.interestOps(SelectionKey.OP_READ);
                    System.out.println("sc:"+sc);
                    System.out.println("scKey:"+scKey);
                }else if(key.isReadable()){
                    try {
                        SocketChannel channel =(SocketChannel)key.channel();
                        ByteBuffer buffer = (ByteBuffer)key.attachment();
                        int read = channel.read(buffer);
                        if(read == -1){ //客户端正常断开
                            key.cancel();
                        }else {
                           /* buffer.flip();
                            ByteBufferUtil.debugRead(buffer);
                            System.out.println(Charset.defaultCharset().decode(buffer));*/
                            split(buffer);
                            if(buffer.position() == buffer.limit()){
                                buffer.flip();
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel(); //客户端断开，需要将key删除
                    }
                }

                //key.cancel();

            }
        }
    }

    public static void split(ByteBuffer source){
        source.flip();//读模式
        for (int i = 0; i < source.limit(); i++) {
            if(source.get(i) == '\n'){
                int length = i + 1 - source.position();
                ByteBuffer allocate = ByteBuffer.allocate(length);
                for (int j = 0; j < length; j++) {
                    allocate.put(source.get());
                }
                ByteBufferUtil.debugAll(allocate);
            }
        }
        //source.clear();//写模式,会将末尾未读取的消息丢弃
        source.compact();//写模式，会将末尾未读取的消息向前移动
        //ByteBufferUtil.debugAll(source);
    }
}
