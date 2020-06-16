package com.fuxl.proxy.targetSource;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.target.SingletonTargetSource;

public class TargetSourctDemo {

    public static void main(String[] args) {
        TargetBean target = new TargetBean();
        TargetSource targetSource = new SingletonTargetSource(target);
        // 使用SpringAOP框架的代理工厂直接创建代理对象
        TargetBean proxy = (TargetBean) ProxyFactory.getProxy(targetSource);
        // 这里会在控制台打印：com.fuxl.proxy.targetSource.TargetBean$$EnhancerBySpringCGLIB$$39df84d3
        System.out.println(proxy.getClass().getName());
        proxy.show();

    }


}
