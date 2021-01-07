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

## Locking Reads

[reference](https://dev.mysql.com/doc/refman/8.0/en/innodb-locking-reads.html)

如果你在同一个事务中查询并插入修改数据,常规的 SELECT 语句并不能提供足够的防御.其他事务能够修改或删除你查询到的数据.InnoDB 提供两种类型锁读 locking-read 以增强这类防御:

- SELECT ... FOR SHARE
    - 在读取到的行上设置一个共享模式的锁.其他 session 可以读取这些行,但不能修改,直到你的事务提交.如果查询的这些行被其他事务修改并未提交,查询将等待到这些事务结束而获取到最新的值.

        > **Note**
        >
        > `SELECT ... FOR SHARE` 是 `SELECT ... LOCK IN SHARE MODE` 的替换版本,但 `LOCK IN SHARE MODE` 保留了向后兼容性,两者相等.然后 `FOR SHARE` 支持 `OF table_name`, `NOWAIT`, `SKIP LOCKED` 选项.

    - 在 mysql 版本 8.0.22 之前, SELECT ... FOR SHARE 需要 `select` 权限与 DELETE/LOCK TABLES/UPDATE 权限之一.在 8.0.22 版本只需要 SELECT 权限.且在 8.0 前都语法是：`SELECT ... LOCK IN SHARE MODE`
    - MYSQL 8.0.22 , SELECT ... FOR SHARE 在 grant tables 上不再获取读锁.
- SELECT ... FOR UPDATE:
    - 对于查询到的索引记录,将锁住行和与其相关的索引项,与对这些行执行 UPDATE 语句一样.其他事务将被阻塞,这些事务可能是执行 `SELECT ... FOR SHARE`/更新这些行/某些隔离级别下的读取数据.一致性读将忽略在其视图中数据上的任何锁.(老版本记录不能被锁,对数据记录在内存备份应用 undo_log 重构能得到老版本数据.)
    - `SELECT ... FOR UPDATE` 需要 SELECT 权限加至少一个 `DELETE` `LOCK TABLES` `UPDATE` 权限.
- 这些子句主要在处理树状或图状结构数据时有用,要么单表要么跨表.You traverse edges or tree branches from one place to another, while reserving the right to come back and change any of these “pointer” values.
- 所有 SELECT FOR UPDATE 与 SELECT FOR SHARE 查询的锁都在事务提交或回滚时释放.

    > **Note**
    >
    > 锁读(locking read) 只有在 autocommit=0 时才能实现(要么使用 `START TRANSACTION`, 要么设置 `autocommit=0`)

- 外部语句中的锁读子句不会对子查询语句的数据行生效. eg:

    ```sql
    SELECT * FROM t1 WHERE c1 = (SELECT c1 FROM t2) FOR UPDATE;
    ```

    - 子查询需要单独加自己的锁读子句. eg:

        ```sql
        SELECT * FROM t1 WHERE c1 = (SELECT c1 FROM t2 FOR UPDATE) FOR UPDATE;
        ```

### Locking Read Examples

> 假设你想在表 child 中插入一条新数据,并保证子表数据在父表 parent 表中有相应的行.你的应用代码能保证以下操作相对完整.

- 首先,使用一致性读取 parent 表中数据以验证其数据是存在的,但还现在插入新数据到 child 表并不安全.因为其他 session 可以在查询 parent 表与插入 child 表两次操作之间将所查询到的数据删除掉.为避免此问题需要执行 SELECT ... FOR SHARE :

    ```sql
    SELECT * FROM parent WHERE NAME = 'Jones' FOR SHARE;
    ```

    - 使用 FOR SHARE 查询语句将等待其他修改 parent 数据的事务执行完,在此之后读取到 parent 数据后将加锁对后来的删除修改操作阻塞到当前事务在 child 表中添加数据完成.
- 另一个场景: child_codes 表中有个 counter 整数计数字段用以指定 child 表中的 id .这时就算是使用 FOR SHARE 查询此字段一样会有问题.因为多个事务会读取到相同的 counter 值,使用相同的值作为 id 插入到 child 表会触发 duplicate-key error,同时当这些事务更新 counter 字段时至少有一个会以死锁收场(多个事务去更新 counter 字段但因为都执行 FOR SHARE 查询而进入等待彼此释放锁.*业务开发中得慎用 SELECT ... FOR SHARE ,因为只要在事务中先查询再更改,只要有并发就很大可能死锁*).
    - SELECT ... FOR UPDATE  将读取最新可得的数据,并在读到的行上加上排他锁.因此其回锁类似 UPDATE 语句.使用此类锁读即可解决上述问题:

        ```sql
        SELECT counter_field FROM child_codes FOR UPDATE;
        UPDATE child_codes SET counter_field = counter_field + 1;
        ```

    - 上述场景在 MYSQL 中可以通过单次访问表实现生成唯一 id:

        ```sql
        UPDATE child_codes SET counter_field = LAST_INSERT_ID(counter_field + 1);
        SELECT LAST_INSERT_ID();
        ```

        - 其中 SELECT 语句仅仅是获取当前连接的 id 信息,不访问任何表.

### Locking Read Concurrency with NOWAIT and SKIP LOCKED

> MYSQL 8.0 版本才开始有此选项。
>
> 使用 SELECT ... FOR UPDATE / SELECT FOR SHARE 在查询被其他事务锁住的行时必须等待到这些事务释放锁,这类规则在你想查询请求快速结束与可以接受被锁的查询目标不被返回到结果集的场景中是不恰当的.为满足以上两种场景,可以在 SELECT FOR UPDATE / SELECT FOR SHARE 中添加选项: NOWAIT / SKIP LOCKED。

- NOWAIT，不等待被锁住的行，直接返回失败。
- SKIP LOCKED,跳过被锁住的行，返回的结果集中不包括被锁住的行。

    > Note
    >
    > 使用 SKIP LOCKED 返回数据不能保证一致性。因此对于一般的事务不适用，但在多 session 访问类队列的表时可以用来避开锁的概念。

- NOWAIT 与 SKIP LOCKED 都只应用在行级锁上。
- NOWAIT 与 SKIP LOCKED 语句对于基于复制的语句并不安全。
- EXAMPLE：

    ```sql
    # Session 1: 查询并锁住 2

    mysql> CREATE TABLE t (i INT, PRIMARY KEY (i)) ENGINE = InnoDB;

    mysql> INSERT INTO t (i) VALUES(1),(2),(3);

    mysql> START TRANSACTION;

    mysql> SELECT * FROM t WHERE i = 2 FOR UPDATE;
    +---+
    | i |
    +---+
    | 2 |
    +---+

    # Session 2: NOWAIT 并发查询使用 而直接返回错误

    mysql> START TRANSACTION;

    mysql> SELECT * FROM t WHERE i = 2 FOR UPDATE NOWAIT;
    ERROR 3572 (HY000): Do not wait for lock.

    # Session 3: SKIP LOCKED 并发查询路过 2

    mysql> START TRANSACTION;

    mysql> SELECT * FROM t FOR UPDATE SKIP LOCKED;
    +---+
    | i |
    +---+
    | 1 |
    | 3 |
    +---+
    ```
