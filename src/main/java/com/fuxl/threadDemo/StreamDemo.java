package com.fuxl.threadDemo;

import java.util.function.Function;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamDemo {
    public static void main(String[] args) {
        test(StreamDemo::parallelSum, 1000000);
        test(StreamDemo::sequenceSum, 1000000);
        test(StreamDemo::itorSum, 1000000);
    }


    public static long test(Function<Long, Long> computer, long n) {
        long fastest = Long.MAX_VALUE;
        long value = Long.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            long start = System.currentTimeMillis();
            //将给定参数应用于此函数
            Long apply = computer.apply(n);
           /* Function<Long,Long> function =a->a*10L;
            computer.andThen(function).apply(n);*/
            long cost = System.currentTimeMillis() - start;
            if (cost < fastest) {
                fastest = cost;
                value = apply;
            }
        }
        System.out.println(value + ":" + fastest);
        return fastest;
    }

    /**
     * 并行
     *
     * @param n
     * @return
     */
    public static long parallelSum(long n) {
//        return Stream.iterate(1L, i -> i + 1).limit(n).parallel().reduce(0L, Long::sum);
        return LongStream.rangeClosed(0,n).parallel().reduce(0L, Long::sum);
    }

    public static long sequenceSum(long n) {
//        return Stream.iterate(1L, i -> i + 1).limit(n).reduce(0L, Long::sum);
        return  LongStream.rangeClosed(0,n).reduce(0L, Long::sum);
    }

    public static long itorSum(long n) {
        long result = 0;
        for (int i = 0; i <= n; i++) {
            result += i;
        }
        return result;
    }


}
