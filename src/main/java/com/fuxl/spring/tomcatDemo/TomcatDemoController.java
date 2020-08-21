package com.fuxl.spring.tomcatDemo;

import com.fuxl.redisson.RedissonUtilsDemo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

@Controller
//@RequestMapping("/tomcatDemo")
public class TomcatDemoController {
    ConcurrentHashMap<String, DeferredResult<Map<String, String>>> concurrentHashMap = new ConcurrentHashMap();
    public static List list = new ArrayList();

    @RequestMapping("/json")
    @ResponseBody
    public Map<String, String> tomcatDemo() {
        System.out.println("json.do");
        Map map = new HashMap();
        map.put("key", "value"+RedissonUtilsDemo.tryLock("a", 0, -1));
       /* try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        RedissonUtilsDemo.unlock("a");
        System.out.println("unlock");
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
    @RequestMapping("/json")
    @ResponseBody
    public Map<String, String> synDemo() {
        System.out.println("json.do");
        Object obj = new Object();
        //锁局部变量相当于没有锁
        synchronized (obj){
            System.out.println(Thread.currentThread().getId());
        }
        //锁方法就是锁当前类的对象，一个jvm只有一个当前类的class文件，为JVM锁
        lockDemo(Thread.currentThread().getName());
        Map map = new HashMap();
        map.put("key",Thread.currentThread().getName());
        /**
         * 输出：
         * json.do
         * 56
         * http-nio-8080-exec-2
         * json.do
         * 58
         * json.do
         * 55
         * http-nio-8080-exec-1
         * json.do
         * 62
         * json.do
         * 60
         * **lock.isLocked()**true**lock.isHeldByCurrentThread()**true
         * unlock
         * http-nio-8080-exec-4
         * **lock.isLocked()**true**lock.isHeldByCurrentThread()**true
         * unlock
         * http-nio-8080-exec-6
         * **lock.isLocked()**true**lock.isHeldByCurrentThread()**true
         * unlock
         * http-nio-8080-exec-8
         * **lock.isLocked()**true**lock.isHeldByCurrentThread()**true
         * unlock
         * **lock.isLocked()**true**lock.isHeldByCurrentThread()**true
         * unlock
         *
         * Process finished with exit code -1
         */
        return map;
    }
    /**
     * BIO每来一个连接，内核就新建一个线程，堵塞在那里监听请求；弊端：线程太多
     * 最大只能开启65535个端口
     * @return
     */
    @RequestMapping("/bioDemo")
    @ResponseBody
    public Map<String, String> bioDemo() {
        System.out.println("bioDemo.do");
        new Thread(()->{
            list.add(Thread.currentThread().getId());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        //循环所有已接收的客户端，看看有没有传输数据的
        list.forEach(System.out::println);
        Map map = new HashMap();
        map.put("key",list);
        return map;
    }

    /**
     * NIO:单线程处理：为了不新建线程，不堵塞，内核实现了非阻塞机制，就是NIO;先看看是否接收到连接，
     * 如果有就放到一个clientList里收集起来，然后遍历所有客户端，看看是不是有客户端发起请求，如有有就处理请求。
     * 弊端：C10K问题，客户端不发请求也要进行循环，会出现很多空循环，空循环中会有receive调用，会出现用户态转内核态的过程，消耗系统资源。
     * @return
     */
    @RequestMapping("/nioDemo")
    @ResponseBody
    public Map<String, String> nioDemo() {
        System.out.println("nioDemo.do");
        list.add(Thread.currentThread().getId());
        //循环所有已接收的客户端，看看有没有传输数据的客户端
        list.forEach(System.out::println);
        Map map = new HashMap();
        map.put("key",list);
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

    /**
     * 同一时间只能有一个请求进入该方法
     * @param str
     */
    public synchronized  void lockDemo(String str ){
        System.out.println(str);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
