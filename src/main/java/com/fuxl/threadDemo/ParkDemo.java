
package com.fuxl.threadDemo;

import java.util.concurrent.locks.LockSupport;

public class ParkDemo {
    public static void main(String[] args) {

        Thread t1 = new Thread(){
            @Override
            public void run() {
                parkDemo();
            }
        };
        t1.start();
        LockSupport.unpark(t1);
        System.out.println("main");
    }

    public static void parkDemo(){
        System.out.println("thread1");
        LockSupport.park();
    }
}
