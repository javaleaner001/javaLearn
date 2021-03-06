package com.fuxl.threadDemo.java_new;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FunctionDemo {

    public static void main(String[] args) {
//        functionLearn();
//        Student student = new Student("Tom",1);
//        CompletableFuture.supplyAsync(()->student.getAge()).thenApply(age->age+10).thenCompose(age2->CompletableFuture.supplyAsync());

//        listCollectionUtils();

    }

    private static void listCollectionUtils() {
        //List差集、交集、并集
        List list1 = new ArrayList();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);

        List<Map<String,Integer>> list2 = new ArrayList();
        Map<String,Integer> map= new HashMap();
        map.put("a",4);
        map.put("b",5);
        Map<String,Integer> map1= new HashMap();
        map1.put("a",5);
        map1.put("b",5);
        list2.add(map);
        list2.add(map1);

//        System.out.println(list1.stream().filter(num ->! list2.contains(num)).collect(Collectors.toList()));
        System.out.println(list2.stream().filter(map2 -> !list1.contains(map2.get("a"))).collect(Collectors.toList()));
    }

    private static void functionLearn() {
        //compose等价于B.apply(A.apply(5))，而andThen等价于A.apply(B.apply(5))。
        System.out.println("**Function:compose等价于B.apply(A.apply(5))，而andThen等价于A.apply(B.apply(5))**");
        Function<Integer, Integer> function = i -> i * 10;
        Function<Integer, Integer> function2 = i -> i + 1;
        System.out.println(function.andThen(function2).apply(2));
        System.out.println(function.compose(function2).apply(2));
        //Consumer与Function类似，只是纯属消费，没有返回值
        System.out.println("**Consumer与Function类似，只是纯属消费，没有返回值**");
        Consumer consumer = System.out::println;
        Consumer consumer1 = i -> System.out.println(i + "aaa");
        consumer1.andThen(consumer).accept("1");
        //断言，判断条件
        System.out.println("**Predicate断言，判断条件**");
        Predicate<Integer> predicate = i -> i < 10;
        System.out.println(predicate.test(1));
        System.out.println(predicate.and(a -> a > 5).test(1));
        System.out.println(predicate.or(a -> a > 5).test(1));
        //用于简化Java中对空值的判断处理，以防止出现各种空指针异常。
        System.out.println("**Optional用于简化Java中对空值的判断处理，以防止出现各种空指针异常**");
        //  of() 和 ofNullable() 方法创建包含值的 Optional。两个方法的不同之处在于如果你把 null 值作为参数传递进去，of() 方法会抛出 NullPointerException：
        Optional.of(optionalDemo());
        Optional<String> optional = Optional.empty();
        optional = Optional.ofNullable(optionalDemo());
        //两个 Optional  对象都包含非空值，两个方法都会返回对应的非空值。不过，orElse() 方法仍然创建了 User 对象。与之相反，orElseGet() 方法不创建 User 对象。
        //在执行较密集的调用时，比如调用 Web 服务或数据查询，这个差异会对性能产生重大影响
        System.out.println(optional.orElseGet(FunctionDemo::optionalDemo));
        System.out.println(optional.orElse("1"));
        //fPresent() 方法。该方法除了执行检查，还接受一个Consumer(消费者) 参数，如果对象不是空的，就对执行传入的 Lambda 表达式
        optional.ifPresent(FunctionDemo::voidDemo);
        optional.isPresent();
        Optional.ofNullable(Arrays.asList("")).map(list->list.get(0)).orElse("");
        Optional.ofNullable(1).filter(value->value!=1).orElse(1);
        System.out.println(Optional.ofNullable(new Student("Tom", 12)).flatMap(student -> Optional.ofNullable(student.getAge())).map(age -> age + 2).filter(age -> age <20).orElseGet(()->20));//14
        System.out.println(Optional.ofNullable(new Student("Tom", 112)).flatMap(student -> Optional.ofNullable(student.getAge())).map(age -> age + 2).filter(age -> age <20).orElse(20));//20
        //Stream
        System.out.println("**Stream**");
        Stream<Integer> integerStream = Stream.of(1, 2);
//        integerStream.iterator().forEachRemaining(System.out::println);
        integerStream.limit(10).reduce(Integer::sum).ifPresent(System.out::println);
//        Stream.iterate(1L,n -> n <= 10L, n -> n+1L).forEach(System.out::println);
//        System.out.println(Stream.iterate(1L, i -> i + 1).limit(10).reduce(0L, Long::sum));
        System.out.println("**map 生成的是个 1:1 映射，每个输入元素，都按照规则转换成为另外一个元素。还有一些场景，是一对多映射关系的，这时需要 flatMap,flatMap 把 input Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，最终 output 的新 Stream 里面已经没有 List了，都是直接的数字**");
//      map 生成的是个 1:1 映射，每个输入元素，都按照规则转换成为另外一个元素。还有一些场景，是一对多映射关系的，这时需要 flatMap。
//      flatMap 把 input Stream 中的层级结构扁平化，就是将最底层元素抽出来放到一起，最终 output 的新 Stream 里面已经没有 List了，都是直接的数字
        Stream<List<Integer>> listStream = Stream.of(Arrays.asList(1), Arrays.asList(1, 2), Arrays.asList(1, 2, 3));
        System.out.println(listStream.flatMap(list -> list.stream()).collect(Collectors.toList()));//[1, 1, 2, 1, 2, 3]
//        System.out.println(listStream.map(list -> list.stream()).collect(Collectors.toList()));//[java.util.stream.ReferencePipeline$Head@514713, java.util.stream.ReferencePipeline$Head@1663380, java.util.stream.ReferencePipeline$Head@137e0d2]
        System.out.println("**partitioningBy 其实是一种特殊的 groupingBy，它依照条件测试的是否两种结果来构造返回的数据结构，get(true) 和 get(false) 能即为全部的元素对象**");
        //partitioningBy 其实是一种特殊的 groupingBy，它依照条件测试的是否两种结果来构造返回的数据结构，get(true) 和 get(false) 能即为全部的元素对象。
        List<Student> list = new ArrayList<>();
        list.add(new Student("TOM", 10));
        list.add(new Student("JACK", 15));
        list.add(new Student("LUYS", 10));
        list.add(new Student("TORRY", 20));
        Map<Integer, List<Student>> collect = list.stream().collect(Collectors.groupingBy(Student::getAge));
        /**
         * 20=[com.fuxl.threadDemo.java_new.FunctionDemo$Student@19af9a9]
         * 10=[com.fuxl.threadDemo.java_new.FunctionDemo$Student@187f9e0, com.fuxl.threadDemo.java_new.FunctionDemo$Student@5e91e4]
         * 15=[com.fuxl.threadDemo.java_new.FunctionDemo$Student@df8d8a]
         */
        collect.entrySet().iterator().forEachRemaining(System.out::println);
        Map<Boolean, List<Student>> collect1 = list.stream().collect(Collectors.partitioningBy(student -> student.getAge() >= 15));
        /**
         * false=[com.fuxl.threadDemo.java_new.FunctionDemo$Student@df8d8a, com.fuxl.threadDemo.java_new.FunctionDemo$Student@946e09]
         * true=[com.fuxl.threadDemo.java_new.FunctionDemo$Student@b0d902, com.fuxl.threadDemo.java_new.FunctionDemo$Student@5e91e4]
         */
        collect1.entrySet().iterator().forEachRemaining(System.out::println);
    }


    public static String optionalDemo() {
        return "optionalDemo";
    }

    public static void voidDemo(String str) {
        System.out.println("voidDemo");
    }

    static class Student {
        private int age;
        private String name;

        public Student(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
