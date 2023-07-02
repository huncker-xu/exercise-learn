package cn.itcast.nety.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/18 1:27
 */
public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost",8080));
        //sc.write(Charset.defaultCharset.encode("hello!"));
        sc.write(Charset.defaultCharset().encode("helloworld1235466\n"));
        System.in.read();
    }
}
