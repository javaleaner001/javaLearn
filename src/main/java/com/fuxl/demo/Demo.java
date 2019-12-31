package com.fuxl.demo;

import java.util.*;

public class Demo {

    public static void main(String[] args) {
//        demo1();
        treeMapDemo();
    }

    private static void treeMapDemo() {
        TreeMap treeMap = new TreeMap();

        SortedMap sortedMap = treeMap.tailMap("");
        sortedMap.firstKey();
    }

    private static void demo1() {
        Map map = new HashMap<>();

        List<Map> list = new ArrayList<>();
       /* list.forEach((key,value) -> {
            map.put(key,"");
        });*/
    }
}
