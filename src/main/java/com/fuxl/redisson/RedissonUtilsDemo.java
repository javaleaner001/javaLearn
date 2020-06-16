package com.fuxl.redisson;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RedissonUtilsDemo {
    private static final Logger logger = LoggerFactory.getLogger(RedissonUtilsDemo.class);

    public static final String LOCK_FLAG = "REDISSONLOCK_";
    private static RedissonClient redisson;

    private static RedissonClient getRedissonClient() {
        return ContextLoader.getCurrentWebApplicationContext().getBean(RedissonClient.class);
    }

    static {
        Config config = new Config();
//        config = Config.fromYAML(RedissonManager.class.getClassLoader().getResourceAsStream("redisson.yaml"));
       /* try {
            config = Config.fromJSON(RedissonUtils.class.getClassLoader().getResourceAsStream("redisson.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        config.useSingleServer()
                .setAddress("redis://139.196.73.236:6379")
                .setTimeout(10000)
                .setConnectionPoolSize(2000)
                .setConnectionMinimumIdleSize(2000).setPassword("YonYou1234");
        config.setLockWatchdogTimeout(3000);//看门狗，3000/3毫秒去看一次（Time/TimerTask定时器实现（见com.fuxl.redisson.TimeDemo））持有锁的线程有没有结束，如果没结束就再增加3000毫秒持有锁时间

//        serverConfig.setPassword("YonYou1234");
//        if(StringUtils.isNotBlank(redssionProperties.getPassword())) {
//            serverConfig.setPassword(redssionProperties.getPassword());
//        }
        redisson = Redisson.create(config);
    }

    private static RedissonClient getRedissonClientLocal() {
       /* Config config = new Config();
        config.useSingleServer()
                .setAddress("139.196.73.236:6379")
                .setTimeout(1000)
                .setConnectionPoolSize(200)
                .setConnectionMinimumIdleSize(200).setPassword("YonYou1234");
//        config.setLockWatchdogTimeout(3000);
//        serverConfig.setPassword("YonYou1234");
//        if(StringUtils.isNotBlank(redssionProperties.getPassword())) {
//            serverConfig.setPassword(redssionProperties.getPassword());
//        }
        return Redisson.create(config);*/
        if (redisson == null) {
            logger.info("RedissonClient init failed !");
            return null;
        }
        return (Redisson) redisson;
    }

    /**
     * 根据name上锁
     *
     * @param lockKey
     * @param leaseTime
     * @param unit
     */
    public static void lock(String lockKey, int leaseTime, TimeUnit unit) {
        RLock lock = getRedissonClientLocal().getLock(LOCK_FLAG + lockKey);
        lock.lock(leaseTime, unit);
    }

    /**
     * 根据name上锁
     *
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = getRedissonClientLocal().getLock(LOCK_FLAG + lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 根据name上锁
     *
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @param unit
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime, TimeUnit unit) {
        RLock lock = getRedissonClientLocal().getLock(LOCK_FLAG + lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 根据name解锁
     *
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        RLock lock = getRedissonClientLocal().getLock(LOCK_FLAG + lockKey);
        //如果锁被当前线程持有，则释放
        System.out.println("**lock.isLocked()**" + lock.isLocked() + "**lock.isHeldByCurrentThread()**" + lock.isHeldByCurrentThread());
        if (lock.isLocked()) {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    /**
     * 批量添加分布式锁
     *
     * @param keyList   锁定的keyList
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @param unit      时间单位
     * @return 返回上锁失败的keyList
     */
    public static List<String> tryLockBatch(List<String> keyList, int waitTime, int leaseTime, TimeUnit unit) {
        List<String> collect = keyList.stream().map((key) -> tryLockForBatch(key, waitTime, leaseTime, unit)).distinct().filter((result) -> !result.equals("")).collect(Collectors.toList());
        return collect;
    }

    private static String tryLockForBatch(String lockKey, int waitTime, int leaseTime, TimeUnit unit) {
        RLock lock = getRedissonClientLocal().getLock(LOCK_FLAG + lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit) == true ? "" : lockKey;
        } catch (InterruptedException e) {
            return lockKey;
        }
    }

    /**
     * 批量解锁
     *
     * @param keyList
     * @return 返回解锁失败的keyList
     */
    public static List<String> unLockBatch(List<String> keyList) {
        return keyList.stream().map((key) -> unlockForBatch(key)).distinct().collect(Collectors.toList());
    }

    private static String unlockForBatch(String lockKey) {
        RLock lock = getRedissonClientLocal().getLock(LOCK_FLAG + lockKey);
        //如果锁被当前线程持有，则释放
        System.out.println("**lock.isLocked()**" + lock.isLocked() + "**lock.isHeldByCurrentThread()**" + lock.isHeldByCurrentThread());
        if (lock.isLocked()) {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                return "";
            }
            return lockKey;
        } else {
            return lockKey;
        }
    }

    private static boolean tryLockForBatchTest(String lockKey, int waitTime, int leaseTime, TimeUnit unit) {
        RLock lock = getRedissonClientLocal().getLock(LOCK_FLAG + lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(()->{
            System.out.println("***lock****" + tryLock("a", 0, -1, TimeUnit.SECONDS));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            unlock("a");
        });
        t1.start();
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        t1.interrupt();
//        System.out.println("**t1 interrupt**");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(()->{
            System.out.println("***t2lock****" + tryLock("a", 4, -1, TimeUnit.SECONDS));
            unlock("a");
        }).start();



//        System.out.println("***lock****" + tryLockBatch(Arrays.asList("a", "b"), 0, -1, TimeUnit.SECONDS));
//        Thread.sleep(2000);
//        System.out.println("***unlock****" + unLockBatch(Arrays.asList("a", "b")));
/*
        new Thread(()->{
            System.out.println("***lock****"+tryLockBatch(Arrays.asList("a", "b"), 0, 10, TimeUnit.SECONDS));
            System.out.println("***unlock****"+unLockBatch(Arrays.asList("a", "b")));


        }).start();*/
        /*try
        {
            //获取锁，这里的key可以上面场景中的业务id
            tryLockBatch(Arrays.asList("a", "b"), 0, 10, TimeUnit.SECONDS);
            //具体要执行的代码....
        } catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            //释放锁
            unLockBatch(Arrays.asList("a", "b"));
        }*/
//        System.out.println(Arrays.asList("a", "b", "a").stream().map((key) -> tryLockForBatchTest(key, 2, 5, TimeUnit.SECONDS)).collect(Collectors.toList()));
//        RedissonClient redissonClientLocal = getRedissonClientLocal();
        /*RLock a = getRedissonClientLocal().getLock("a");
        RLock a1 = getRedissonClientLocal().getLock("a");
        RLock a2 = getRedissonClientLocal().getLock("a");

        try {
            System.out.println(a.tryLock(1, 5, TimeUnit.SECONDS));
            System.out.println(a1.tryLock(1, 5, TimeUnit.SECONDS));
            System.out.println(a2.tryLock(1, 5, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }
}
