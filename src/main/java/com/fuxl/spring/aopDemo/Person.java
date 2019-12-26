package com.fuxl.spring.aopDemo;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2019/9/18.
 */
@Component
public class Person {
    public void getOffer(){
        System.out.println("======getOffer======");
    }
}
