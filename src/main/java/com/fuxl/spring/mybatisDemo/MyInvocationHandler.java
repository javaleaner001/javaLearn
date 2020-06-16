package com.fuxl.spring.mybatisDemo;

import org.apache.ibatis.annotations.Select;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理
 */
public class MyInvocationHandler implements InvocationHandler {
    public Class classImp;

    public Class getClassImp() {
        return classImp;
    }

    public void setClassImp(Class classImp) {
        this.classImp = classImp;
    }

    public Object getProxyImp(Class clazz) {
        //2、传入实现类
        return Proxy.newProxyInstance(classImp.getClassLoader(), classImp.getInterfaces(), this);
    }

    public Object getProxy(Class clazz) {
        //1、传入接口类(mybatis)
        Class inClazz[] = new Class[]{clazz};
        return Proxy.newProxyInstance(clazz.getClassLoader(), inClazz, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Select annotation = method.getAnnotation(Select.class);
        if (annotation != null) {
            System.out.println("connect");
            System.out.println(annotation.value()[0]);
            System.out.println("out connect");
        }
        //调用被代理类所有方法，会执行代理类toString方法，需要写返回
        if (method.getName().equals("toString")) {
            return proxy.getClass().getInterfaces()[0].getName();
        }
      /*  //代理实现类时执行实现类方法
        Object invoke = method.invoke(classImp, args);*/
        return null;
    }
}
