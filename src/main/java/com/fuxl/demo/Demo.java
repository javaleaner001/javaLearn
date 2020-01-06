package com.fuxl.demo;

import java.util.*;

public class Demo {

    public static void main(String[] args) {
//        demo1();
        treeMapDemo();
    }

    private static void treeMapDemo() {
        TreeMap treeMap = new TreeMap();
        treeMap.put(1,1);
        treeMap.put(2,2);
        treeMap.put(4,4);
        treeMap.put(5,5);
        treeMap.put(6,6);
        SortedMap sortedMap = treeMap.tailMap(3);
        System.out.println(sortedMap.firstKey());
    }

    private static void demo1() {
        Map map = new HashMap<>();

        List<Map> list = new ArrayList<>();
       /* list.forEach((key,value) -> {
            map.put(key,"");
        });*/
    }
}
