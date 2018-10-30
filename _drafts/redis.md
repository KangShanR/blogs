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
- 执行 `FLUDHDB` 命令后，将会让 redis 中当前 db 的 **数据清空** 。
- `nx`： 在命令后面加上 `nx` 表示 不存在 not exist，用于安全型保存（当 key 不存在时再进行保存或者重命名）

命令：
- `del key` key 存在时删除 key
- `exists key` key 是否存在
- `ttl keyName` ttl(time to life) 键存活时间，单位秒。用于查找 key 的生命时间。
- `TYPE keyName` 查看 键 类型
- `keys pattern` 查看当前库所有符合 pattern 的 key。`keys *` 表示获取所有的 key
- `expire key second` 指定 key 生命长度，单位：秒
- `persist key` 持久化 key，将 key 的生命长度属性移除
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
- 读：
    - `HGET key field` hash表中单个字段 value 的读取
    - `hmget key field [field]` 获取 hash 表中多个字段的 value

常用命令
- `hgetall key` 获取 hash 表中的所有的键值对
- `HEXISTS key field` 查询 hash 字段 是否存在
- `Hlen key` 哈希表中字段的数量
- `hkeys key` hash 表中的所有的 key
- `hvals key`获取 hash 表中所有 value
- `hsetnx key field value` 只有当字段 field 不存在时才进行存储
- `hincrby key field increment` 在指定的整数字段上加上增加值 increment
- `hincrbyfloat key field increment` 在指定的浮点数值字段上加上增值 increment

### List

> 使用起来像栈一样，先进后出 FILO。但在这儿的先后概念换为 左右 `LR`。先进为左，后入为右。
- `l` left 表示列表左部，即头部
- `r` right 表示列表右部，即尾部
- `x`exist 表示已存在的

存读命令
- 存：
    - `LPUSH listKey listField1 listValue1 [listField listValue]` 从左开始存
        - 返回当前 list 的 length
    - `Rpush key value [value]` 在 list 尾部插入数据
- 读：
    - `Lindex key index` 通过 index (从 0 开始)读取 list value
    - `LRANGE listKey startNo endNo`
        - 此处与存入不同之处在于：开始序号从 0 开始

常用命令：
- `lpushx key value [value]` 将 values 追加到一个 **已存在** 的列表头部
- `rpushx key value  [value]` 追加 values 到 **已存在** 列表尾部
- `linsert key BEFORE|AFTER pivot value` 在元素 pivot 前|后 插入 value（note：不要想了，没有 rinsert 的命令）
- `lrem key count value` 移除 list 中的等于 value 的 |count| 个元素，当 count 为正数时，从左往右计数，当 count 为负数时，从右往左计数
- `llen key` 获取列表长度
- `lpop key` 弹出第一个元素（返回此值 并 删除）
- `rpop key` 弹出最后一个元素
- ``


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


## redis config

> redis 配置相关。使用 redis-server 启动时，就需要指定其配置文件，一般在路径下（redis.windows.conf 文件）可以直接打开 配置文件进行更改，也可以在使用 redis-cli 客户端连接后使用命令进行查看/更改。

config 命令：
- `config get key` 查看 key 配置，如： `config get loglevel` 查看日志级别
- `config set key value` 配置相关 redis 配置。 `config set loglevel debug` 设置日志记录级别为 debug
- `config get *` 查看所有配置
- `config get databases` 查看数据库数量


## redis db

> redis db 指 redis 数据库，通常默认一个 redis 实例中有 16 个 db ，index  从 0 到 15 ，每个 db 之间不相互干扰。

命令：
- `select index` 命令行切换 db，当在命令行中切换到非 db0 的库时，命令行中可以直接看到库编号 `redis[1]` ，在 api jedis 中有相应的 api 来查询当前 db 编号。`jedis.getDB()`
- `flushdb` 清除当前 db 的所有数据，不会清除其他 db 的数据。
- `flushall` 清除当前 redis 实例的所有数据（慎用）。
