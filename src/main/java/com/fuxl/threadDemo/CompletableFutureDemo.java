package com.fuxl.threadDemo;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Runnable类型的参数会忽略计算的结果
 * Consumer是纯消费计算结果，BiConsumer会组合另外一个CompletionStage纯消费
 * Function会对计算结果做转换，BiFunction会组合另外一个CompletionStage的计算结果做转换。
 */
public class CompletableFutureDemo {

    public static Random random = new Random(10);

    public static void main(String[] args) {
        try {
//            test();
//            testCompletableFuture();
            consumer();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 转换
     * thenAccept
     * thenCombine
     * thenCompose
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public static void testCompletableFuture() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> completableFuture = CompletableFuture.supplyAsync(() -> Shop.getPrice()
        /*{
            return Shop.getPrice();
        }*/);
        CompletableFuture<Integer> completableFuture2 = CompletableFuture.supplyAsync(() ->
        {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 100;
        });
        //thenApplyAsync/thenApply
        /**
         * thenAccept和thenRun都是无返回值的。如果说thenApply是不停的输入输出的进行生产，那么thenAccept和thenRun就是在进行消耗。
         * 它们是整个计算的最后两个阶段。
         * 同样是执行指定的动作，同样是消耗，二者也有区别：
         * thenAccept接收上一阶段的输出作为本阶段的输入
         * thenRun根本不关心前一阶段的输出，根本不不关心前一阶段的计算结果，因为它不需要输入参数
         */
        CompletableFuture<String> stringCompletableFuture = completableFuture.thenApplyAsync(i -> i * 10).thenApply(integer -> integer.toString());
        /**
         * thenCombine:此阶段与其它阶段一起完成，进而触发下一阶段,thenCombine整合两个计算结果
         * thenCombine的功能更类似thenAcceptBoth，只不过thenAcceptBoth是纯消费，它的函数参数没有返回值，而thenCombine的函数参数fn有返回值
         */
        CompletableFuture<Double> stringCompletableFuture1 = completableFuture.thenCombine(completableFuture2, (x, y) -> x + y);
        System.out.println(stringCompletableFuture1.get());
        //thenCompose等上一结果返回后进行下一步逻辑
        CompletableFuture<Double> doubleCompletableFuture = completableFuture.thenCompose(i -> CompletableFuture.supplyAsync(() -> i * 10));
//        System.out.println(doubleCompletableFuture.get());
    }

    /**
     * 消费
     * thenAccept只对结果执行Action，而不返回新的计算值
     */
    public static void consumer() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> completableFuture = CompletableFuture.supplyAsync(() -> Shop.getPrice()/*{
            return 100;
        }*/);
        //异步，有延时，调用get方法时获取延迟返回
        CompletableFuture<Void> voidCompletableFuture1 = completableFuture.thenAccept(System.out::println);
        //
        CompletableFuture<Void> voidCompletableFuture = completableFuture.thenAcceptBoth(CompletableFuture.supplyAsync(() -> Shop.getPrice()), (x, y) -> System.out.println(x * y));
        //TODO applyToEither
//        completableFuture.applyToEither(CompletableFuture.supplyAsync(()->Shop.getPrice(),i -> ));
        //只执行结果，不返回，所以报错
//        completableFuture.thenAcceptBoth(CompletableFuture.supplyAsync(()->Shop.getPrice()),(x,y)->x*y);
        /*CompletableFuture<Void> voidCompletableFuture1 = completableFuture.thenRun(() -> {
            System.out.println(100);
        });*/
        System.out.println(voidCompletableFuture.get());
    }

    /**
     * thenRun
     * allOf/anyOf
     * handle/whenComplete/exceptionally
     * get/join
     */
    public static void otherCompletableFutureMethods(){
        //运行thenRun更彻底地，下面一组方法当计算完成的时候会执行一个Runnable，与thenAccept不同，Runnable并不使用CompletableFuture计算的结果。

        //批量allOf/anyOf

        //终止操作handle/whenComplete/exceptionally

        //通过阻塞或者轮询的方式获得结果get在遇到底层异常时，会抛出受查异常ExecutionException。/join在遇到底层的异常时，会抛出未受查的CompletionException。
    }

    public static void test() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        Future<Double> priceAsSync = getPriceAsSync("");
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
        double price = priceAsSync.get();
    }

    public static Future<Double> getPriceAsSync(String product) {
        return CompletableFuture.supplyAsync(() -> getPrice(product));
    }

    public static double getPrice(String product) {
        delay();
        return random.nextDouble() * 100.;
    }

    public static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
