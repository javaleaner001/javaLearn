package com.fuxl.proxy.JdkProxy;

import com.fuxl.spring.mybatisDemo.MyInvocationHandler;

import java.lang.reflect.Proxy;

public class JdkProxyTest {
    public static void main(String[] args) {
        MyInvoceHandler myInvocationHandler = new MyInvoceHandler();
        JdkProxyServiceImpl jdkProxyService = new JdkProxyServiceImpl();
        myInvocationHandler.setObject(jdkProxyService);
        IjdkProxyService ijdkProxyService = (IjdkProxyService) Proxy.newProxyInstance(JdkProxyServiceImpl.class.getClassLoader(), JdkProxyServiceImpl.class.getInterfaces(), myInvocationHandler);
        ijdkProxyService.query();
    }
}
