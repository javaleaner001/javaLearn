package com.fuxl.spring.mybatisDemo;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.io.File;
import java.net.URL;

/**
 * 第三方对象交给spring管理
 * 解析beanDefinition 放入 beanDefinitionMap中
 * @author Administrator
 */
public class MyImportBeanDefinitionRegistar implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        //扫描文件夹
        ClassLoader classLoader = MyImportBeanDefinitionRegistar.class.getClassLoader();
        URL resource = classLoader.getResource("com/fuxl/spring/mybatisDemo/service");
        if (resource != null) {
            File file = new File(resource.getPath());
            for (File listFile : file.listFiles()) {
//                System.out.println(listFile.getPath());
                String className = listFile.getPath().substring(listFile.getPath().indexOf("com"), listFile.getPath().indexOf(".class")).replace("\\", ".");
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MyFactoryBeanNew.class);
                AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
                beanDefinition.getPropertyValues().add("mapperInterface", className);
                String beanName = className.substring(className.lastIndexOf(".") + 1);
                //底层实现beanDefinitionMap.put(beanName, beanDefinition);
                beanDefinitionRegistry.registerBeanDefinition(toLowerCaseFirstOne(beanName), beanDefinition);
            }
        }

       /* BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(MyFactoryBeanNew.class);
        AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        beanDefinition.getPropertyValues().add("mapperInterface","com.fuxl.spring.mybatisDemo.service.QueryDao");
        beanDefinitionRegistry.registerBeanDefinition("qDao",beanDefinition);

        BeanDefinitionBuilder beanDefinitionBuilder_u = BeanDefinitionBuilder.genericBeanDefinition(MyFactoryBeanNew.class);
        AbstractBeanDefinition beanDefinition_u = beanDefinitionBuilder_u.getBeanDefinition();
        beanDefinition_u.getPropertyValues().add("mapperInterface","com.fuxl.spring.mybatisDemo.service.UserDao");
        beanDefinitionRegistry.registerBeanDefinition("uDao",beanDefinition_u);*/
    }

    /**
     * 首字母转小写
     *
     * @param s
     * @return
     */
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }
}
