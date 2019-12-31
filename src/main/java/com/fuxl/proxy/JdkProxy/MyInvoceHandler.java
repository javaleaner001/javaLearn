package com.fuxl.proxy.JdkProxy;

import com.fuxl.spring.mybatisDemo.MyInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvoceHandler implements InvocationHandler {
    Object object;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("====before=====");
        method.invoke(object, args);
        System.out.println("====after=====");
        return null;
    }
}
