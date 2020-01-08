package com.fuxl.threadDemo.java_new;

public class Shop {

    public Shop(){

    }
    public String name;

    public static Double getPrice() {
        CompletableFutureDemo.delay();
        return 2.00;
    }


    public Shop(String name) {
        this.name = name;
    }

    public String getName() {
        CompletableFutureDemo.delay();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
