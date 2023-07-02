package cn.itcast.nety.c1;

import cn.itcast.nety.c1.utils.ByteBufferUtil;

import java.nio.ByteBuffer;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/16 0:10
 */
public class TestByteBufferEaxm {
    public static void main(String[] args) {
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello World\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
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
        ByteBufferUtil.debugAll(source);
    }
}
