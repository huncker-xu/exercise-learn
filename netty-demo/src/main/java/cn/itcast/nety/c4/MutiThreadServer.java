package cn.itcast.nety.c4;

import cn.itcast.nety.utils.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

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
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker-"+i);
        }
        AtomicInteger index = new AtomicInteger();
        while(true){
            //默认为阻塞
            boss.select();
            Iterator<SelectionKey> iter = boss.selectedKeys().iterator();
            while (iter.hasNext()){
                SelectionKey key = iter.next();
                iter.remove();
                if(key.isAcceptable()){
                    //默认阻塞
                    SocketChannel sc = ssc.accept();
                    //设置为非阻塞
                    sc .configureBlocking(false);
                    log.debug("connected...{}",sc.getRemoteAddress());
                    //2.关联selector
                    log.debug("before register...{}",sc.getRemoteAddress());
                    //初始化线程selector 启动worker-0
                    workers[index.getAndIncrement()%workers.length].register(sc);
//                    //boss
//                    sc.register(worker.selector,SelectionKey.OP_READ,null);
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
        private ConcurrentLinkedDeque<Runnable> queue = new ConcurrentLinkedDeque<>();

        public Worker(String name) {
            this.name = name;
        }

        //初始化线程和selector 启动worker-0
        public void register(SocketChannel sc) throws IOException {
            if(!start){
                thread = new Thread(this,name);
                thread.start();
                selector = Selector.open();
                start = true;
            }
            //向队列添加任务，但这个任务并没有立刻执行
            //boss
            queue.add(() ->{
                try {
                    sc.register(selector,SelectionKey.OP_READ,null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            //唤醒select方法
            selector.wakeup();
        }

        @Override
        public void run() {
            try {
                while (true){
                    //worker-0 阻塞
                     selector.select();
                    Runnable task = queue.poll();
                    if(task != null){
                        //执行了sc.register(selector,SelectionKey.OP_READ,null)
                        task.run();
                    }
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
