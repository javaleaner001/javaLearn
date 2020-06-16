package com.fuxl.spring.tomcatDemo;

import com.fuxl.redisson.RedissonUtilsDemo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Controller
//@RequestMapping("/tomcatDemo")
public class TomcatDemoController {
    ConcurrentHashMap<String, DeferredResult<Map<String, String>>> concurrentHashMap = new ConcurrentHashMap();

    @RequestMapping("/json")
    @ResponseBody
    public Map<String, String> tomcatDemo() {
        System.out.println("json.do");
        Map map = new HashMap();
        map.put("key", "value"+RedissonUtilsDemo.tryLock("a", 0, -1));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RedissonUtilsDemo.unlock("a");
        return map;
    }
    @RequestMapping("/json2")
    @ResponseBody
    public Map<String, String> tomcatDemo2() {
        System.out.println("json2.do");
        Map map = new HashMap();
        map.put("key", "value"+RedissonUtilsDemo.tryLock("a", 0, -1));

        RedissonUtilsDemo.unlock("a");
        return map;
    }
    /**
     * 异步调用（同一请求中）
     *
     * @return
     */
    @RequestMapping("/call")
    @ResponseBody
    public Callable<Map<String, String>> callDemo() {
        //异步调用测试
        Callable result =()-> {
            /*@Override
            public Object call() throws Exception {*/
                Thread.sleep(5000);
                Map map = new HashMap();
                map.put("callKey", "callValue");
                System.out.println("===subThread===call.do");
                return map;
//            }
        };
        System.out.println("call.do");
        return result;
    }

    /**
     * 异步调用（不同请求中，消息中间件方式）
     *
     * @return
     */
    @RequestMapping("/deferredResult")
    @ResponseBody
    public DeferredResult<Map<String, String>> deferredResultDemo() {
        //异步调用测试
        DeferredResult deferredResult = new DeferredResult();
        concurrentHashMap.put("deferredResultId", deferredResult);
        System.out.println("deferredResult.do");
        //模拟消息监听
        new Thread() {
            @Override
            public void run() {
                listenerDeferrendResult(new HashMap<>());
            }
        }.start();
        return deferredResult;
    }

    /**
     * 消息监听及接收
     *
     * @param map
     */
    public void listenerDeferrendResult(Map<String, String> map) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        map.put("deferredResultKey", "deferredResultValue");
        concurrentHashMap.get("deferredResultId").setResult(map);
        System.out.println("Listen-deferredResult");
    }
}
