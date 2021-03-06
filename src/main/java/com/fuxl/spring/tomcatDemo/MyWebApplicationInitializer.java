package com.fuxl.spring.tomcatDemo;

import com.fuxl.spring.tomcatDemo.AppConfig;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

/**
 * servlet3.0协议
 * spring-web-5.1.9.RELEASE.jar!\META-INF\services\javax.servlet.ServletContainerInitializer
 */
public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletCxt) {

        // Load Spring web application configuration
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.register(AppConfig.class);
//        ac.refresh();

        // Create and register the DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(ac);
        ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        //支持异步返回<async-supported>true</async-supported>
        registration.setAsyncSupported(true);
        registration.addMapping("/*");
    }
}
