package cn.itcast.nety.c1;

import cn.itcast.nety.c1.utils.ByteBufferUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/13 23:27
 */
public class TestByteBuffer {
    public static void main(String[] args) {
        //FileChannel
        try (FileChannel channel = new FileInputStream("D:\\Apps\\idea\\IdeaProjects\\exercise-learn\\netty-demo\\data.txt").getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while(true){
                int read = channel.read(buffer);
                System.out.println("字节数："+read);
                if(read == -1){
                    break;
                }
                buffer.flip();//读模式
                while (buffer.hasRemaining()){
                    byte b = buffer.get();
                    System.out.println((char)b);
                }
                ByteBufferUtil.debugAll(buffer);
                buffer.compact();//写模式
                ByteBufferUtil.debugAll(buffer);
            }

        } catch (IOException e) {

        }
    }
}
