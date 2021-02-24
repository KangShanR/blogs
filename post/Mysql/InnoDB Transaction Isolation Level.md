---
title: InnoDB Transaction Isolation Level
layout: post
tag: [mysql, InnoDB]
categories: [Mysql]
description: The InnoDB Glory
date: "2021-1-6 10:59:00"
---

> [InnoDB 事务模型](https://dev.mysql.com/doc/refman/8.0/en/innodb-transaction-model.html)
>
> InnoDB 事务模型的目标是联合数据库多视图属性与传统的多阶段锁。InnoDB 类似 Oracle ，其执行锁到行级别，同时默认执行查询不加锁并保持一致性读。InnoDB 锁数据存储空间效率高，不随锁数量增加而剧增。锁表全行也不用担心 InnoDB 内存耗尽。<!--more-->
>
> [InnoDB 隔离级别](https://dev.mysql.com/doc/refman/5.7/en/innodb-transaction-isolation-levels.html)
>
> Transactions Isolation Levels, 事务隔离级别是多个事务在同一时间执行查询、修改时，微调性能、可用性、一致性、数据复用性的平衡手段。

事务三种隐患

1. 脏读，事务A读到其他事务B未提交的内容，B中数据回滚或其他更新后，A就会读到脏数据。
2. 不可重复读，事务A分别在事务B（更改了数据）提交前后的数据，发现数据前后不一致。
3. 幻读，事务A读事务B（新插入了数据）提交后，发现前面读到的数据比之后读到的数据少了一部分。

可以设置全局，也可以设置到 session ，设置后开启新的 session 即可生效。

## .1. REPEATABLE READ

可重复读，事务在执行语句之初就去读取需要查询的数据并放在视图中，在事务提交之前保存之前都不更新此视图（**新插入的数据还是会再读取到视图中**）。所以这样可以防止不可重复读，不能防止幻读。

1. 对于一致性非锁读（普通查询），同一事务内会保持一致性，第一次读数据会产生快照。（如果查询条件不一样呢？会再查询出新的快照出来还是会对后面新查询进行优化先在已有快照里匹配呢？）
2. 对于锁读（select ... for update/lock in share mode）/update/delete 语句，加锁的情况取决于查询条件：
   1. 唯一索引作为唯一查询条件进行查询：InnoDB 只会锁住查询到的唯一记录，不会添加 gap-lock
   2. 对于其他查询: InnoDB 会锁住扫描到的范围内所有索引记录，同时会在其前加上 next-key-lock 或 gap－lock 以阻止其他事务在其间隙插入新的记录（幻读）。

## .2. READ COMMITTED

读已提交，事务提交后才去读。事务在执行每一条查询前都会查询结果放到事务的视图中，可以防止脏读，不能防止不可重复读与幻读

1. 一致性非锁读(consistent non-locking reads,　普通查询)　事务内所有的 sets and reads 都在其快照中。
2. 锁读（locking reads, select ... for update/lock in share mode）/update/delete, InnoDB 只会锁索引记录，而不会在数据行前添加 gap-lock ，因而允许了添加新数据行在锁行前（幻读）。这种隔离级别下 gap-lock 只会对外键约束检查与重复　key　检查生效。
   1. 此隔离级别下 gap－lock 关闭，只有基于行的二进制日志被支持。在 READ COMMITTED 隔离下，且 binlog_format=MIXED ，服务器自动使用基于行的日志 。
3. 使用 READ COMMITTED 隔离级别两个额外效果：
   1. 对于 UPDATE／DELETE 语句，InnoDB 只对更新、删除的行加锁。对于未匹配的行的 record－lock 在 MYSQL 计算出 where 条件后即释放。这可以降低死锁发生的概率。
   2. 对于 UPDATE 语句，如果某行数据已经被锁，InnoDB **执行半一致性读**返回最新提交的到 MYSQL，这样 MYSQL 才能用以决定该行是否满足 WHERE 条件。如果某行匹配，MYSQL 会再次读取此行，且这次 InnoDB 要么锁住要么等待此行的锁。
4. 数据库属性 `innodb_locks_unsafe_for_binlog`　（以下简称　locks_unsafe） 与 READ COMMITTED 效果一致，其区别在于：
   1. lock_unsafe 是全局配置，而　READ COMMITTED 可以设置到 session　级别;
   2. lock_unsafe 只能数据库启动时设置，而 READ COMMITTED 可以在数据库服务器运行中设置。

### .2.1. 案例

```sql
CREATE TABLE t (a INT NOT NULL, b INT) ENGINE = InnoDB;
INSERT INTO t VALUES (1,2),(2,3),(3,2),(4,3),(5,2);
COMMIT;
```

创建表 t ，但**没有索引**，所以**查询与索引扫描使用记录锁隐藏的聚簇索引而非索引列**。

```sql
# Session A
START TRANSACTION;
UPDATE t SET b = 5 WHERE b = 3;
```

session A 执行更新语句。

```sql
# Session B
UPDATE t SET b = 4 WHERE b = 2;
```

session B 在 session A 后执行更新语句。

- InnoDB 执行每一个 UPDATE 会先获取每一行的排他锁，然后决定是否更改，如果 InnoDB 发现了不需要更改的行会立即释放其锁，而其他需要更改的行的锁需要等待到事务结束释放。而其作用的事务进程如下：
    - 当使用 REPEATABLE READ 隔离级别时
        - 第一个 UPDATE 会获取所有行的 x-lock 且不释放。

            ```sql
            x-lock(1,2); retain x-lock
            x-lock(2,3); update(2,3) to (2,5); retain x-lock
            x-lock(3,2); retain x-lock
            x-lock(4,3); update(4,3) to (4,5); retain x-lock
            x-lock(5,2); retain x-lock
            ```

        - 第二个 UPDATE 执行时一尝试获取锁立即被阻塞，因为第一个 UPDATE 正持有所有的锁，直到第一个 UPDATE 提交或回滚：

            ```sql
            x-lock(1,2); block and wait for first UPDATE to commit or roll back
            ```

    - 当在 READ COMMITTED 隔离级别下
        - 第一个 UPDATE 在获取了所有行锁后会释放其不需要修改的行锁：

            ```sql
            x-lock(1,2); unlock(1,2)
            x-lock(2,3); update(2,3) to (2,5); retain x-lock
            x-lock(3,2); unlock(3,2)
            x-lock(4,3); update(4,3) to (4,5); retain x-lock
            x-lock(5,2); unlock(5,2)
            ```

        - 第二个 UPDATE 执行半一致性读，返回每行被提交的最新版本到 MYSQL ，以让 MYSQL 能决定这些行是否匹配 UPDATE 条件：

            ```sql
            x-lock(1,2); update(1,2) to (1,4); retain x-lock
            x-lock(2,3); unlock(2,3)
            x-lock(3,2); update(3,2) to (3,4); retain x-lock
            x-lock(4,3); unlock(4,3)
            x-lock(5,2); update(5,2) to (5,4); retain x-lock
            ```

- 如果 **WHERE 条件中包含了索引列**，InnoDB 会使用索引，只有索引列被纳入获取释放记录锁的考虑范围。eg:

    ```sql
    CREATE TABLE t (a INT NOT NULL, b INT, c INT, INDEX (b)) ENGINE = InnoDB;
    INSERT INTO t VALUES (1,2,3),(2,2,4);
    COMMIT;

    # Session A
    START TRANSACTION;
    UPDATE t SET b = 3 WHERE b = 2 AND c = 3;

    # Session B
    UPDATE t SET b = 4 WHERE b = 2 AND c = 4;
    ```

    - 第一个 UPDATE 将获取并持有 b=2 的行，而第二个 UPDATE 在获取相同记录的 x-lock 时被阻塞。

## .3. READ UNCOMMITTED

- SELECT 语句执行不加锁，但早期版本可能会在行上加锁。使用用此隔离级别不能保证读一致性，俗称“脏读”。而在其他方面此隔离级别与 READ COMMITTED 一致。*读未提交，事务未提交就可以读到数据更新，不能阻止以上三种任何隐患，直接读取事务中修改后的值，没有视图概念。*

## .4. SERIALIZABLE

> 串行化，事务之间排队执行，防止一切隐患。

串行化类似 REPEATABLE READ ，但如果 autocommit=OFF ,InnoDB 会隐式地将普通 SELECT 语句转换成 SELECT ... FOR SHARE 。如果 autocommit=ON ,SELECT 在自己的事务中，这是只读且如果执行一致性读（不加锁）能被串行化的，并且也不需要为其他事务加锁。如果需要强迫一个普通 SELECT 为其他事务修改的行阻塞，关闭 autocommit 。
