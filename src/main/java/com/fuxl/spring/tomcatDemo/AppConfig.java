package com.fuxl.spring.tomcatDemo;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fuxl.spring.aopDemo.Person;
import com.fuxl.spring.mybatisDemo.MyScan;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Created by Administrator on 2019/9/18.
 */
@Configuration
//@EnableAspectJAutoProxy
@ComponentScan("com.fuxl.spring")
//@MyScan
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer {
    /**
     * 添加json解析器
     * 1.实现WebMvcConfigurer#extendMessageConverters
     * 2.add FastJsonHttpMessageConverter类
     * 3.使用@EnableWebMvc注解
     * 4.删除ac.refresh()方法,(因为会重复执行bean创建？)
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        System.out.println("fastjson");
        converters.add(new FastJsonHttpMessageConverter());
    }







    public static void main(String[] args) {
       /* //初始化分解
        AnnotationConfigApplicationContext ac1 = new AnnotationConfigApplicationContext();
        ac1.register(AppConfig.class);
        //将第三方对象注入spring容器
        ConfigurableListableBeanFactory beanFactory = ac1.getBeanFactory();
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler();
        QueryDao dao = (QueryDao)myInvocationHandler.getProxy(QueryDao.class);
        beanFactory.registerSingleton("qDao",dao );
        ac1.refresh();
        System.out.println(ac1.getBean("qDao"));*/


       /* AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        //1.aopDemo
        Person bean = ac.getBean(Person.class);
        bean.getOffer();*/

        //2.jdk 动态代理
//        MyInvocationHandler myInvocationHandler = new MyInvocationHandler();
//        QueryDao dao = (QueryDao)myInvocationHandler.getProxy(QueryDao.class);
//        dao.query();

        //3.mybatisDemo
        //3.1 使用FactoryBean MyBeanFactory
        //BeanFactory FactoryBean区别
        /*System.out.println(ac.getBean("&myFactoryBean"));
        System.out.println(ac.getBean("myFactoryBean"));
        System.out.println(ac.getBean("qDao"));*/
        //3.2 使用ImportBeanDefinitionRegistar MyBeanFactoryNew
//        QueryDao qDao = (QueryDao)ac.getBean("qDao");
//        qDao.query();

//        ac.getBean("cityService");
    }
}
