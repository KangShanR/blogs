---
layout: "post"
title: "redis"
date: "2018-10-25 12:23"
---

> redis 存储在服务器中的使用。 remote dictionary service ，远程字典服务。
> 使用 spring 中封装好的获取 redis 数据的工具类可以方便地去进行 redis 数据操作。
> [first study reference](http://www.runoob.com/redis/redis-data-types.html)

## redis key

> 针对 redis 的键的操作命令。_不管这个 key 是 string 类型的 key 还是 hash list 等的 key 都同样地使用这些命令进行执行。_

- 当 redis 中的键不存在时，其值也不能通过 `get key` 来得到。
- 执行 `FLUSHDB` 命令后，将会让 redis 中当前 db 的 **数据清空** 。
- `nx`： 在命令后面加上 `nx` 表示 不存在 not exist，用于安全型保存（当 key 不存在时再进行保存或者重命名）

### 通用命令

不管其数据类型都可以使用的命令

- `del key` key 存在时删除 key，可有指定多个 key 删除
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

### string

> 如上：图片/序列化的字符串都可以被存储在 redis 中，最大 512 MB

string 的存取

- 存命令： `SET key value`
- 读命令： `GET key`
- `append key value` 将 value 追加到 key 对应的 value 后面
- `decr key` 将 key 对应的数值减小 1 。如果非 数字型 value 会报错。
- `decrby key num` 减少指定数，右面数值类型会报错。
- `incr key` 加 1
- `incrby key num` 增加指定数，只能对可转为整数的数据进行运算
- `mset key1 value1 [key value]` 设置多个 单个键值对存储
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

> 使用起来像 queue 队列一样，具有 FIFO 特性。但在这儿的先后概念换为 左右 `LR`。先进为左，后入为右。

- `l` left 表示列表左部，即头部
- `r` right 表示列表右部，即尾部
- `x` exist 表示已存在的
- `nx` not exist 不存在

存读命令

- 存：
  - `LPUSH listKey listField1 listValue1 [listField listValue]` 从左开始存
  - `Rpush key value [value]` 在 list 尾部插入数据
  - `LSET key index value` 设置 list 指定 index 的 value
- 读：
  - `Lindex key index` 通过 index (从 0 开始)读取 list value
  - `LRANGE listKey startNo endNo`
    - 此处与存入不同之处在于：开始序号从 0 开始
    - 如果 end index 越界超过最大 index 不会有任何报错
    - 使用 `lrange listkey 0 -1` 查看所有的 value

常用命令：

- `lpushx key value [value]` 将 values 追加到一个 **已存在** 的列表头部
- `rpushx key value  [value]` 追加 values 到 **已存在** 列表尾部
- `linsert key BEFORE|AFTER pivot value` 在元素 pivot 前|后 插入 value（note：不要想了，没有 rinsert 的命令，且只会在第一个 value 前后位置 insert ，其余的 value 不管）
- `lrem key count value` 移除 list 中的等于 value 的 |count| 个元素，当 count 为正数时，从左往右计数，当 count 为负数时，从右往左计数。当 count 为 0 时，删除所有的
- `llen key` 获取列表长度
- `lpop key` 弹出第一个元素（返回此值 并 删除）
- `rpop key` 弹出最后一个元素
- `LSET key index value` 设置 list 指定 index 的 value
- `LTRIM key start stop` 修剪 list ，start 与 stop 都是其 index
- `RPOPLPUSH source destination` 将 source 列表的尾部元素移动到 destination 首部。这里的 source 与 destination 可以为同一个 list

### set

> 字符串的集合

存读命令

- 存： `SADD setKey setValue1 setValue2`s
  - 将返回存入数量，如果 value 已经存在，将返回 0 表示失败
- 读： `SMENBERS key` ，将返回集合所有的 value

常用命令：

- `SMEMBERS key` 返回集合所有成员
- `SCARD key` 返回 set 的成员数量
- `SDIFF set1 [set2]` 返回 set1 中有但 set2 中没有的集，如果只有一个 key ，则返回此 set 与 空 set 的差集
- `SDIFFSTORE destination key1 [key2]` 查两个 set 的差集 并将其存储在 目标 set destination 中（差集为空时不会创建出目标 set）
- `SINTER key1 [key2]` 求两个集合的交集
- `SINTERSTORE destination key1 [key2]` 求两个集合的交集并存储在 destination 中
- `SISMEMBER key member` 判断 member 是否在 集合中
- `SPOP key` 随机弹出一个集合成员
- `SMOVE source destination member` 将 member 从一个集合 source 移动到另一个集合 destination
- `SDANDMEMBER key [count]` 返回集合一个或指定个成员
- `SREM key member1 [member2]` 移除集合一个或多个成员
- `SUNION key1 [key2]` 返回给定的集合的并集
- `SUNIONSTORE destination key1 [key2]` 返回给定集合的并集并存储在指定集合 destination 中
- `SSCAN key cursor [MATCH pattern] [COUNT count]` 迭代集合（_没弄懂这两个参数的使用_）

### zset

> 有序集合，通过将每个value 配上 score 来进行排序。其中 value 不能重复， 但 score 可以。

存读命令

- 存： `ZADD zsetKey score zsetValue1` 同样，如果 value 不存在则返回 1 表示 成功，如果 value 已经存在将返回 0 表示 失败(不论其 score 是否已经存在，都会返回 0)。
- 读： `ZRANGE zsetKey start stop` 通过权重查找指定序列内的元素。`start 0 end -1` 返回全部

常用命令：

> 大部分命令与 set 差不多，但多了个 score 的维度，用于各个数据权重比较。

- `ZSCORE key member` 返回集合成员的 score
- `ZADD key score1 member1 score2 member2` 添加元素，如果元素已经存在，会使用新 score 替换原 score，返回值是添加的个数，不含修改 score 的；
- `ZSCORE key member` 返回指定元素的 score；
- `ZCARD key` 返回指定集合的成员数量；
- `ZREM KEY members` 移除集合中指定成员（可多个）；
- `ZRANGE key start end [withscores]` 获取指定排序内的元素， withscores 用以加上 score
- `ZREVRANGE key start end [withscores]` 按元素权重反序取指定区间元素
- `ZREMRANGEBYRANK key start end` 按照排序删除指定区间的元素
- `ZREMRANGEBYSCORE key start end` 按照分数排序后删除指定区间元素
- `ZRANGEBYSCORE key min max [withscores] [limit offset count]` 按 score 升序排列并从 offset 处取 count 个元素
- `ZINCRBY key increment member` 为指定成员增加 score
- `ZRANK key member` 返回元素在集合中的排名
- `ZCOUNT key min max` 返回在集合中 score 在指定区间的元素数量

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

## redis db connnection

> redis db 指 redis 数据库，通常默认一个 redis 实例中有 16 个 db ，index  从 0 到 15 ，每个 db 之间不相互干扰。

命令：

- `PING` 测试 redis db 是否接通。接通将返回 `PONG`
- `QUIT` 关闭连接
- `AUTH password` 验证 password 是否正确，不正确将不能再执行命令
- `select index` 命令行切换 db，当在命令行中切换到非 db0 的库时，命令行中可以直接看到库编号 `redis[1]` ，在 api jedis 中有相应的 api 来查询当前 db 编号。`jedis.getDB()`
- `flushdb` 清除当前 db 的所有数据，不会清除其他 db 的数据。
- `flushall` 清除当前 redis 实例的所有数据（慎用）。
- `move key db2` 将 key 移到 db2 中
- `dbsize` 查询当前库中 key 数量
- `info` 获取服务器信息

## 消息订阅与发布

- `SUBSCRIBE channel` 订阅频道
- `PSUBSCRIBE channel*` 批量订阅频道
- `PUBLISH channel content` 发布内容到频道

## redis 事务

redis 事务与 mysql 事务概念完全不一样，并不会有回滚的说法，只是多个语句批量执行，如果其中某一个命令出现错误，并不影响其他命令的执行。同时，在 `execute` 执行前可以执行 `DISCARD` 取消事务。

命令

- `MULTI` 开启事务
- `EXEC` 提交事务
- `DISCARD` 回滚事务

### features

1. 事务中的命令保证是串行化执行
2. 事务执行时，redis server 不会为其他 client 提供任何服务，从而保证事务的原子性
3. 与关系型数据库不同之处：事务中有任何命令执行失败，不会影响其他命令的执行
4. 如果在 `EXEC` 命令提交后发生网络故障，事务中命令依然会被执行
5. Apenend-only 模式下 redis 调用系统函数 write 将事务中所有的命令写入磁盘。若在写入过程中出现系统崩溃，此时出现部分数据丢失，redis 服务器在重启时执行一致性检查，一旦发现类似问题会退出并提示错误，此时可使用工具包中的 redis-check-aof 工具进行定位不一致数据，交将写入数据回滚。修复之后可重启 redis server。

## redis 持久化

持久化，将内存中的数据存入磁盘以方便 redis 重启时数据恢复。

1. RDB 模式，默认，无需配置。指定间隔时间，将数据快照写入磁盘。
2. AOF 模式，以日志形式将 redis 每一个写操作记录，重启 redis server 时会读取日志重构数据库
3. 可同时指定以上两种方式。
4. 可以指定不持久化。

### RDB

优势

1. 容灾能力强；
2. 只需要操作时一个文件进行转移；
3. 性能优势。持久化时只需要 fork 出子进程，可以避免影响服务进程 IO 。
4. 数据集大时，启动效率高。

劣势

1. 始终会在间隔时间内出现数据丢失；
2. 当数据集大时，子进程同样会造成服务停止。

### AOF

优势

1. 三种同步方式可代选择：每修改同步（效率最低）、每秒同步、不同步；
2. 写日志使用 append 模式，在写时出现宕机也不会影响已经写好的数据；
3. 日志过大时可使用 rewrite 模式，会创建一个新的文件记录修改，再对修改数据写入到老磁盘，更好地保证数据安全性；
4. AOF 格式清晰可读性高，可直接通过该文件进行数据重建。

劣势

1. 文件过大，相比于 RDB；
2. 运行效率较低。

#### 配置 AOF

redis.conf 文件用于配置。

- `always` 修改即同步
- `everysec` 每秒同步
- `no` 不同步
- 手动同步：若不满足同步条件，可手动同步： `bgrewriteaof`
- 使用 aof 备份文件恢复数据：
  - 打开 appendonly.aof 文件，根据需要将不需要的命令删除，再启动 redis server 即可。

### 使用场景

1. 取最新的 n 个数据，使用 list 。
   1. 对每次新操作时行 `LPUSH latest.operation key`。
   2. 再 `LRANGE latest.operation 0 n`
2. 取排行榜上 TOP N
   1. 使用 zset
