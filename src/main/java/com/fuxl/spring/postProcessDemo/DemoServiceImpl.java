package com.fuxl.spring.postProcessDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DemoServiceImpl implements DemoService {
    @Autowired
    public City city;
    @Override
    public void getAddress() {
        System.out.println("DemoServiceImp");
    }
}
