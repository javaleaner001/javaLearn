package com.fuxl.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Created by Administrator on 2019/9/18.
 */
public class SpringTest {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
//        ac.refresh();
        //1.aopDemo
//        Person bean = ac.getBean(Person.class);
//        bean.getOffer();
        //2.jdk 动态代理
//        MyInvocationHandler myInvocationHandler = new MyInvocationHandler();
//        QueryDao dao = (QueryDao)myInvocationHandler.getProxy(QueryDao.class);
//        dao.query();

        //mybatisDemo
        System.out.println(ac.getBean("myFactoryBean"));
        //com.fuxl.spring.mybatisDemo.QueryDao
        System.out.println(ac.getBean("&myFactoryBean"));
        //com.fuxl.spring.mybatisDemo.MyFactoryBean@a7bfbc



    }
}
