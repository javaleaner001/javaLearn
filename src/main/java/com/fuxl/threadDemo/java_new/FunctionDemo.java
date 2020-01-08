package com.fuxl.threadDemo.java_new;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FunctionDemo {

    public static void main(String[] args) {
        //compose等价于B.apply(A.apply(5))，而andThen等价于A.apply(B.apply(5))。
        Function<Integer, Integer> function = i -> i * 10;
        Function<Integer, Integer> function2 = i -> i + 1;
        System.out.println(function.andThen(function2).apply(2));
        System.out.println(function.compose(function2).apply(2));
        //Consumer与Function类似，只是纯属消费，没有返回值
        Consumer consumer = System.out::println;
        Consumer consumer1 = i -> System.out.println(i + "aaa");
        consumer1.andThen(consumer).accept("1");
        //断言，判断条件
        Predicate<Integer> predicate = i -> i < 10;
        System.out.println(predicate.test(1));
        System.out.println(predicate.and(a -> a > 5).test(1));
        System.out.println(predicate.or(a -> a > 5).test(1));
        //用于简化Java中对空值的判断处理，以防止出现各种空指针异常。
        Optional<String> optional = Optional.empty();
        System.out.println(optional.orElseGet(FunctionDemo::optionalDemo));
        optional = Optional.ofNullable(optionalDemo());
        optional.ifPresent(FunctionDemo::voidDemo);

        Stream<Integer> integerStream = Stream.of(1, 2);
//        integerStream.iterator().forEachRemaining(System.out::println);
        integerStream.limit(10).reduce(Integer::sum).ifPresent(System.out::println);
//        Stream.iterate(1L,n -> n <= 10L, n -> n+1L).forEach(System.out::println);
//        System.out.println(Stream.iterate(1L, i -> i + 1).limit(10).reduce(0L, Long::sum));
    }


    public static String optionalDemo() {
        return "optionalDemo";
    }

    public static void voidDemo(String str){
        System.out.println("voidDemo");
    }
}
