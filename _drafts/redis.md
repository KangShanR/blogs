---
layout: "post"
title: "redis"
date: "2018-10-25 12:23"
---

# redis

> redis 存储在服务器中的使用。 remote dictionary service ，远程字典服务。

> 使用 spring 中封装好的获取 redis 数据的工具类可以方便地去进行 redis 数据操作。

> [first study reference](http://www.runoob.com/redis/redis-data-types.html)


## redis key

> 针对 redis 的键的操作命令。_不管这个 key 是 string 类型的 key 还是 hash list 等的 key 都同样地使用这些命令进行执行。_
-  当 redis 中的键不存在时，其值也不能通过 `get key` 来得到。
- 执行 `FLUDHDB` 命令后，将会让 redis 中的 **数据清空** 。
- `nx`： 在命令后面加上 `nx` 表示 不存在 not exist，用于安全型保存（当 key 不存在时再进行保存或者重命名）

命令：
- `ttl keyName` ttl(time to life) 键存活时间，单位秒。用于查找 key 的生命时间。
- `TYPE keyName` 查看 键 类型
- `expire second` 指定 key 生命长度，单位：秒
- `persist key` 持久化 key，将 key 的生命长度移除
- `dump key` 序列化 key 。_note:不知其意义_
- `randomkey` 随机返回一个 key
- `rename key newKey` 更名 key 为 newkey
- `renamenx key newkey` 当 newkey 不存在时，才 将 key rename 为 newkey。



## 数据类型

redis 支持数据类型包括：
- string(字符串，二进制安全型，所以图片/序列化的字符串都可以被 redis 存储，最大 512 mb)
- hash(哈希)
- list(列表)
- set(集合)
- zset(sorted set)

### string 类型

> 如上：图片/序列化的字符串都可以被存储在 redis 中，最大 512 MB

string 的存取

- 存命令： `SET key value`
- 读命令： `GET key`
- `append key value` 将 value 追加到 key 对应的 value 后面
- `decr key` 将 key 对应的数值减小 1 。如果非 数字型 value 会报错。
- `incr key` 加 1
- `mset key1 value1 [key value] ` 设置多个 单个键值对存储
- `msetnx key1 value1 [key value]` 当所有的 key 不存在时才进行存储

常用命令：
- `strlen key` from `string length` 获取字符串长度
- `setrange key offset value` 从指定偏移量开始覆写 value 到指定 key 的 value 中。offset 从 0 开始计算
- `mget key [key]` 获取多个 key 的 value

### hash

> 常用的数据类型，一个 hashkey 中可以存储多个 键值对 作为其 hash 表的值

存读命令
- 存：
    - 单个字段值对存储 `HSET key field value`
    - 多个字段值对存储 `HMSET key field value [field value]`
- 读： `HGET key field`

常用命令
- `hgetall key` 获取 hash 表中的所有的键值对
- `HEXISTS key field` 查询 hash 字段 是否存在

### List

> 使用起来像栈一样，先进后出 FILO

存读命令
- 存：`LPUSH listKey listField1 listValue1 listField2 listValue2 ...`
    - 存入单个键值对时会返回当前键值对的排序号，从 1 开始
- 读：`LRANGE listKey startNo endNo`
    - 此处与存入不同之处在于：开始序号从 0 开始

### set

> 字符串的集合

存读命令
- 存： `SADD setKey setValue1 setValue2`
    - 将返回存入数量，如果 value 已经存在，将返回 0 表示失败
- 读： `SMENBERS setKey` ，将返回集合所有的 value

### zset

> 有序集合，通过将每个value 配上 score 来进行排序。其中 value 不能重复， 但 score 可以。

存读命令
- 存： `ZADD zsetKey score zsetValue1`
    - 同样，如果 value 不存在则返回 1 表示 成功，如果 value 已经存在将返回 0 表示 失败(不论其 score 是否已经存在，都会返回 0)。
- 读： `ZRANGEBYSCORE  zsetKey start stop`


## redis 命令连接到主机

> 运行 redis-cli 命令就是打开客户端，使用客户端去连接 redis 主机： `redis-cli -h hostadrress -p portNo -a password`。也就是说使用些命令也一样可以连接到其他 redis 主机（可能使用密码）。

- 如果开户客户端连接主机时出现 中文乱码。可以在命令后加上参数： `--raw` ，即可解决。
    - `redis-cli -h host -p port -a password --raw`
