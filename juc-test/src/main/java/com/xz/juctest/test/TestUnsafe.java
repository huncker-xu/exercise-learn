package com.xz.juctest.test;

import lombok.Data;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/28 21:21
 */
public class TestUnsafe {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        //允许访问private变量
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe)theUnsafe.get(null);
        System.out.println(unsafe);

        //获取域的偏移地址
        long id = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));
        long name = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));

        Teacher teacher = new Teacher();
        unsafe.compareAndSwapInt(teacher,id,0,1);
        unsafe.compareAndSwapObject(teacher,name,null,"张三");

        System.out.println(teacher);
    }

}

@Data
class Teacher{
    volatile int id;
    volatile String name;
}
