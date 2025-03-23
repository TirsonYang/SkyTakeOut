package com.sky.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//@SpringBootTest         注释掉表示每次启动项目时不会进行测试，如果需要测试，可以将注释去掉 注意，这里的注释和@Autowired注释同步，要么全部注释掉，要么全部取消注释
public class SpringDataRedisTest {
    //@Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate(){
        System.out.println(redisTemplate);
    }

    /**
     * 操作字符串类型的数据
     */
    @Test
    public void testString(){
        //set get setex setnx

        //set
        redisTemplate.opsForValue().set("city","北京");
        //get
        String city = (String) redisTemplate.opsForValue().get("city");
        System.out.println(city);
        //setex
        redisTemplate.opsForValue().set("code","1234",20, TimeUnit.SECONDS);
        //setnx
        redisTemplate.opsForValue().setIfAbsent("lock","1");
        redisTemplate.opsForValue().setIfAbsent("lock","2");
    }

    /**
     * 操作哈希类型的数据
     */
    @Test
    public void testHash(){
        //hset hget hdel hkeys hvals
        HashOperations hashOperations = redisTemplate.opsForHash();
        //hset
        hashOperations.put("100","name","Tom");
        hashOperations.put("100","age","20");
        hashOperations.put("100","haha","哈哈");
        //hget
        String name = (String) hashOperations.get("100", "name");
        System.out.println(name);
        //hdel
        hashOperations.delete("100","haha");
        //hkeys    封装到set集合中
        Set keys = hashOperations.keys("100");
        System.out.println(keys);
        //hvals    封装到List集合中
        List values = hashOperations.values("100");
        System.out.println(values);
    }

    /**
     * 操作列表类型的数据
     */
    @Test
    public void testList(){
        //lpush lrange rpop llen
        ListOperations listOperations = redisTemplate.opsForList();
        //lpush
        listOperations.leftPushAll("mylist","a","b","c","d","x");
        //range
        System.out.println(listOperations.range("mylist",0,-1));
        listOperations.leftPush("mylist","z");
        List mylist = listOperations.range("mylist", 0, -1);
        System.out.println(mylist);
        //rpop
        listOperations.rightPop("mylist");
        listOperations.range("mylist",0,-1);
        //llen
        System.out.println(listOperations.size("mylist"));
        listOperations.set("mylist",3,"m");


        listOperations.set("mylist",4,"111");
        System.out.println(listOperations.range("mylist",0,-1));
    }

    /**
     * 操作集合类型的数据
     */
    @Test
    public void testSet(){
        //sadd smembers scard sinter sunion srem
        SetOperations setOperations = redisTemplate.opsForSet();
        //sadd
        setOperations.add("set1","a","b","c","d");
        setOperations.add("set2","1","2","3","a","b","x");
        //smembers
        System.out.println(setOperations.members("set2"));
        //scard
        System.out.println(setOperations.size("set1"));
        //sinter
        System.out.println(setOperations.intersect("set1", "set2"));
        //sunion
        System.out.println(setOperations.union("set1", "set2"));
        //srem
        setOperations.remove("set1","a","b");

        System.out.println(setOperations.members("set1"));
    }

}
