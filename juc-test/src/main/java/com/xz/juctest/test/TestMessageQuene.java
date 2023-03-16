package com.xz.juctest.test;

import com.xz.juctest.utils.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @author Xu Zeng
 * @version 2020.3
 * @date 2023/3/16 21:53
 */
@Slf4j(topic = "message")
public class TestMessageQuene {

    public static void main(String[] args) {
        MessageQuene messageQuene = new MessageQuene(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(()->{
                messageQuene.put(new Message(id,"值"+id));
            },"生产者"+i).start();

            new Thread(()->{
                Sleeper.sleep(1);
                while (true) {
                    Sleeper.sleep(1);
                    Message take = messageQuene.take();
                }
            },"消费者"+i).start();
        }
    }
}
@Slf4j
class MessageQuene{
    //消息的队列集合
    private LinkedList<Message> list = new LinkedList<>();
    //队列容量
    private int capcity;
    //获取消息


    public MessageQuene(int capcity) {
        this.capcity = capcity;
    }

    //获取消息
    public Message take(){
        synchronized (list){
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，消费者线程的等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = list.removeFirst();
            log.debug("已消费消息");
            list.notifyAll();
            return message;
        }
    }

    //存入消息
    public void put(Message message){
        synchronized (list){
            //存满时等待
            while (list.size() == capcity){
                try {
                    log.debug("队列已满，生产者线程的等待");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            list.addLast(message);
            log.debug("已生产消息");
            list.notifyAll();
        }
    }
}


//final不能有子类
final class Message{
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}

