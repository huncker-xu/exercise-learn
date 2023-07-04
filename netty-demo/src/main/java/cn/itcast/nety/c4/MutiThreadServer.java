package cn.itcast.nety.c4;

import cn.itcast.nety.c1.utils.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/7/3 23:50
 */
@Slf4j
public class MutiThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        //创建服务器
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        //将服务器注册到bossSelector上
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));
        //1.创建固定数量的worker
        Worker worker = new Worker("worker-0");
        while(true){
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                iter.remove();
                if(key.isAcceptable()){
                    SocketChannel sc = ssc.accept();
                    sc .configureBlocking(false);
                    log.debug("connected...{}",sc.getRemoteAddress());
                    //2.关联selector
                    log.debug("before register...{}",sc.getRemoteAddress());
                    //初始化线程selector 启动worker-0
                    worker.register();
                    //boss
                    sc.register(worker.selector,SelectionKey.OP_READ,null);
                    log.debug("after register...{}",sc.getRemoteAddress());

                }
            }
        }
    }

    static class Worker implements Runnable{
        private Thread thread;
        private Selector selector;
        private String name;
        //还未初始化
        private volatile boolean start = false;

        public Worker(String name) {
            this.name = name;
        }

        //初始化线程和selector 启动worker
        public void register() throws IOException {
            if(!start){
                thread = new Thread(this,name);
                thread.start();
                selector = Selector.open();
                start = true;
            }

        }

        @Override
        public void run() {
            try {
                while (true){
                     selector.select();
                    Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
                    while (iter.hasNext()){
                        SelectionKey key = iter.next();
                        iter.remove();
                        if(key.isReadable()){
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel channel = (SocketChannel)key.channel();
                            log.debug("read...{}",channel.getRemoteAddress());
                            channel.read(buffer);
                            buffer.flip();
                            ByteBufferUtil.debugAll(buffer);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
