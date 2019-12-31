package com.fuxl.spring.postProcessDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoServiceImpl implements DemoService {
    @Autowired
    public City city;

    public DemoServiceImpl() {
        System.out.println("======执行DemoServiceImpl构造函数======");
    }

    @Override
    public String getAddress() {
        System.out.println("======DemoServiceImp#getAddress======");
        return "ShangHai";
    }
}
