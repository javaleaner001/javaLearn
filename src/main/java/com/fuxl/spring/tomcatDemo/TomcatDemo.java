package com.fuxl.spring.tomcatDemo;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class TomcatDemo {

    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        try {
//            tomcat.addContext("/","d:\\test\\");
            //web项目标识
            tomcat.addWebapp("/", "d:\\test\\");
            tomcat.setPort(8080);
            tomcat.start();
            //等待访问
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}
