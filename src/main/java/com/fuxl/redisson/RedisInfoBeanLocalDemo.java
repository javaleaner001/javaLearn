package com.fuxl.redisson;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.params.SetParams;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Redis实现分布式锁
 */
@Component
public class RedisInfoBeanLocalDemo {
    private static final Logger logger = LoggerFactory.getLogger(RedisInfoBeanLocalDemo.class);
    //REDIS返回状态
    public final static String REDIS_KEY_STATUS_OK = "OK";
    //REDIS失效时长（秒）
    public final static String REDIS_FAILURE_TIME = "EX";
    //类型
    public final static String REDIS_RETURN_TYPE = "NX";

    @Value("${dms.redis.ip}")
    private String ip;
    @Value("${dms.redis.port}")
    private int port;
    @Value("${dms.redis.password}")
    private String password;

    private String getIp() {
        return ip;
    }

    private void setIp(String ip) {
        this.ip = ip;
    }

    private String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    private int getPort() {
        return port;
    }

    private void setPort(int port) {
        this.port = port;
    }

    private static volatile JedisPool jedisPool = null;

    private JedisPool getJedisPool() {
        if (jedisPool == null) {
            synchronized (RedisInfoBeanLocalDemo.class) {
                JedisPoolConfig jedisPoolConfig = getJedisPoolConfig();
                if (password != null && !"".equals(password)) {
                    // redis 设置了密码
                    jedisPool = new JedisPool(jedisPoolConfig, ip, port, 10000, password);
                } else {
                    // redis 未设置密码
                    jedisPool = new JedisPool(jedisPoolConfig, ip, port, 10000);
                }
            }
        }
        return jedisPool;
    }

    private static JedisPool getJedisPoolLocal() {
        if (jedisPool == null) {
            synchronized (RedisInfoBeanLocalDemo.class) {
                JedisPoolConfig jedisPoolConfig = getJedisPoolConfig();
                jedisPool = new JedisPool(jedisPoolConfig, "139.196.73.236", 6379, 10000, "YonYou1234");
            }
        }
        return jedisPool;
    }

    private static JedisPoolConfig getJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //向资源池借用连接时是否做连接有效性检测(ping)，无效连接会被移除
        jedisPoolConfig.setTestOnBorrow(true);
        //向资源池归还连接时是否做连接有效性检测(ping)，无效连接会被移除
        jedisPoolConfig.setTestOnReturn(true);
        //当资源池连接用尽后，调用者的最大等待时间(单位为毫秒)
        jedisPoolConfig.setMaxWaitMillis(10000);
        //最大连接数
        jedisPoolConfig.setMaxTotal(300);
        jedisPoolConfig.setMaxIdle(300);
        return jedisPoolConfig;
    }
   /* private Jedis getJedis() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        Jedis resource;
        if (password != null && !"".equals(password)) {
            // redis 设置了密码
            resource = new JedisPool(jedisPoolConfig, ip, port, 10000, password).getResource();
        } else {
            // redis 未设置密码
            resource = new JedisPool(jedisPoolConfig, ip, port, 10000).getResource();
        }
        return resource;
    }*/

    /**
     * 分布式锁
     *
     * @param key
     * @param value
     * @param exprieTime
     * @return value:没有相同的KEY，设置VALUE成功
     * null :存在相同KEY
     */
    public boolean setNxExprie(String key, String value, int exprieTime) {
        Jedis jedis = getJedisPool().getResource();
        String result = null;
        try {
//            result = jedis.set(key, value, REDIS_RETURN_TYPE, REDIS_FAILURE_TIME, exprieTime);
            result =  jedis.set(key,value,SetParams.setParams().ex(exprieTime).nx());
        } catch (Exception e) {
            closeResource(jedis, false);
            logger.error("*****REDIS SET KEY*****\nKEY:" + key + "\nVALUE:" + value + "\nresult:" + result);
        }
        logger.info("*****REDIS SET KEY*****\nKEY:" + key + "\nVALUE:" + value + "\nresult:" + result);
        closeResource(jedis, true);

        if (REDIS_KEY_STATUS_OK.equals(result)) {
            return true;
        }
        return false;

    }
    public boolean setNxExprieLocal(String key, String value, int exprieTime) {
        Jedis jedis = getJedisPoolLocal().getResource();
        String result = null;
        try {
//            result = jedis.set(key, value, REDIS_RETURN_TYPE, REDIS_FAILURE_TIME, exprieTime);
            result =  jedis.set(key,value,SetParams.setParams().ex(exprieTime).nx());
        } catch (Exception e) {
            closeResource(jedis, false);
            logger.error("*****REDIS SET KEY*****\nKEY:" + key + "\nVALUE:" + value + "\nresult:" + result);
        }
        logger.info("*****REDIS SET KEY*****\nKEY:" + key + "\nVALUE:" + value + "\nresult:" + result);
        closeResource(jedis, true);

        if (REDIS_KEY_STATUS_OK.equals(result)) {
            return true;
        }
        return false;

    }
    /**
     * 批量加锁
     * @param keyList
     * @param exprieTime
     * @return
     */
    public static Map setNxExprieBatch(List<String> keyList, int exprieTime) {

//        Jedis jedis = getJedisPool().getResource();
        Jedis jedis = getJedisPoolLocal().getResource();
        Map resultMap = new ConcurrentHashMap();
//        List<Map> result = null;
        try {
            if(Optional.ofNullable(keyList).isPresent()) {
//                keyList.forEach(key->jedis.set(key, UUID.randomUUID().toString(), REDIS_RETURN_TYPE, REDIS_FAILURE_TIME, exprieTime));
//                System.out.println(keyList.stream().map(key -> jedis.set(key, UUID.randomUUID().toString(), REDIS_RETURN_TYPE, REDIS_FAILURE_TIME, exprieTime)).collect(Collectors.toList()));
//                result = keyList.stream().map(key -> setNx(jedis, key, exprieTime,resultMap));
                keyList.stream().map(key -> setNx(jedis, key, exprieTime, resultMap)).collect(Collectors.toList());
//                System.out.println(resultMap);
//                Map<Long, User> maps = userList.stream().collect(Collectors.toMap(User::getId, Function.identity(), (key1, key2) -> key2));
//                Map<Long, String> maps = userList.stream().collect(Collectors.toMap(User::getId, User::getAge, (key1, key2) -> key2));
            }
        } catch (Exception e) {
            closeResource(jedis, false);
        }
        closeResource(jedis, true);
        return resultMap;
    }

    public static Map setNx(Jedis jedis,String key,int exprieTime ,Map resultMap){
        String value = UUID.randomUUID().toString();
//        String value2 = Thread.currentThread().getName();
//        jedis.set(key,value , REDIS_RETURN_TYPE, REDIS_FAILURE_TIME, exprieTime);
        String set = jedis.set(key, value, SetParams.setParams().ex(exprieTime).nx());
        Map map = new HashMap();
        map.put(key,value);
        System.out.println("**map**"+map+"**result:**"+set);
        return map;
    }

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("a", "b", "c","a");
       /* List<String> strings1 = Arrays.asList();
        List list = null;
        List list1 = new ArrayList();
        System.out.println(Optional.ofNullable(strings1).orElse(Arrays.asList("a")));
        System.out.println(Optional.ofNullable(strings1).orElseGet(()->Arrays.asList("a")));
        System.out.println(Optional.ofNullable(strings1).isPresent());
        System.out.println(Optional.ofNullable(strings).isPresent());
        System.out.println(Optional.ofNullable(list).isPresent());
        System.out.println(Optional.ofNullable(list1).isPresent());*/
        setNxExprieBatch(strings,5).forEach((key,value)-> System.out.println(key+":"+value));
//        System.out.println(UUID.randomUUID().toString());
//        System.out.println(Thread.currentThread().getName());



    }
    /**
     * 获取VALUE值
     *
     * @param key
     * @return
     */
    public String getValueByKey(String key) {
        Jedis jedis = getJedisPool().getResource();
        String value = null;
        try {
            value = jedis.get(key);
        } catch (Exception e) {
            closeResource(jedis, false);
            logger.error("*****REDIS GET KEY*****\nKEY:" + key + "\nVALUE:" + value);
        }
        closeResource(jedis, true);
        logger.info("*****REDIS GET KEY*****\nKEY:" + key + "\nVALUE:" + value);
        return value;

    }

    /**
     * 删除KEY
     *
     * @param key
     */
    public void delNxIfAbsense(String key, String value) {
        Jedis jedis = getJedisPool().getResource();
        try {
            String result = jedis.get(key);
            if (result != null && !"".equals(result) && value.equals(result)) {
                Long del = jedis.del(key);
                closeResource(jedis, true);
                logger.info("*****REDIS DELETE KEY*****\nKEY:" + key + "\nVALUE:" + value + "\nSTATUS:" + del);
            }
        } catch (Exception e) {
            closeResource(jedis, false);
            logger.error("*****REDIS DELETE KEY*****\nKEY:" + key + "\nVALUE:" + value);
        }

    }

    /**
     * 资源回收
     * @param jedis
     * @param isOK
     */
    public static void closeResource(Jedis jedis, boolean isOK) {
        if (null != jedis) {
           /* if (!isOK) {
                jedisPool.returnBrokenResource(jedis);
            } else {
                jedisPool.returnResource(jedis);
            }*/
        }
    }
}
