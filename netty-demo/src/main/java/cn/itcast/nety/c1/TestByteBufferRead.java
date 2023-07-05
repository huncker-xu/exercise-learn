package cn.itcast.nety.c1;


import cn.itcast.nety.utils.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/15 21:46
 */
public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a','b','c','d'});
        buffer.flip();//读取模式
        System.out.println(buffer.get(new byte[4]));
        ByteBufferUtil.debugAll(buffer);
        buffer.rewind();//从头开始读
        System.out.println((char)buffer.get());
        ByteBufferUtil.debugAll(buffer);
        buffer.mark();//在索引为1的位置加标记
        System.out.println((char)buffer.get());
        ByteBufferUtil.debugAll(buffer);
        buffer.reset();//重置索引
        ByteBufferUtil.debugAll(buffer);
        System.out.println((char)buffer.get());
        System.out.println((char)buffer.get());
        ByteBufferUtil.debugAll(buffer);

        //get(i)不会改变索引的位置
        System.out.println((char)buffer.get(1));
        System.out.println((char)buffer.get(3));
        ByteBufferUtil.debugAll(buffer);



    }

}
