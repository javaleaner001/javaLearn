package com.fuxl.threadDemo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class DemoTest {
    private static Map<String, String> map = new HashMap();

    static {
        for (int i = 0; i < 10000; i++) {
            map.put(String.valueOf(i), String.valueOf(i));
        }
    }

    public static void main(String[] args) {
        try {
            callableDemo();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        threadDemo();
    }

    public static void threadDemo() {
        ExecutorService executorService = new ThreadPoolExecutor(10, 100, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(30000));
        final List<Student> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 10000; i++) {
            final int finalI = i;
            executorService.execute(new Thread(new Runnable() {
                @Override
                public void run() {
                    Student student = new Student();
                    student.setNo(String.valueOf(finalI));
                    setSth(student, getName(String.valueOf(finalI)));
                    list.add(student);
                }
            }
            ));
        }
        executorService.shutdown();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (Student student : list) {
            if (!student.getNo().equals(student.getName())) {
                System.out.println("==========" + student.getNo() + "!=" + student.getName());
            }
        }
    }

    private static void setSth(Student student, String name) {
        student.setName(name);
    }

    private static String getName(String i) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        System.out.println(map.get(i));
        return map.get(i);
    }

    public static class Student {
        public String name;
        public String no;

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static void callableDemo() throws InterruptedException {

//        new Thread(futureTask).start();
        ExecutorService executorService = new ThreadPoolExecutor(4, 8, 0, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(200));
        for (int i = 0; i < 10; i++) {
            FutureTask futureTask = new FutureTask(new Callable() {
                @Override
                public Object call() throws Exception {
                    System.out.println("===callable===");
                    Thread.sleep(50);
                    return Thread.currentThread().getName();
                }
            });
            System.out.println(i);
            /**
             * 线程池的submit和execute方法区别
             *1、接收的参数不一样
             * 2、submit有返回值，而execute没有
             *
             * 用到返回值的例子，比如说我有很多个做validation的task，我希望所有的task执行完，然后每个task告诉我它的执行结果，是成功还是失败，如果是失败，原因是什么。
             * 然后我就可以把所有失败的原因综合起来发给调用者。
             *
             * 个人觉得cancel execution这个用处不大，很少有需要去取消执行的。
             *
             * 而最大的用处应该是第二点。
             * 3、submit方便Exception处理
             * 意思就是如果你在你的task里会抛出checked或者unchecked exception，
             * 而你又希望外面的调用者能够感知这些exception并做出及时的处理，那么就需要用到submit，通过捕获Future.get抛出的异常。
             */
//            executorService.execute(new Thread(futureTask));
            Future<?> submit = executorService.submit(new Thread(futureTask));//线程池执行FutureTask方法
            try {
                if(submit.get()==null){
                    System.out.println("任务完成");
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            try {
                //阻塞方法futureTask.get()
                System.out.println("==" + futureTask.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();

        System.out.println("end");
    }
}
