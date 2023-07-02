package cn.itcast.nety.c1;

import cn.itcast.nety.c1.utils.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/15 22:09
 */
public class TestByteBufferString {
    public static void main(String[] args) {
        // 字符串转ByteBuffer
        //1.
        ByteBuffer buffer1 = ByteBuffer.allocate(16);
        buffer1.put("hello".getBytes());
        ByteBufferUtil.debugAll(buffer1);

        //2. Charset
        ByteBuffer buffer2 = StandardCharsets.UTF_8.encode("hello");
        ByteBufferUtil.debugAll(buffer2);

        //3. wrap
        ByteBuffer buffer3 = ByteBuffer.wrap("hello".getBytes());
        ByteBufferUtil.debugAll(buffer3);


        System.out.println("=============");
        //ByteBuffer转字符串
        //System.out.println(StandardCharsets.UTF_8.decode(buffer2));
        String s = StandardCharsets.UTF_8.decode(buffer2).toString();
        System.out.println(s);

        buffer1.flip();//切换到读模式
        String s1 = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(s1);

    }
}
