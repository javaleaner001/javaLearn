package com.fuxl.threadDemo.java_new;

import java.util.concurrent.*;

public class FutureDemo {

    public static String doSthLongOperation() throws InterruptedException {
        Thread.sleep(1000);
        return "123";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        long start = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis());
        Future<String> future = executorService.submit(FutureDemo::doSthLongOperation);
        System.out.println(System.currentTimeMillis() - start);
        System.out.println(future.get());
        System.out.println(System.currentTimeMillis() - start);
    }
}
