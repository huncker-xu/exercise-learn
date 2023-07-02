package com.xz.jvmtest;

import sun.misc.Unsafe;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/5/24 23:52
 */
public class StringTest1 {

    //StringTable["a","b","ab"] hashtable结构，不能扩容
    public static void main(String[] args) {
        //常量池中的信息，都会被加载到运行时常量池中,这时 a b ab都是常量池中的符号，还没有变为Java字符串对象
        String s1 = "a";
        String s2 = "b";
        String s3 = "ab";
        String s4 = s1+s2;
        String s5 = "a" + "b";

        System.out.println(s3 == s5);
    }
}
