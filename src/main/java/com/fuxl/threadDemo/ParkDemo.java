
package com.fuxl.threadDemo;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ParkDemo {
    public static void main(String[] args) {
//        reentrantLockDemo();

        Thread t1 = new Thread(()->parkDemo());
        t1.start();
        LockSupport.unpark(t1);
        System.out.println("main");
    }

    private static void reentrantLockDemo() {
        ReentrantLock reentrantLock = new ReentrantLock();
        Condition condition = reentrantLock.newCondition();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        condition.signal();

        ReentrantReadWriteLock reentrantReadWriteLock =  new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
        readLock.lock();
        readLock.unlock();
        readLock.unlock();
    }

    public static void parkDemo(){
        System.out.println("thread1");
        LockSupport.park();
    }
}
