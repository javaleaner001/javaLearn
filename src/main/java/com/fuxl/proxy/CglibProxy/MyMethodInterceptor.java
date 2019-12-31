package com.fuxl.proxy.CglibProxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class MyMethodInterceptor implements MethodInterceptor {
    //代理的目标对象
    private Object targetObject;

    //产生一个代理对象
    public Object createProxyInstance(Object targetObject) {
        this.targetObject = targetObject;
        //用于生成代理对象
        Enhancer enhancer = new Enhancer();
        //设置目标类为代理对象的父类
        enhancer.setSuperclass(this.targetObject.getClass());
        //设置回调对象为本身
        enhancer.setCallback(this);
        //生成一个代理类对象
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("====before=====");
        method.invoke(targetObject, objects);
        System.out.println("====after=====");
        return "======return Cglib-proxy======";
    }
}
