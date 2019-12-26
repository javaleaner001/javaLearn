package com.fuxl.spring.mybatisDemo;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 第三方对象交给spring管理
 * 1、@Bean
 * 2、ConfigurableListableBeanFactory.registerSingleton("systemProperties", this.getEnvironment().getSystemProperties());
 * 3、实现FactoryBean 可以使用@Component注入spring容器（MyFactoryBean）或者直接手动放入beanDefinitionMap中(见MyImportBeanDefinitionRegistar)
 */
@Component
public class MyFactoryBean implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler();
        QueryDao dao = (QueryDao)myInvocationHandler.getProxy(QueryDao.class);
        return dao;
    }

    @Override
    public Class<?> getObjectType() {
        return QueryDao.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
