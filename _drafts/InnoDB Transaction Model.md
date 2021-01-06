---
tag: [mysql, InnoDB, commit]
categories: programming
description: The InnoDB transactions' commit & rollback
date: "2021-1-6 13:52:00"
---

# InnoDB Transaction Model

[reference](https://dev.mysql.com/doc/refman/8.0/en/innodb-transaction-model.html)

[InnoDB Isolation Level Note](./InnoDB%20Transaction%20Isolation%20Level.md)

## Autocommit commit rollback

[reference](https://dev.mysql.com/doc/refman/8.0/en/innodb-autocommit-commit-rollback.html)

> 在 InnoDB 中所有用户的活动都在事务之中，如果 autocommit＝ON 每个 SQL 语句都会形成一个自有的事务。默认情况下 Ｍysql 开启一个session都会在每个连接都会设置autocommit＝on，MYSQL 只要语句没有返回错误都会提交。

- 一个 session 可以在 autocommit=on 时在同一个事务中执行多个语句，只要显示地以 `START TRANSACTION` 或者 `BEGIN` 开始并以 `COMMIT`/`ROLLBACK` 结束语句。
- 如果 autocommit=0 , session 也设置了 autocommit=0 ，那么这个 session 总会有一个事务处于打开状态。`COMMIT` `ROLLBACK` 语句会结束当前事务并开始一个新的事务。
- 如果一个 session autocommit=0，没有显式地提交最后一个事务，MYSQL 会自动圆滚这个事务。
- 某些语句会[隐式地结束一个事务](https://dev.mysql.com/doc/refman/8.0/en/implicit-commit.html)，在你执行语句之前好像提交过一样。

## Consistent Nonlocking Reads

[reference](https://dev.mysql.com/doc/refman/8.0/en/innodb-consistent-read.html)

> 一致性读指 InnoDB 使用多视图给数据库某时间点快照响应查询。此查询能看到在此时间点前提交的事务的更新，在此时间点后提交的事务或未提交的更新不能被看到。此规则例外：本事务内之前语句的更新将被查询到。此类异常情况会造成：更新某表内部分行，SELECT 能够看到这些被更新的行，但也可以看到任何行的老版本。如果其他 sessions 同时更新了这张表，那么就等到了表在数据库内从未出现的状态。

- 如果在默认的 REPEATABLE READ 隔离级别下，在相同事务中所有的一致性读都会读取第一次查询建立的快照。若要获取到更新的快照，提交当前事务并开启一个新的。
- 在 READ COMMITTED 隔离级别下，同一个事务中每个一致性读的 SETS 与 READS 都在其自己的更新的快照中进行。
- InnoDB 在 REPEATABLE READ/READ COMMITTED 隔离级别下处理 SELECT 语句默认使用一致性读。一致性读在访问表数据时不加任何锁，所以其他 session 可以同时修改同一张表数据。
- 默认 REPEATABLE READ 隔离级别下，发起一个一致性读（普通 SELECT 语句），InnoDB 给事务的快照时间点(timepoint)取决于查询到达数据库，如果另外一个事务在此时间点后提交了插入、删除或更新，原事务是无感知的。

    > **Note**
    >
    > 在一个事务中，对 SELECT 语句使用这种数据库状态快照的做法并不需要使用在 DML 语句上。如果你在事务A插入修改了一些行并提交了事务，另外一个并发的 REPEATABLE READ 事务可以影响修改这些刚提交的行，尽管有可以这个并发的事务所在的 session 还不能查询到这些事务A刚提交的修改。
    >
    > 一个事务修改删除了刚被另一个事务提交的数据，这些修改对当前事务都会可见。

    ```sql
    SELECT COUNT(c1) FROM t1 WHERE c1 = 'xyz';
    -- Returns 0: no rows match.
    DELETE FROM t1 WHERE c1 = 'xyz';
    -- Deletes several rows recently committed by other transaction.

    SELECT COUNT(c2) FROM t1 WHERE c2 = 'abc';
    -- Returns 0: no rows match.
    UPDATE t1 SET c2 = 'cba' WHERE c2 = 'abc';
    -- Affects 10 rows: another txn just committed 10 rows with 'abc' values.
    SELECT COUNT(c2) FROM t1 WHERE c2 = 'cba';
    -- Returns 10: this txn can now see the rows it just updated.
    ```

- 若要将 timepoint 提前，可以提交当前事务，并开启另一个 `SELECT` / `START TRANSACTION WITH CONSISTENT SNAPSHOT`
- 这一切都叫做**多视图并发控制（multi-version concurrency control）**。

    ```sql
                 Session A              Session B

                SET autocommit=0;       SET autocommit=0;
    time
    |           SELECT * FROM t;
    |           empty set
    |                                   INSERT INTO t VALUES (1, 2);
    |
    v           SELECT * FROM t;
                empty set
                                        COMMIT;

                SELECT * FROM t;
                empty set

                COMMIT;

                SELECT * FROM t;
                ---------------------
                |    1    |    2    |
                ---------------------
    ```sql

- 如果需要数据库最新状态，要么使用 READ COMMITTED ，要么使用 locking read: `SELECT * FROM t FOR SHARE;`
    - 在 READ COMMITTED 隔离级别下，事务内所有的读写都使用使用自己的快照。而对于 FOR SHARE 将使用锁读（locking read），SELECT 将阻塞直到包含最新行数据的事务结束。
- 一致性读在特定的 DDL 语句中失效：
    - DROP TABLE 语句，因为 MYSQL 不能使用被删除的表且 InnoDB 将销毁此表。
    - ALTER TABLE 操作，此操作将复制表的临时拷贝并在临时拷贝建立成功时删除原表。当事务中重新发起一个一致性读，新表中的数据行在事务中快照并不存在。这种情况下事务会返回错误：ER_TABLE_DEF_CHANGED,"Table definition has changed, please retry transaction".
- 对子查询如：`INSERT INTO ... SELECT` `UPDATE ... (SELECT)` `CREATE TABLE ... SELECT` 并未指定加锁（for update / for share）:
    - InnoDB 默认对这引起语句使用更严格的锁且 `SELECT` 部分使用 READ COMMITTED （在相同事务内，一致性读 sets gets 都针对其自有快照）。
    - 执行非锁读的场景，设置事务隔离级别为 READ COMMITTED / READ UNCOMMITTED 以避免对所选表中数据行加锁。
