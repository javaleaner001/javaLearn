package com.fuxl.demo;

import org.openjdk.jol.info.ClassLayout;

/**
 * 对象头的前64位是MarkWord，后32位是类的元数据指针（开启指针压缩）。
 * Java 对象头主要包括两部分，第一部分就是 Mark Word，这也是 Java 锁实现原理中重要的一环，另外一部分是 Klass Word。
 *
 * Klass Word 这里其实是虚拟机设计的一个oop-klass model模型，这里的OOP是指Ordinary Object Pointer（普通对象指针），
 * 看起来像个指针实际上是藏在指针里的对象。而 klass 则包含 元数据和方法信息，用来描述 Java 类。它在64位虚拟机开启压缩指针的环境下占用 32bits 空间。
 *
 * Mark Word 是我们分析的重点，这里也会设计到锁的相关知识。Mark Word 在64位虚拟机环境下占用 64bits 空间。整个Mark Word的分配有几种情况：
 *
 * 未锁定（Normal）： 哈希码（identity_hashcode）占用31bits，分代年龄（age）占用4 bits，偏向模式（biased_lock）占用1 bits，锁标记（lock）占用2 bits，剩余26bits 未使用(也就是全为0)
 * 可偏向（Biased）： 线程id 占54bits，epoch 占2 bits，分代年龄（age）占用4 bits，偏向模式（biased_lock）占用1 bits，锁标记（lock）占用2 bits，剩余 1bit 未使用。
 * 轻量锁定（Lightweight Locked）： 锁指针占用62bits，锁标记（lock）占用2 bits。
 * 重量级锁定（Heavyweight Locked）：锁指针占用62bits，锁标记（lock）占用2 bits。
 * GC 标记：标记位占2bits，其余为空（也就是填充0）
 *
 * 0 00 轻量级锁
 * 0 01 无锁
 * 0 10 重量级锁
 * 0 11 gc标志
 * 1 01 偏向锁
 */
public class ObjectDemo {
//    int a =8;
    public static void main(String[] args) {
//        ObjectDemo objectDemo = new ObjectDemo();
//        Student s = new Student();
        Object obj = new Object();
        Mobile mobile = new Mobile();
        Mobile mobile1 = new Mobile();
//        System.out.println(ClassLayout.parseInstance(new Object()).toPrintable());
//        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
//        System.out.println(ClassLayout.parseInstance(s).toPrintable());
//        System.out.println(ClassLayout.parseInstance(mobile).toPrintable());
//        System.out.println(ClassLayout.parseInstance(mobile1).toPrintable());
//        System.out.println(ClassLayout.parseInstance(new Mobile()).toPrintable());

        for (int i=0;i<2;i++){
            new Thread(()->{
                synchronized (mobile){
                    /**
                     *
                     com.fuxl.demo.Mobile object internals:
                     OFFSET  SIZE               TYPE DESCRIPTION                               VALUE 010重量级锁
                     0     4                    (object header)                           2a d1 3c 00 (00101010 11010001 00111100 00000000) (3985706)
                     4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
                     8     4                    (object header)                           18 0a 3e 17 (00011000 00001010 00111110 00010111) (389941784)
                     12     4   java.lang.String Mobile.name                               (object)
                     Instance size: 16 bytes
                     Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
                     */
                    System.out.println(ClassLayout.parseInstance(mobile).toPrintable());
                }
            }).start();
        }
        synchronized (mobile){
            System.out.println(ClassLayout.parseInstance(mobile).toPrintable());

        }

//        System.out.println(ClassLayout.parseInstance(mobile).toPrintable());
//        System.out.println(mobile.hashCode()+"******"+mobile1.hashCode());
    }
    /**
     *com.fuxl.demo.Mobile object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
     *       0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4        (object header)                           43 a1 e4 27 (01000011 10100001 11100100 00100111) (669294915)
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     *
     * com.fuxl.demo.Mobile object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE 001轻量级锁
     *       0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
     *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4        (object header)                           43 a1 e4 27 (01000011 10100001 11100100 00100111) (669294915)
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     *
     * com.fuxl.demo.Mobile object internals:
     *  OFFSET  SIZE   TYPE DESCRIPTION                               VALUE  000  轻量级锁
     *       0     4        (object header)                           b8 f5 05 02 (10111000 11110101 00000101 00000010) (33945016)
     *       4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
     *       8     4        (object header)                           43 a1 e4 27 (01000011 10100001 11100100 00100111) (669294915)
     *      12     4        (loss due to the next object alignment)
     * Instance size: 16 bytes
     * Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
     */

}

class Student{
    long id;

}