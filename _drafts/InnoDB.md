---
tag: "mysql, InnoDB"
date: "2020-12-29 15:58"
---

# InnoDB

> [官方文档](https://dev.mysql.com/doc/refman/5.7/en/innodb-locking-reads.html)
> [美团](https://tech.meituan.com/2014/08/20/innodb-lock.html)

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

### 意向锁

表级锁，用以标明此表中有数据正被锁。

### 记录锁

record_lock

用于锁住单行数据。

### 间隙锁

Gap-Lock

- 间隙锁是纯抑制性的，排它锁与共享锁在 gap-lock 这儿没有区别，同时，同一个 gap 可以有多个 gap-lock，只用来抑制其他事务在间隙插入新的数据。
- 在读已提交隔离级别下，间隙锁对查询、索引扫描无效，仅对外键约束、重复键检查有效。

### Next-Key Lock

> 后键锁，是一个 record-lock 与 gap-lock 的组合。

- record-lock 用以锁住索引所在行数据，而 gap-lock 锁住当前行前的间隙，以防止前面部分插入新的数据。*在可重复读的隔离级别下，用以防止幻读。由此可推断 InnoDB 索引 B+ tree 中叶子节点中的数据排列界限是左开右闭的，只有这样才做到只使用左界加间隙锁就可以防止幻读。同时插入已存在的数据时是从左边插入的。*

## 事务隔离级别

事务三种隐患

1. 脏读，事务A读到其他事务B未提交的内容，B中数据回滚或其他更新后，A就会读到脏数据。
2. 不可重复读，事务A分别在事务B（更改了数据）提交前后的数据，发现数据前后不一致。
3. 幻读，事务A读事务B（新插入了数据）提交后，发现前面读到的数据比之后读到的数据少了一部分。

### InnoDB 隔离级别

可以设置全局，也可以设置到 session ，设置后开启新的 session 即可生效。

1. 读未提交，事务未提交就可以读到数据更新，不能阻止以上三种任何隐患，直接读取事务中修改后的值，没有视图概念。
2. 读已提交，事务提交后才去读。事务在执行每一条查询前都会查询结果放到事务的视图中，可以防止脏读，不能防止不可重复读与幻读
3. 可重复读，事务在执行语句之初就去读取需要查询的数据并放在视图中，在事务提交之前保存之前都不更新此视图（**新插入的数据还是会再读取到视图中**）。所以这样可以防止不可重复读，不能防止幻读。
4. 串行化，事务之间排队执行，防止一切隐患。
