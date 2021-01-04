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

> Intention lock，分为排他意向锁 IX 与共享意向锁 IS。

表级锁，用以标明此表中有数据正被锁。

### 记录锁

> record_lock

用于锁住单行数据。

### 间隙锁

Gap-Lock

- 间隙锁是纯抑制性的，排它锁与共享锁在 gap-lock 这儿没有区别，同时，同一个 gap 可以有多个 gap-lock，只用来抑制其他事务在间隙插入新的数据。
- 在读已提交隔离级别下，间隙锁对查询、索引扫描无效，仅对外键约束、重复键检查有效。

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

## 事务隔离级别

事务三种隐患

1. 脏读，事务A读到其他事务B未提交的内容，B中数据回滚或其他更新后，A就会读到脏数据。
2. 不可重复读，事务A分别在事务B（更改了数据）提交前后的数据，发现数据前后不一致。
3. 幻读，事务A读事务B（新插入了数据）提交后，发现前面读到的数据比之后读到的数据少了一部分。

### [InnoDB 隔离级别](https://dev.mysql.com/doc/refman/5.7/en/innodb-transaction-isolation-levels.html)

可以设置全局，也可以设置到 session ，设置后开启新的 session 即可生效。

1. READ UNCOMMITTED 读未提交，事务未提交就可以读到数据更新，不能阻止以上三种任何隐患，直接读取事务中修改后的值，没有视图概念。
2. READ COMMITTED 读已提交，事务提交后才去读。事务在执行每一条查询前都会查询结果放到事务的视图中，可以防止脏读，不能防止不可重复读与幻读
   1. 一致性非锁读(consistent non-locking reads,　普通查询)　事务内所有的 set get 都会写入到快照中，而读取到所有已经提交的数据。
   2. 锁读（locking reads, select ... for update/lock in share mode）/update/delete, InnoDB 只会锁索引记录，而不会在数据行前添加 gap-lock ，允许添加新数据行在锁行前（幻读）。这种隔离级别下 gap-lock 只会对外键约束检查与重复　key　检查生效。
   3. 数据库属性 `innodb_locks_unsafe_for_binlog`　（以下简称　locks_unsafe） 与 READ COMMITTED 效果一致，其区别在于：
      1. lock_unsafe 是全局配置，而　READ COMMITTED 可以设置到 session　级别
      2. lock_unsafe　只能数据库启动时设置，而 READ COMMITTED 可以在数据库服务器运行中设置。
3. REPEATABLE READ 可重复读，事务在执行语句之初就去读取需要查询的数据并放在视图中，在事务提交之前保存之前都不更新此视图（**新插入的数据还是会再读取到视图中**）。所以这样可以防止不可重复读，不能防止幻读。
   1. 对于一致性非锁读（普通查询），同一事务内会保持一致性，第一次读数据会产生快照。（如果查询条件不一样呢？会再查询出新的快照出来还是会对后面新查询进行优化先在已有快照里匹配呢？）
   2. 对于锁读（select ... for update/lock in share mode）/update/delete 语句，加锁的情况取决于查询条件：
      1. 唯一索引作为唯一查询条件进行查询：InnoDB 只会锁住查询到的唯一记录，不会添加 gap-lock
      2. 对于其他查询: InnoDB 会锁住扫描到的范围内所有索引记录，同时会在其前加上 next-key-lock 以阻止其他事务在其前间隙插入新的记录（幻读）。
4. SERIALIZABLE 串行化，事务之间排队执行，防止一切隐患。
