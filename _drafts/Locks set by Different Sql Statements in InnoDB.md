---
tag: [mysql, InnoDB, lock, SQL]
categories: programming
description: Locks set by different SQL in the InnoDB
date: "2021-1-7 10:4:00"
---

# Locks set by different SQL in the InnoDB

> [reference](https://dev.mysql.com/doc/refman/8.0/en/innodb-locks-set.html)
>
> 对于一个　locking－read、UPDATE、DELETE 的处理过程中通常只对被扫描到的索引记录加锁，不管 where 条件中其他条件所排除的行。InnoDB 不记忆具体 WHERE 条件，只记忆被扫描的索引范围。所加之锁一般为 next－key lock ，除对表记录行加锁外还在其前加上 gap－lock 。gap－lock 可被显示地禁用造成 next-key lock 失效，另外事务隔离级别也会影响锁。

- 如果二级索引在查询中被使用且不设置 record_lock ，InnoDB 将会查出聚簇索引并把锁加在上面。
- 如果语句中没有合适的索引，MYSQL 处理语句时必须扫描全表，表中每行数据将被锁住，这就意味着其他用户的插入与更新都会被阻塞。所以恰当的索引对于查询性能举足轻重。
- InnoDB 加锁类型：
    - SELECT ... FROM 是一致性读，一般情况下只读取数据库快照，不会加锁。在事务隔离级别为 SERIALIZABLE 时会被转换成 LOCK IN SHARE MODE，查询将会在其找到的记录前加上 next-key lock 。如果使用唯一索引查询单条记录，就只需要一个 index-record lock。
    - SELECT ... FOR UPDATE/SELECT ... LOCK IN SHARE MODE被扫描的索引记录都将加锁，并期望尽快地释放不匹配 WHERE 条件的行的锁。在某些场景，结果集与源数据之间的关系在查询中丢失，将导致这些行的锁不能立即释放。
