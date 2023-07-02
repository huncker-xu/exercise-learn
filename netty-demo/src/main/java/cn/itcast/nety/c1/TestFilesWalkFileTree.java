package cn.itcast.nety.c1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/6/17 23:32
 */
public class TestFilesWalkFileTree {

    public static void main(String[] args) throws IOException {
        String source = "D:\\qycache";
        String target = "D:\\qycacheaaa";
        Files.walk(Paths.get(source)).forEach(path->{
            try {
                String targetName = path.toString().replace(source,target);
                if(Files.isDirectory(path)){
                    Files.createDirectory(Paths.get(targetName));
                }else if(Files.isRegularFile(path)){
                    Files.copy(path,Paths.get(targetName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
