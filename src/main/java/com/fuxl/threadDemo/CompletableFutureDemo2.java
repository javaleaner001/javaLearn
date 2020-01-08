package com.fuxl.threadDemo;

import com.sun.org.apache.xpath.internal.operations.Quo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class CompletableFutureDemo2 {
    public static List<Shop> shops = Arrays.asList(new Shop("1"), new Shop("2"),
            new Shop("3"), new Shop("4"));

    public static void main(String[] args) {
//        shops.forEach(shop -> System.out.println(shop.getName()));
        long start = System.currentTimeMillis();
//        System.out.println(getShopName());
//        System.out.println(getShopNameAsSync());
        System.out.println(getShopNameAsSync2());
        System.out.println("耗时：" + (System.currentTimeMillis() - start));

    }

    public static List getShopName() {
        return shops.stream().map(shop -> shop.getName()).collect(Collectors.toList());
    }

    public static List getShopNameAsSync() {
        Executor executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<String>> collect = shops.stream().map(shop -> CompletableFuture.supplyAsync(() -> shop.getName(), executor)).collect(Collectors.toList());
        return collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public static List getShopNameAsSync2() {
        Executor executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<String>> collect =
                shops.stream()
                        .map(shop -> CompletableFuture.supplyAsync(() -> shop.getName(), executor)
                        )
                        .map(future -> future.thenApply(Quote::parse))
                        .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Shop.getPrice())).thenApply(i -> i.toString()))
                        .collect(Collectors.toList());
//        等价于
        List<CompletableFuture<String>> collect1 = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> shop.getName(), executor).thenApply(Quote::parse)
                        .thenCompose(quote -> CompletableFuture.supplyAsync(() -> Shop.getPrice())).thenApply(i -> i.toString())
                )
                .collect(Collectors.toList());
        return collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    public static List getShopNameAsSync3() {
        Executor executor = Executors.newFixedThreadPool(10);
        List<CompletableFuture<String>> collect =
                shops.stream()
                        .map(shop -> CompletableFuture.supplyAsync(() -> shop.getName(), executor)
                        )
                        .map(future -> future.thenApply(Quote::parse))
                        .map(future -> future.thenCompose(quote -> CompletableFuture.supplyAsync(() -> Shop.getPrice())).thenApply(i -> i.toString()))
                        .collect(Collectors.toList());
        return collect.stream().map(CompletableFuture::join).collect(Collectors.toList());
//        return collect;
    }
}
