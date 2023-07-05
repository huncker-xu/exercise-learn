package cn.itcast.nety.c1;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/15 22:50
 */
public class TestGatheringWrites {
    public static void main(String[] args) {
        ByteBuffer b1 = StandardCharsets.UTF_8.encode("hello");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode("world");
        ByteBuffer b3 = StandardCharsets.UTF_8.encode("你好");

        try(FileChannel channel = new RandomAccessFile("words2.txt","rw").getChannel()){
            channel.write(new ByteBuffer[]{b1,b2,b3});
        }catch (IOException e){

        }

    }
}