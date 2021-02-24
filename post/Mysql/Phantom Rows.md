---
title: Phantom Rows
layout: post
tag: [mysql, InnoDB, Lock]
categories: [Mysql]
description: Phantom rows && prevention
date: "2021-1-12 21:12:00"
---

# 1. Phantom Rows

> [reference](https://dev.mysql.com/doc/refman/5.7/en/innodb-next-key-locking.html)
>
> 在一个事务中执行相同的查询得到不同的数据谓之为*幻读*。比如：在一个 SELECT 两次执行中，第二次查询到一行第一次查询时没有行。这一行就叫*幻读行*。<!--more-->

假如表 child 中定义了索引行 id，现在需要读取 id 大于100并加锁以在后面更新一些行，其 sql 语句：`SELECT * FROM child WHERE id > 100 FOR UPDATE;`

假设现在这张表中有 id=90,102 两行数据，如果只加 index-lock 但不对 gap-lock （90,102]，其他 session 则可以插入新行 id=101。这个时候原事务中如果再执行查询语句将会读取到 id=101 这行新数据。如果我们将多行数据示为一个数据项，这条幻行数据就打破了事务运行的隔离原则（数据在事务运行期间不改变）。

InnoDB 使用 next-key lock 算法来阻止幻读。next-key lock 由 index-row lock 与 gap-lock 组成。当 InnoDB 执行查询或表索引扫描时设置 s-lock 或 x-lock 在其查询到（encounter）的 索引记录（index-record）上。因此这个 row-level lock 实际就是 index-record lock。此外还有一个 gap-lock 加在记录前的 gap 。

当 InnoDB 扫描一个索引时，也会在最后一个记录之后加上 gap-lock 。对上面的例子，为阻止其他 session 插入 id>100 的数据，InnoDB 会在 id=102 记录后的 gap 加上锁。

可以使用 next-key lock 实现唯一性检查：如果使用 share 模式读取未查询到想要插入的重复行，那就可以安全地插入行，next-key lock 会在读取时设置到行上以阻止同时想要插入的重复行。也就是说，next-key lock 锁住了表中并不存在的行。