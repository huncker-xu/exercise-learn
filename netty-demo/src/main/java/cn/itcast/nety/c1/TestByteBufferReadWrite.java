package cn.itcast.nety.c1;


import cn.itcast.nety.utils.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/14 23:54
 */
public class TestByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put((byte)0x61);//a
        ByteBufferUtil.debugAll(buffer);
        buffer.put(new byte[]{0x62,0x63,0x64});
        ByteBufferUtil.debugAll(buffer);
        //System.out.println(buffer.get());
        buffer.flip();//切换为读模式
        System.out.println(buffer.get());
        ByteBufferUtil.debugAll(buffer);
        buffer.compact();//切换为写模式
        ByteBufferUtil.debugAll(buffer);
        buffer.put(new byte[]{0x65,0x6f});
        ByteBufferUtil.debugAll(buffer);


    }
}
