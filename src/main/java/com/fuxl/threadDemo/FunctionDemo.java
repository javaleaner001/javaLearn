package com.fuxl.threadDemo;

import java.util.function.Function;

public class FunctionDemo {

    public static void main(String[] args) {
        //compose等价于B.apply(A.apply(5))，而andThen等价于A.apply(B.apply(5))。
        Function<Integer,Integer> function = i->i*10;
        Function<Integer,Integer> function2 = i->i+1;
        System.out.println(function.andThen(function2).apply(2));
        System.out.println(function.compose(function2).apply(2));

    }
}
