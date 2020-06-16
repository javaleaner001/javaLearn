package com.fuxl.spring.postProcessDemo;

import com.fuxl.proxy.CglibProxy.MyMethodInterceptor;
import com.fuxl.proxy.JdkProxy.IjdkProxyService;
import com.fuxl.proxy.JdkProxy.JdkProxyServiceImpl;
import com.fuxl.proxy.JdkProxy.MyInvoceHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;

@Component
public class MyBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements InstantiationAwareBeanPostProcessor,
        SmartInstantiationAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, BeanPostProcessor {

    /**
     * 第一次调用后置处理器，当前beanName代表的类要不要加代理，加了代理则直接代理返回（AOP:JdkProxy/CGlib）
     * 注意：如果这里被代理，还没有执行循环引用逻辑， 就返回了bean，循环引用时会报错
     * AbstractAutoProxyCreator#postProcessBeforeInstantiation：AOP代理创建
     * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#resolveBeforeInstantiation
     * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInstantiation
     * org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#postProcessBeforeInstantiation
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("++++++++++++++++++++"+beanName);
        System.out.println("====1====InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation");
        System.out.println("====1====在调用doCreateBean()之前，如果方法返回不为空，则返回的对象将作为Bean，避免调用doCreateBean()");

        //注意：如果这里被代理，还没有执行循环引用逻辑， 就返回了bean，循环引用时会报错
        if(beanName.equals("city")){
         System.out.println("====1====注意：如果这里被代理，还没有执行循环引用逻辑， 就返回了bean，循环引用时会报错");
            City city = (City)new MyMethodInterceptor().createProxyInstance(new City());
            return city;

        }
        if(beanName.equals("demoServiceImpl")){
         System.out.println("====1====注意：如果这里被代理，还没有执行循环引用逻辑， 就返回了bean，循环引用时会报错");
            MyInvoceHandler myInvocationHandler = new MyInvoceHandler();
            DemoServiceImpl demoServiceImpl = new DemoServiceImpl();
            myInvocationHandler.setObject(demoServiceImpl);
            DemoService demoService = (DemoService) Proxy.newProxyInstance(DemoServiceImpl.class.getClassLoader(), DemoServiceImpl.class.getInterfaces(), myInvocationHandler);
//            demoService.getAddress();
            return demoService;
        }
        return null;
    }

    /**
     *  可以返回一个构造函数
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("====2====SmartInstantiationAwareBeanPostProcessor#determineCandidateConstructors");
        System.out.println("====2====用于确定使用哪个构造函数来实例Bean;");
        return null;
    }

    /**
     * 修改RootBeanDefinition
     * @param rootBeanDefinition
     * @param aClass
     * @param s
     */
    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition rootBeanDefinition, Class<?> aClass, String s) {
        System.out.println("====3====MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition");
        System.out.println("====3====Bean被实例化，修改RootBeanDefinition，例如在填充Bean之前添加PropertyValue等;");
    }

    /**
     * 循环引用，当创建City对象时，调用到属性注入后置处理器，发现有DemoService要注入，
     * 回头去注入DemoService的bean，发现DemoService中有City需要注入，形成循环引用，
     * 此时getSingleton获取City的Bean,该方法中判断City是否正在创建中，如果是
     * 先调用SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference后置处理器，然后
     * 从二级缓存工厂中拿出City对象进行注入（对象和bean区别）
     * org.springframework.beans.factory.support.AbstractBeanFactory#doGetBean
     * org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#getSingleton(java.lang.String)
     * org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        //beanName:City
        System.out.println(beanName+"====4====SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference");
        System.out.println("====4====在填充Bean之前调用，用于解析循环引用;");
        return bean;
    }

    /**
     * 调用populate Bean，但是在填充值之前，return false会破坏在Bean中填充值的操作;autowireByName()及autowireByType()将不执行
     * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#populateBean(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, org.springframework.beans.BeanWrapper)
     * org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor#postProcessAfterInstantiation(java.lang.Object, java.lang.String)
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("====5====InstantiationAwareBeanPostProcessorAdapter#postProcessAfterInstantiation");
        System.out.println("====5====调用populate Bean，但是在填充值之前，return false会破坏在Bean中填充值的操作;autowireByName()及autowireByType()将不执行");
        return true;
    }

    /**
     *
     * @param pvs
     * @param pds
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        System.out.println("====6====InstantiationAwareBeanPostProcessorAdapter#postProcessPropertyValues");
        System.out.println("====6====调用populate Bean，但在填充值之前，修改将应用于Bean的PropertyValues，返回null将打破在Bean中填充值的opreation;");
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("====7====BeanPostProcessor#postProcessBeforeInitialization");
        System.out.println("====7====在invokeInitMethods之前调用，修改最后的Bean实例");
        return bean;
    }

    /** 这里的 targetSourcedBeans 将与 earlyProxyReferences 一起分析
     *  targetSourcedBeans: 当在实例化前置方法 postProcessBeforeInstantiation 中创建了代理类, 则在 targetSourcedBeans 中将添加 beanName, 也就是 targetSourcedBeans 中含有 beanName 则说明这个类被动态代理了
     *  earlyProxyReferences: 当 Bean 被循环引用, 并且被暴露了, 则会通过 getEarlyBeanReference 来创建代理类; 在初始化后置方法 postProcessAfterInitialization 中也会通过判断 earlyProxyReferences 中是否存在 beanName 来决定是否需要对 target 进行动态代理
     */

    /**
     *
     * 执行initializeBean方法时，调用BeanPostProcessor#applyBeanPostProcessorsAfterInitialization后置处理器，创建AOP代理（JdkProxy/GCLib）
     *  org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#doCreateBean
     * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#initializeBean(java.lang.String, java.lang.Object, org.springframework.beans.factory.support.RootBeanDefinition)
     * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization
     * org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#postProcessAfterInitialization
     * org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator#wrapIfNecessary
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("====8====BeanPostProcessor#postProcessAfterInitialization");
        System.out.println("====8====在invokeInitMethods之后调用，修改最后的Bean实例");
        return bean;
    }
}
