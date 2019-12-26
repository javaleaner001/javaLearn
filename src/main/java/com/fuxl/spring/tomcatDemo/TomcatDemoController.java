package com.fuxl.spring.tomcatDemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
//@RequestMapping("/tomcatDemo")
public class TomcatDemoController {

    @RequestMapping("/json.do")
    @ResponseBody
    public Map<String,String> tomcatDemo(){
        System.out.println("json.do");
        Map map = new HashMap();
        map.put("key","value");
        return map;
    }
}
