package com.xz.juctest.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestAccount {
    public static void main(String[] args) {
        AccountA accountUnsafe = new AccountUnsafe(10000);
        AccountA.demo(accountUnsafe);
        AccountA a = new AcountCas(10000);
        AccountA.demo(a);
    }
}

class AcountCas implements AccountA{
    private AtomicInteger balance;

    public AcountCas(int balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        while (true){
            int prev = balance.get();
            int next = prev - amount;
            if(balance.compareAndSet(prev,next)){
                break;
            }
        }
    }
}
class AccountUnsafe implements AccountA{
    private Integer balance;

    public AccountUnsafe(Integer balance){
        synchronized (this) {
            this.balance = balance;
        }
    }

    @Override
    public Integer getBalance() {
        return this.balance;
    }

    @Override
    public void withdraw(Integer amount) {
        synchronized (this) {
            this.balance -= amount;
        }
    }
}

interface AccountA{
    //获取余额
    Integer getBalance();
    //取款
    void withdraw(Integer amount);

    static void demo(AccountA accountA){
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() ->{
                accountA.withdraw(10);
            }));
        }
        long start = System.nanoTime();
        ts.forEach(Thread::start);
        ts.forEach(t ->{
            try{
                t.join();
            }catch (InterruptedException interruptedException){
                interruptedException.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(accountA.getBalance()+" cost:"+(end-start)/1000_1000+" ms");
    }
}
