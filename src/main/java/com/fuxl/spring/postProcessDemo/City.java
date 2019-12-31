package com.fuxl.spring.postProcessDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class City {
    @Autowired
    public DemoService demoService;

    public City(){
        System.out.println("======执行City构造函数======");
    }
    @PostConstruct
    public void init(){
        System.out.println("======执行City@PostConstruct======");
    }
    public String getAddress() {
        System.out.println("======City#getAddress======");
        demoService.getAddress();
        return "======return City#getAddress======";
    }
}
