package cn.itcast.nety.c1;

import cn.itcast.nety.c1.utils.ByteBufferUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/15 22:41
 */
public class TestScatteringReads {
    public static void main(String[] args) {
        //分散读取文件
        try(FileChannel channel = new RandomAccessFile("D:\\Apps\\idea\\IdeaProjects\\exercise-learn\\netty-demo\\words.txt","r").getChannel()) {
            ByteBuffer b1 = ByteBuffer.allocate(3);
            ByteBuffer b2 = ByteBuffer.allocate(3);
            ByteBuffer b3 = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{b1,b2,b3});
            b1.flip();
            b2.flip();
            b3.flip();
            ByteBufferUtil.debugAll(b1);
            ByteBufferUtil.debugAll(b2);
            ByteBufferUtil.debugAll(b3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
