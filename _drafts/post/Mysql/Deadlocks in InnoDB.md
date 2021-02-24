---
title: Deadlocks in InnoDB
layout: post
tag: [mysql, InnoDB, Deadlock]
categories: programming
description: Deadlock in InnoDB
date: "2021-1-12 22:31:00"
---

# Deadlocks in InnoDB

> [reference](https://dev.mysql.com/doc/refman/5.7/en/innodb-deadlocks.html)

当不同的事务因为持有其他事务需要的锁不能继续处理时的场景叫死锁。因为事务之间在等待对方的资源释放，但又不释放自己所持有的资源。

当多个事务通过语句 `SELECT ... FOR UPDATE` 或 `UPDATE` 对多张表中的行加锁，但加锁顺序不一致时会产生死锁。

为降低死锁产生的可能性：

- 使用事务而不要使用 `LOCK TABLES` 语句。
- 让 insert/update 事务尽量小，以保证事务打开的时间尽量短。
- 当不同的事务更新多张表或大量数据行时，在每个事务中使用相同的操作顺序（如：`SELECT ... FOR UPDATE`）；
- 为 `SELECT ... FOR UPDATE` 与 `UPDATE` 语句使用的 column 创建索引。

*事务隔离级别不影响死锁产生的可能性，因为事务隔离级别只改变读的行为，而死锁产生来源于写操作。*

当死锁产生且死锁检测打开（默认打开状态），InnoDB 会检测死锁状态并回滚其中一个事务。如果使用配置 `innodb_deadlock_detect` 关闭了死锁检测，发生死锁时，InnoDB 根据配置 `innodb_lock_wait_timeout` 回滚事务。所以就算应用逻辑是正确的，也必须处理这种事务必须重试的场景。使用命令行 `SHOW ENGINE INNODB STATUS` 可以查看 InnoDB 用户事务的最后一个死锁。如果事务结构或应用错误处理伴随着高频的死锁，使用 `innodb_print_all_deadlocks=1` 将所有的死锁信息打印到 mysqld 错误日志中。

## An InnoDB Deadlock Example

[reference](https://dev.mysql.com/doc/refman/5.7/en/innodb-deadlock-example.html)

> 一个死锁例子。其中有两个客户端分别访问按以下顺序执行命令。

1. clientA 创建表并在其中插入一条记录；
2. clientA 开启新事务并使用 `SELECT * FROM t WHERE id = 1 LOCK IN SHARE MODE` 查询出这条数据；
3. clientB 开启事务并执行删除这条数据的命令：`DELETE FROM t WHERE id = 1;`这个时候，因为 clientA 持有该行数据的 s-lock ，clientB 的 x-lock 不能立即获取，只会产生一个 x-lock 的请求放入到队列中。
4. clientA 这时执行 `DELETE FROM t WHERE ID = 1;`。这个时候产生死锁。产生原因：clientA 需要一个 x-lock，但 clientB 已经有一个 x-lock 请求在队列中同时它又在等待　clientA 释放其正持有 s-lock 。A 正持有的 s-lock 也因为 B 优先的 x-lock 请求而不能升级为 x-lock 。最终结果是，InnoDB 为其中一个 client 生成一个 error，并释放其所持有的锁。这个 client 返回错误：`ERROR 1213 (40001): Deadlock found when trying to get lock;
try restarting transaction`，同时，另外的 client 的锁请求得到响应而删除这行数据。

## Deadlock Detection

> [死锁检测](https://dev.mysql.com/doc/refman/5.7/en/innodb-deadlock-detection.html)

当死锁检测打开（默认），InnoDB 会自动检测死锁并回滚基中一个或多个事务以打断死锁链条。InnoDB 会尝试回滚最小的事务，事务的大小取决于 inserted,updated,deleted 的行数。

当 innodb_table_locks = 1 且 autocommit = 0 时， InnoDB 对表锁有感知，并且 InnoDB 之上的 MYSQL 层也感知行级锁。否则，InnoDB 不能对 MYSQL `LOCK TABLES` 语句加的表锁或 InnoDB 之外的存储引擎设置的锁进行死锁检测。这些场景下可以用设置系统变量 `innodb_lock_wait_timeout` 来解决。

如果 InnoDB 监听的 LASTED DETECTED DEADLOCK 输出包含 “TOO DEEP OR LONG SEARCH IN THE LOCK TABLE WAITS-FOR GRAPH, WE WILL ROLL BACK FOLLOWING TRANSACTION,” 的信息，这表明 wait-for list 上等待的事务数量超过了限制数量 200　。当等待列表事务数量超过 200 时直接当作死锁发生，并且尝试检查等待列表的事务是回滚过的。同样，如果等待列表上事务所拥有的加锁线程必须加超过 1000000 个锁也会产生这个错误。

### Disabling Deadlock Detection

在高并发系统中，死锁检测在一定数量线程等待相同的锁时会造成响应延迟。这种情况下，关闭死锁检测在死锁发生时根据 `innodb_lock_wait_timeout` 设置来回滚事务会更高效。关闭死锁检测使用 `innodb_deadlock_detect` 配置项。

