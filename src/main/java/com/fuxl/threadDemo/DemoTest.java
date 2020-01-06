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
        for (int i = 0; i < 100; i++) {
            FutureTask futureTask = new FutureTask(new Callable() {
                @Override
                public Object call() throws Exception {
                    System.out.println("===callable===");
                    Thread.sleep(500);
                    return Thread.currentThread().getName();
                }
            });
            System.out.println(i);
            executorService.execute(new Thread(futureTask));
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
