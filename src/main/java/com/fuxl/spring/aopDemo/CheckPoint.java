package com.fuxl.spring.aopDemo;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2019/9/18.
 * 1.添加切面@Aspect
 * 2.添加切点@Pointcut
 * 3.添加连接点@Before
 */
@Component
@Aspect
public class CheckPoint {

    @Pointcut("execution(* com.fuxl.spring.aopDemo.*.*(..))")
    public void pointCut(){}

    @Before("pointCut()")
    private void checkPointBefore(){
    System.out.println("====before====");

    }
}
