---
layout: "post"
title: "redis in java"
date: "2018-10-31 15:42"
---

# redis in java (jedis)

> 在 java 中使用 redis 连接，集成包：Jedis

使用说明：
在依赖包中加入 Jedis 包：
```
<dependency>
			<groupId>redis.clients</groupId>
			<artifactId>jedis</artifactId>
            <version>2.9.0</version>
</dependency>
```


## 基础连接使用

使用测试：
```
//jedis
        Jedis  localhost = new Jedis("10.28.6.14", 6380);//connection host & port
        localhost.auth("evcspass");//设置连接 password
        //执行命令
        String ping      = localhost.ping();
        System.out.println(localhost.select(0));;
        System.out.println(ping);
        System.out.println(localhost.keys("*"));
        System.out.println(localhost.getDB());
```


## 使用连接池连接使用

> 凡有连接，必存在着资源池的使用，打开了连接需要在不使用时需要关闭连接释放资源（如同 mysql 数据 连接）。所以， Jedis 连接就存在着 JedisPool 连接池的使用。

```
private static ShardedJedisPool pool;
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(50);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        // 集群
        JedisShardInfo jedisShardInfo1 = new JedisShardInfo("120.26.1.1", 6379);
        jedisShardInfo1.setPassword("123456888888");
        List<JedisShardInfo> list = new LinkedList<JedisShardInfo>();
        list.add(jedisShardInfo1);
　　　　 pool = new ShardedJedisPool(config, list);
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();
        String keys = "myname";
        String vaule = jedis.set(keys, "lxr");
        System.out.println(vaule);
    }
```
