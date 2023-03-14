package com.xz.juctest.test;

import com.xz.juctest.utils.Sleeper;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import sun.awt.windows.ThemeReader;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/14 23:33
 */
public class TestGardedObject {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Sleeper.sleep(1);
        for (Integer id : MailBoxes.getIds()) {
            new Postman(id,"内容"+id).start();
        }
    }
}


//居民
@Slf4j(topic = "c.people")
class People extends Thread{
    @Override
    public void run() {
        //收信
        GuardedObject guardedObject = MailBoxes.createGuardedObject();
        log.debug("等待开始收信 id:{}",guardedObject.getId());
        Object mail = guardedObject.get(5000);
        log.debug("收到信 id:{},内容:{}",guardedObject.getId(),mail);
    }
}
//邮递员
@Slf4j(topic = "c.postman")
class Postman extends Thread{
    private int id;
    private String mail;
    public Postman(int id, String mail){
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardObject = MailBoxes.getGuardObject(id);
        log.debug("送信 id:{},内容:{}",id,mail);
        guardObject.complete(mail);
    }
}

//邮箱
class MailBoxes{
    //hashtable线程安全
    private static Map<Integer,GuardedObject> boxes = new Hashtable<>();

    private static int id = 1;
    public static synchronized int generateId(){
        return id++;
    }
    public static GuardedObject createGuardedObject(){
        GuardedObject guardedObject = new GuardedObject(generateId());
        boxes.put(guardedObject.getId(),guardedObject);
        return guardedObject;
    }

    public static GuardedObject getGuardObject(int id){
        return boxes.remove(id);
    }

    public static Set<Integer> getIds(){
        return boxes.keySet();
    }
}

class GuardedObject{
    //唯一标识
    private int id;
    public GuardedObject(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    //结果
    private Object response;

    //获取结果
    //timeout表示要等待多久
    public Object get(long timeout){
        synchronized (this){
            long begin = System.currentTimeMillis();
            long passTime = 0;
            while (response == null){
                long waitTime = timeout - passTime;
                if(passTime >= timeout){
                    break;
                }
                try {
                    this.wait(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //求得经历时间
                passTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    //产生结果
    public void complete(Object response) {
        synchronized (this){
            this.response = response;
            this.notifyAll();
        }
    }
}
