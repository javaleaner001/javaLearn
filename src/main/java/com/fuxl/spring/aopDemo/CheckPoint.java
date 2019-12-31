package com.fuxl.spring.aopDemo;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2019/9/18.
 * 1.添加切面@Aspect
 * 2.添加切点@Pointcut多个连接点组合为一个切点
 * 3.添加通知@Before等
 * 代理前为目标对象targetObject，代理后为代理对象proxyObject
 */
@Component
@Aspect
public class CheckPoint {

    @Pointcut("execution(* com.fuxl.spring.aopDemo.*.*(..))")
    public void pointCut(){}

    @Before("pointCut()")
    private void checkPointBefore(){
    System.out.println("======AOP-before======");

    }
}
