package com.fuxl.proxy.CglibProxy;


public class CgLibProxyTest {

    public static void main(String[] args) {
        City city =(City) new MyMethodInterceptor().createProxyInstance(new City());
        city.getName();
    }
}
