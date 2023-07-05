package cn.itcast.nety.c5;

import cn.itcast.nety.utils.ByteBufferUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/7/4 22:19
 */
@Slf4j
public class AioFileChannel {
    public static void main(String[] args) throws IOException {
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("D:\\Apps\\idea\\IdeaProjects\\exercise-learn\\netty-demo\\data.txt"), StandardOpenOption.READ)) {
            ByteBuffer buffer =  ByteBuffer.allocate(16);
            log.debug("read begin...");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override//success
                public void completed(Integer result, ByteBuffer attachment) {
                    //守护线程，其他线程运行完，自己也结束
                    log.debug("read completed...{}",result);
                    attachment.flip();
                    ByteBufferUtil.debugAll(attachment);
                }

                @Override//fail
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });
            log.debug("read end...");
        } catch(IOException e) {
            e.printStackTrace();
        }
        System.in.read();
    }
}
