package com.xz.juctest.test;

import lombok.Data;

@Data
public class Test6 extends Test5{
    public String age;


    @Override
    public void method() {
        super.method();
    }

    public static void main(String[] args) {
        Test5 test5 = new Test6();
        test5.setName("test5");
    }
}
