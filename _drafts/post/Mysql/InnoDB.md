---
title: InnoDB
layout: post
tag: [mysql, InnoDB, InnoDB Locking]
categories: programming
description: The InnoDB Glossary
date: "2020-12-29 15:58:00"
---

# InnoDB

> [官方文档](https://dev.mysql.com/doc/refman/5.7/en/innodb-locking-reads.html)
>
> [美团文档](https://tech.meituan.com/2014/08/20/innodb-lock.html)
>
> [查看Innodb引擎数据](https://dev.mysql.com/doc/refman/8.0/en/show-engine.html)

## Locking Readings

> [锁读](https://dev.mysql.com/doc/refman/5.7/en/innodb-locking-reads.html)

## 并发写问题

> [CSDN](https://www.cnblogs.com/fengzheng/p/12557762.html)

- 在并发写同一行数据时，如果 where 条件字段没有加索引，innoDB 会对所有行加行锁，再对条件进行筛选，不符合条件的行再释放锁。一锁一放损耗极大，所以建议适当添加索引。
- 在可重复读隔离级别下，InnoDB 加间隙锁可以防止幻读，同理，如果没有索引将会把所在行之外所有列都加上间隙锁，而导致范围外的行也需要阻塞到当前锁释放才能插入。
- 意向锁是表锁，只用来表示该表有没行正被锁住，而以免有行被锁住时想要加表锁修改表结构，需要每行查询是否有行锁。

### Consistent Read

> [一致性读](https://dev.mysql.com/doc/refman/8.0/en/glossary.html#glos_consistent_read)

- 在事务中使用一致性读可以避免并发，在读已提交和可重复读隔离级别中，默认是使用一致性读的。

## Multi-Versioning

> [多版本](https://dev.mysql.com/doc/refman/8.0/en/innodb-multi-versioning.html)

- 覆盖索引 cover-index 在查询的字段都在索引结构中，而不需要通过回表查询主键
    - 普通二级索引就是一种覆盖索引，其除索引字段外还包括了主键。
    - 当二级索引记录被标记为删除或被其他事务更新，覆盖索引技术不能被应用。

## InnoDB Locking

> [锁](https://dev.mysql.com/doc/refman/8.0/en/innodb-locking.html)

### 共享锁与排他锁

> Shared and Exclusive Locks。Innodb 实现两种标准行锁：shared（S） lock 与 exclusive（X） lock。

- S 锁允许获取到锁的事务读取该行
- X 锁允许获取到锁的事务修改、更新该行
- 如果一个事务T1获取到行 r 的 S 锁，另一个事务T2需要获取 r 的锁的规则：
    - T2请求获取 r 的 S 锁，能够马上得到
    - T2 请求获取 r 的 X 锁，不能立即成功
- 如果 T1 获取到的是 r 的X 锁，其他事务想要获取 r 的任何类型锁都需要等待 T1 释放了 r 的锁才行。

### 意向锁

> Intention lock，分为排他意向锁 IX 与共享意向锁 IS。用以支撑 InnoDB 多粒度锁控制（行锁与表锁同时存在）。
> `LOCK TABLES ... WRITE` 语句持有指定表的排他 X 锁。

- 表级锁，用以标明事务在之后的行操作上所需要的锁类型。
- 意向锁的两种形式：
    - IS 表明一个事务要设置一个 S 锁到表中某（多）行数据；
    - IX 表明一个事务要设置一个 X 锁到表中某（多）行数据。
- `SELECT ... FOR SHARE` 将设置一个 IS 锁，而 `SELECT ... FOR UPDATE` 将设置一个 IX 锁。
- 意向锁协议：
    - 事务获取行 S 锁前需要获取该表的一个 IS 锁或更强的锁 （IX）；
    - 事务要获取表中行的 X 锁 前，需要先取得该表的一个 IX 锁。
- 表级锁兼容性矩阵表：

|   |X       |IX        |S         |IS        |
|:-:|--------|----------|----------|----------|
|X  |conflict|conflict  |conflict  |conflict  |
|IX |conflict|compatible|conflict  |compatible|
|S  |conflict|conflict  |compatible|compatible|
|IS |conflict|compatible|compatible|compatible|

- 如果一个事务想获取一个已存在的锁且不能被获取到，将产生**死锁**。
- 意向锁不阻塞其他，但若要请求全表除外（`LOCK TABLES ... WRITE`）。

### 记录锁

> record_lock：`select * from t where f = 10 for update;`

- 用于锁住单行数据，阻止其他事务**插入**、**更新**、**删除**（不阻止其他事务读取） f ＝ 10 的行。
- record_lock 只会锁索引记录，如果表中没有建索引，Innodb 将会创建一个隐藏的聚簇索引，并使用此索引来锁住行。

### 间隙锁

> Gap Locks, 间隙锁是加在索引记录之间的锁，或在间隙记录之前或之后。`SELECT c1 FROM t WHERE c1 BETWEEN 10 AND 20 FOR UPDATE;`，此语句所加的间隙锁阻止了其他事务的插入一个 t.c1 = 15 的行，不管表 t 中是否已有一条同值记录，因为间隙锁将锁住范围内的所有行。

- **使用唯一索引作为查询条件的单行查询是不需要添加间隙锁的**（查询条件包含多个键组成的唯一索引中部分列名除外）。eg：对于列 id 有唯一索引的表执行的语句：`SELECT * FROM t WHERE id = 10;` 只会添加 record lock 到行，不管是否有其他会话在该行间隙前插入新行。如果 id 列并未加唯一索引，此语句将锁住间隙前。
- 间隙锁是纯抑制性的，排它锁与共享锁在 gap-lock 这儿没有区别，同时，同一个 gap 可以有多个 gap-lock，**只用来抑制其他事务在间隙插入新的数据**。冲突的锁能在 gap 这儿并存的原因是如果索引记录被清除，记录上不同事务的间隙锁必须被合并。
- 在读已提交隔离级别下，间隙锁对查询、索引扫描无效，仅对外键约束、重复键检查有效。
    - 在 READ COMMITTED 隔离级别下，不匹配行的 record－lock 在 mysql 计算出 where 条件后就释放。对于 UPDATE 语句，Innodb 只保持半一致性：返回最近提交的版本给 Mysql，以让 Mysql 可以决定各行是否满足 UPdate 条件。

### Next-Key Lock

> 后键锁，是一个 record-lock 与 gap-lock 的组合。

- record-lock 用以锁住索引所在行数据，而 gap-lock 锁住当前行前的间隙，以防止前面部分插入新的数据。
- 默认情况下,InoDB 在可重复读的隔离级别下查询与索引扫描会使用 Next-Key lock 防止幻读。*由此可推断 InnoDB 索引 B+ tree 中叶子节点中的数据排列界限是左开右闭的，只有这样才做到只使用左界加间隙锁就可以防止幻读。同时插入已存在的数据时是从左边插入的。*
- 假设某索引包括值: 10,11,13,20 .那么在此索引上可能被 next-key lock 锁住的区间就包括:(负无穷大, 10] (10,12] (11,13] (13,20] (20,正无穷大) 4个区间
    - 对于任何一个区间, next-key lock 会用一个 record-lock 锁住其右界值,同时在右界值到左界(不包含)的区间加上 gap-lock 防止插入新值.
    - 对于最后一个区间, InnoDB 会使用一个伪最大值 supremum (大于此索引中任何一个值), 这个 supremum 并不真实存在于索引中,所以对于最后一个区间只使用了 gap-lock.

### Insert intention locks

> 插入意向锁, 用在插入操作中插入行前设置的一种间隙锁. 此锁用来标识插入同一个索引间隙的多个事务之间如果不插入在同一个位置,那么事务之间无需等待彼此.eg:假设现有索引值 4 与 7 ,另有两个事务尝试各自插入 5 与 6 .**插入意向锁的获取优先于插入行的排他锁获取**,但彼此不阻塞因为各行并不冲突.

对于包含一个索引值有 90 和 102 的表,client A 先在 id > 100 的索引记录上添加了排他锁,这个排他锁包含了 (90, 102] 的 gap-lock

```sql
mysql> CREATE TABLE child (id int(11) NOT NULL, PRIMARY KEY(id)) ENGINE=InnoDB;
mysql> INSERT INTO child (id) values (90),(102);

mysql> START TRANSACTION;
mysql> SELECT * FROM child WHERE id > 100 FOR UPDATE;
```

client B 执行:

```sql
mysql> START TRANSACTION;
mysql> INSERT INTO child (id) VALUES (101);
```

client B 在间隙中插入新数据,这个事务将会拿到 insert-intention-lock 同时等待获取排他锁.

### Auto-inc Locks

> 自增锁,事务在插入包含自增列 AUTO_INCREMENT 的表数据时所持有的表级锁.在最简单的场景,如果一个事务在往表中插入数据,其他插入数据的事务必须等待,以保证前事务能获取到连续的主键值.

innodb_autoinc_lock_mode 配置控制自增锁的算法.此配置值用以在自增值的有序性与插入操作的并发性上平衡.

```sql
CREATE PROCEDURE reTry(IN count INT, OUT res INT)
BEGIN
  DECLARE v1 INT DEFAULT 0;
  WHILE v1 < count DO
    UPDATE test SET value = value + 1;
    set v1 = v1 + 1;
  END WHILE;
END;
```
