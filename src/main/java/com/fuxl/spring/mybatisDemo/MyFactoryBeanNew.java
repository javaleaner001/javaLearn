package com.fuxl.spring.mybatisDemo;

import org.springframework.beans.factory.FactoryBean;

public class MyFactoryBeanNew implements FactoryBean {
    Class mapperInterface;
    @Override
    public Object getObject() throws Exception {
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler();
        return  myInvocationHandler.getProxy(mapperInterface);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    /*public Class getMapperInterface() {
        return mapperInterface;
    }*/

    public void setMapperInterface(Class mapperInterface) {
        this.mapperInterface = mapperInterface;
    }
}
