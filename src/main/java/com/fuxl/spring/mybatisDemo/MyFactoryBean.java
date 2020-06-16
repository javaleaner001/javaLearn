package com.fuxl.spring.mybatisDemo;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * 总结：getBean("qDao")时去调用FactoryBean实现的getObject()方法，该方法中使用动态代理，调用最终代理方法；
 * 这个被代理的类为启动时通过ImportBeanDefinitionRegistrar.registerBeanDefinitions()方法将第三方对象交给了spring管理
 *
 * 第三方对象交给spring管理
 * 1、@Bean
 * 2、ConfigurableListableBeanFactory(DefaultListableBeanFactory).registerSingleton("systemProperties", this.getEnvironment().getSystemProperties());
 * 3、实现FactoryBean 可以使用@Component注入spring容器（MyFactoryBean）或者直接手动放入beanDefinitionMap中(见MyImportBeanDefinitionRegistar)
 * 当class实现类了FactoryBean时，通过getBean方法返回的不是FactoryBean本身，而是 FactoryBean#getObject()方法所返回的对象，相当于FactoryBean#getObject()代理了getBean()方法
 *  区别于：
 *  BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, this.registry);将读取的BEAN信息存入beanDefinitionMap：beanDefinitionMap.put(beanName, beanDefinition);
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
