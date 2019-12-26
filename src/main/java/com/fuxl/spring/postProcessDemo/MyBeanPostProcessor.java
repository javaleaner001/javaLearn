package com.fuxl.spring.postProcessDemo;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;

@Component
public class MyBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements InstantiationAwareBeanPostProcessor,
        SmartInstantiationAwareBeanPostProcessor, MergedBeanDefinitionPostProcessor, BeanPostProcessor {
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {

        System.out.println("++++++++++++++++++++"+beanName);
        System.out.println("====1====InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation");
        System.out.println("====1====在调用doCreateBean()之前，如果方法返回不为空，则返回的对象将作为Bean，避免调用doCreateBean()");
        return null;
    }

    @Override
    public Constructor<?>[] determineCandidateConstructors(Class<?> beanClass, String beanName) throws BeansException {
        System.out.println("====2====SmartInstantiationAwareBeanPostProcessor#determineCandidateConstructors");
        System.out.println("====2====用于确定使用哪个构造函数来实例Bean;");
        return null;
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition rootBeanDefinition, Class<?> aClass, String s) {
        System.out.println("====3====MergedBeanDefinitionPostProcessor#postProcessMergedBeanDefinition");
        System.out.println("====3====Bean被实例化，修改RootBeanDefinition，例如在填充Bean之前添加PropertyValue等;");
    }

    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        System.out.println("====4====SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference");
        System.out.println("====4====在填充Bean之前调用，用于解析循环引用;");
        return bean;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        System.out.println("====5====InstantiationAwareBeanPostProcessorAdapter#postProcessAfterInstantiation");
        System.out.println("====5====调用populate Bean，但是在填充值之前，return false会破坏在Bean中填充值的操作;");
        return true;
    }

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

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("====8====BeanPostProcessor#postProcessAfterInitialization");
        System.out.println("====8====在invokeInitMethods之后调用，修改最后的Bean实例");
        return bean;
    }
}
