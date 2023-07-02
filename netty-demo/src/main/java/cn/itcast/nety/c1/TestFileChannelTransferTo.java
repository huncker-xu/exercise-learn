package cn.itcast.nety.c1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/17 23:06
 */
public class TestFileChannelTransferTo {
    public static void main(String[] args) {
        try(FileChannel from = new FileInputStream("D:\\Apps\\idea\\IdeaProjects\\exercise-learn\\netty-demo\\data.txt").getChannel();
        FileChannel to = new FileOutputStream("D:\\Apps\\idea\\IdeaProjects\\exercise-learn\\netty-demo\\to.txt").getChannel();
        ){
            //效率高，底层利用操作系统的零拷贝进行优化，最多传输2g数据
            long size = from.size();
            for(long left = size; left > 0;){
                left -= from.transferTo((size-left),left,to);
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
