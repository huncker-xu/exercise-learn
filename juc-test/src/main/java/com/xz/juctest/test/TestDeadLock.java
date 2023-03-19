package com.xz.juctest.test;

import com.xz.juctest.utils.Sleeper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestDeadLock {
    public static void main(String[] args) {
        Chopstick c1 = new Chopstick("1");
        Chopstick c2 = new Chopstick("2");
        Chopstick c3 = new Chopstick("3");
        Chopstick c4 = new Chopstick("4");
        Chopstick c5 = new Chopstick("5");
        new Philosoper("苏格拉底",c1,c2).start();
        new Philosoper("柏拉图",c2,c3).start();
        new Philosoper("亚里士多德",c3,c4).start();
        new Philosoper("赫拉克利特",c4,c5).start();
        new Philosoper("阿基米德",c5,c1).start();
        //new Philosoper("阿基米德",c1,c5).start();
    }
}
@Slf4j
class Philosoper extends Thread{
    Chopstick left;
    Chopstick right;

    public Philosoper(String name,Chopstick left,Chopstick right){
        super(name);
        this.left = left;
        this.right = right;
    }

    @Override
    public void run() {
        while (true){
            synchronized (left){
                synchronized (right){
                    eat();
                }
            }
        }
    }

    public void eat(){
        log.debug("eating...");
        Sleeper.sleep(1);
    }
}
class Chopstick{
    String name;

    public Chopstick(String name) {
        this.name = name;
    }
}
