package com.fuxl.spring.postProcessDemo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.fuxl.spring.postProcessDemo")
public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(App.class);
//        City city = (City)ac.getBean("city");
//        System.out.println(city.getName());

    }
}
