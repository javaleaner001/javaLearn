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
        System.out.println("call.do");
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
        System.out.println("异步处理已开始，后面可以处理其他业务逻辑。");
        //其他业务逻辑
        return result;
    }

    /**
     * 异步调用（不同请求中，消息中间件方式）
     *DeferredResult需要自己用线程来处理结果setResult，而Callable的话不需要我们来维护一个结果处理线程。
     * 总体来说，Callable的话更为简单，同样的也是因为简单，灵活性不够；相对地，DeferredResult更为复杂一些，但是又极大的灵活性。
     * 在可以用Callable的时候，直接用Callable；而遇到Callable没法解决的场景的时候，可以尝试使用DeferredResult。
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
        new Thread(()-> listenerDeferrendResult(new HashMap<>())).start();
       /* new Thread() {
            @Override
            public void run() {
                listenerDeferrendResult(new HashMap<>());
            }
        }.start();*/
        System.out.println("监听开始，后面可以继续处理其他业务逻辑。");
        //其他业务逻辑
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
