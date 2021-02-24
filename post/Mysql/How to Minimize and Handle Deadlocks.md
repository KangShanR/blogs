---
title: How to Minimize and Handle Deadlocks
layout: post
tag: [mysql, InnoDB, Deadlock]
categories: [Mysql]
description: How to Minimize and Handle Deadlocks?
date: "2021-1-13 22:6:00"
---

> [reference](https://dev.mysql.com/doc/refman/5.7/en/innodb-deadlocks-handling.html)
>
> 死锁最小化建立在[死锁检测](./Deadlocks%20in%20InnoDB.md)之上。

死锁是事务型中经典问题，如果出现死锁的频率不高不是一个危险的问题。通常来讲，需要在应用中为死锁场景下事务重试做好准备。<!--more-->

InnoDB 自动使用行级锁。可能仅仅是在插入修改单行就出现死锁，这是因为这些操作并非真正的原子操作，它们会自动地在插入或修改的 index record（可能多个）上设置锁。（设置锁并非直接加锁，而是先请求锁，获得锁后再持有锁。）

降低死锁概念的技术：

1. 每次都使用 `SHOW ENGINE INNODB STATUS` 命令查看最近死锁的来源，这样可以帮助微调应用避免死锁。
2. 如果高频出现死锁警告，打开 `innodb_print_all_deadlocks` 配置以收集更多 debug 信息。每个死锁的信息都记录在 MYSQL 的 error log 中。当完成 debug 后关闭此配置。
3. 应用中随便为死锁准备重新发布事务，死锁并不危险，只会重试。
4. 尽量精简事务以降低其发生冲突的可能。
5. 更新完相应的记录立即提交事务以降低其冲突的可能。实际操作中，不要让一个交互式的 mysql session 长时间不提交。
6. 如果使用锁读（locking read: SELECT ... FOR UPDATE/SELECT ... LOCK IN SHARE MODE），尝试使用更低的事务隔离级别，如：READ COMMITTED。
7. 当在同一个事务中修改多个表数据或单表多行数据时，保证每次（业务中不同的代码块）操作顺序的一致。这样事务会形成很好的队列而不出现死锁。例如：在应用中组织数据库的操作在 function 中或调用存储过程，而不是在应用不同的地方编写多个类似的增删改的 sql 语句。
8. 在表中添加合适的索引。这样可以减少查询扫描的 index records 记录，因而添加更少的锁。使用 [EXPLAIN SELECT](https://dev.mysql.com/doc/refman/5.7/en/explain.html) 来让 MYSQL server 决定查询的最适索引。
9. 尽量少加锁。如果允许 SELECT 语句返回老版本快照，就不要选择添加 FOR UPDATE/LOCK IN SHARE MODE 子句进入锁读。使用 READ COMMITTED 隔离级别对一致性读也有帮助，因为在事务中都能读取到最新的快照。
10. 如果别无他法，可以将事务序列化到表级锁上。使用示例（注意语句顺序）：

    ```sql
    SET autocommit=0;
    LOCK TABLES t1 WRITE, t2 READ, ...;
    ... do something with tables t1 and t2 here ...
    COMMIT;
    UNLOCK TABLES;
    ```

    表级锁阻止表上的并发更新，可以避免忙系统少响应的消耗上的死锁。
11. 另外一个序列化事务的方法是创建一个辅助标识表，这个表只有一条数据。使每一个事务在访问其他表前必须先更新这行数据。这样可以保证所有事务都排列执行。需要注意的是，InnoDB 死锁检测算法在这种场景下依然工作，因为这个序列化锁是个行级锁。在 MYSQL 表级锁情况下，死锁问题使用超时方法来解决。
