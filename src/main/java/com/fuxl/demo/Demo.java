package com.fuxl.demo;

import org.openjdk.jol.info.ClassLayout;

import java.util.*;

public class Demo {

    public static Fruits fruits = new Fruits();

    public static class Fruits {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        //定义一个静态枚举类
        static enum SingletonEnum {
            //创建一个枚举对象，该对象天生为单例
            INSTANCE;
            private Fruits fruits;

            //私有化枚举的构造函数
            private SingletonEnum() {
                fruits = new Fruits();
            }

            public Fruits getInstnce() {
                return fruits;
            }
        }
    }

    private enum Type {
        APPLE("苹果", 1), ORANGE("橘子", 2), BANANA("香蕉", 3);
        private String name;
        private int index;

        private Type(String name, int index) {
            this.name = name;
            this.index = index;
        }

        private static String getName(int index) {
            for (Type value : Type.values()) {
                if (value.index == index) {
                    System.out.println(value.index + "**" + value.name);
                    return value.name;
                }
            }
            return null;
        }

        public String getDemo() {
            return "a";
        }

    }

    public static void main(String[] args) {

//        demo1();
//        singleDemo();
        //        treeMapDemo();
Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }

    private static void singleDemo() {
        //饿汉模式静态生成对象，jvm启动时创建
        System.out.println(fruits);
        System.out.println(fruits);
        boolean flage = Type.APPLE== Type.APPLE;
        System.out.println(flage);
        Fruits fruits = new Fruits();
        fruits.setName(String.valueOf(Type.APPLE));
        System.out.println(fruits.getName());
        System.out.println(Type.getName(2));
        //枚举类单例模式，不能延迟初始化
        System.out.println(Fruits.SingletonEnum.INSTANCE.getInstnce());
        System.out.println(Fruits.SingletonEnum.INSTANCE.getInstnce());
    }

    private static void treeMapDemo() {
        TreeMap treeMap = new TreeMap();
        treeMap.put(1, 1);
        treeMap.put(2, 2);
        treeMap.put(4, 4);
        treeMap.put(5, 5);
        treeMap.put(6, 6);
        //获取比3大的第一个值
        SortedMap sortedMap = treeMap.tailMap(3);
        System.out.println(sortedMap.firstKey());
    }

    private static void demo1() {
        List list = Arrays.asList("1", "2", "3");
        list.forEach(value -> System.out.println(value));
        list.forEach(System.out::println);

    }
}
