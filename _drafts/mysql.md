---
layout: "post"
title: "mysql"
date: "2018-11-01 10:13"
---

## mysql server & client

> mysql 部署 server 及 client 请求

### mysql server

> mysql server 的部署及请求[参考](http://www.cnblogs.com/QingXiaxu/p/7987302.html)

localhost 的 testing 尝试：

- 下载 mysql 安装包，解压后将 bin 添加到环境变量中。
- 在其根目录添加 data 文件夹及 my.ini 配置文件
- 初始化 mysql 服务 `mysqld --initialize --user=mysql --console`
  - 初始化后，data 文件夹中会生成相应的各个文件
  - 生成临时账户及密码： root@localhost: W>pg*LYv>6up
- 添加 MySQL 服务到电脑系统服务中： `sc create MySQL binPath= "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysqld.exe"`
  - 添加错了时，可以使用命令删除服务 `sc delete serviceName`
- 启动服务 `NET START MySQL`（到此，MySQL 的 server 算是启动完成，可以使用 client 命令进行登录访问了）
- mysql 服务： `net stop MySQL`
- 删除 mysql 服务（需要先停止服务）： `mysqld -remove`
  - 以上添加 mysql 到电脑服务系统步骤可以不用做一样可以启动 mysql 服务：
    - 需要的操作是：控制台进入到 mysql 安装目录 bin 中
      - 直接启动 mysqld.exe `mysqld --console` --console 打开日志记录
      - 关闭服务：`mysqladmin -uroot shutdown`
- 查看当前 mysql server 连接情况命令： `show processlist` 将会看到当前 mysql server 连接的各个 client 的 ip/port/status/user/db 等

### mysql client operate

> 在 mysql 控制台操作数据库

_note: mysql server 命令行都必须使用 `;`  或 `\g` 结尾，否则不会执行。_

- 登录：`mysql -h hostadrress -u username [-p]` -p 决定是否带上密码
登录全更改初始密码：
- `SET PASSWORD FOR 'root'@'localhost' = PASSWORD('password');`
- `UPDATE mysql.user SET authentication_string = PASSWORD('password'), password_expired = 'N' WHERE User = 'root' AND Host = 'localhost';`
  - （office laptop sql root password: `111`; desktop mysql database root password:''）
  - 从此行更改用户密码的命令可以得出：
    - `mysql` 库 `user` 表是存放用户的表
    - 其中的 `aythentication_string` 字段用于存放密码
    - 密码格式：PASSWORD('password')
    - 需要 **注意** 的是： mysql 5.7 后不再支持 PASSWARD() 函数，取而代之的是 MD%()
- 退出控制台：`exit|quit`

常用命令：

- 查找库内所有的表名： `SELECT table_name FROM information_schema.tables WHERE table_schema = 'dabataseName' AND table_type='base TABLE';`
- 选择库：`USE database`
- 查看库内所有表名：`SHOW TABLES;`
- 添加库： `CREATE DATABASE db_name character set {字符集};`
- 删除库： `DROP database;`
- 删除表：`DROP TABLE (TABLE name);`
- 查看库创建信息 ： `show create database (database name);`
- 查看表创建信息： `show create TABLE (TABLE name);`
- 查看表所有 column : `SHOW COLUMNS FROM (TABLE name);`
- 查看表所有 index : `SHOW INDEX FROM TABLE`
- 查看表状态信息： `SHOW TABLE STATUS [FROM db_name] [LIKE 'TABLE'] [\G]`
- 查看表：`desc (table_name);`
- 修改表名：`rename TABLE 表名 to 新表名;`

### mysql 服务添加新用户

> 当 mysql 服务初始化完成并启动后，登录 root 账户后可以在 client 使用命令添加用户，只需要在 mysql 库 user 表中插入数据即可。

执行命令：

```sql
INSERT INTO user
  (host, user, password,select_priv, insert_priv, update_priv)
  VALUES ('localhost', 'guest', PASSWORD('guest123'), 'Y', 'Y', 'Y');
```

_note: user 表中所有的以 `_priv` 结尾的字段都是表示相关的权限（privilege），`Y` 表示有， `N` 表示无此权限。_

## DDL

> 对表属性字段进行更改的操作

创建表：

> CREATE TABLE (table name) ((field name) (字段类型) (字段长度) [约束]));

```sql
create TABLE if not exists `user`(
    `id` int unsigned  not null auto_increment,`username` varchar(20) NOT null,
    `remark` varchar(30), primary key (`id`)
    )
    engine=innodb default charset=utf8mb4 COMMENT='用户表';
```

- 修改表名: `rename TABLE 原表名 TO 新表名;`
- 添加列：`ALTER TABLE 表名 ADD 字段名 类型(长度) [约束] COMMENT 备注;`
- 修改列：`ALTER TABLE 表名 MODIFY 字段名 类型(长度) [约束] COMMENT 备注;` 可指定列字符集，在其中加入: `CHARSET (utf8mb4)`;
- 修改列名：`ALTER TABLE 表名 CHANGE 旧字段名 新字段名 类型(长度) [约束] COMMENT 备注;`
- 删除列：`ALTER TABLE 表名 DROP 列名;`
- 修改表字符集：`ALTER TABLE 表名 CHARSET (charsetname);`
- 修改表备注：`ALTER TABLE 表名 COMMENT='your comments';` 表备注与列备注不同，使用了 `=`

### DML

> 对表中行数据进行更改

- 表插入数据：
  - `INSERT into 表名 （字段名...） values (value1), (value2), (value3);` 插入指定字段名数据
  - `INSERT into 表名 values (row1, row2, row3);` 此各必须插入所有字段
- 修改行数据 `UPDATE 表名 SET field1=value, field2=value2 [WHERE condition];`
- 删除行数据： `DELETE FROM 表名 [WHERE condition];`
- 删除表内所有数据： `TRUNCATE TABLE tablename;` 与 delete 不加 WHERE 条件区别在于 此命令将删除所有后新建一张同样的表所有其新行 id 从 0 开始，而 delete 将从之前删除的最大的 id 开始。

## DQL

> mysql 查询 DQL

- 查询：
  - 模糊查询：今天（Feb 18 2020才知道：除了 `%` 可以指定多个点位符外， `_` 还可以当作单个占位符使用。
  - 顺序： select >> from >> WHERE >> group by >> having >> order by
  
### 多表查询

> 多表联接查询

分类：

1. 交叉连接查询 `SELECT * from tableA,tableB;` （不推荐使用，其算法会将两张表每行数据进行组合得到一个行数之积的结果再 select）
2. 内连接查询 `INNER JOIN` ，其中 `INNER` 可省略
   1. 隐式内连接 `SELECT * FROM A,B [WHERE condition];`
   2. 显式内连接 `SELECT * FROM A JOIN B [ON condition];`
3. 外连接查询 keywork: `OUTER JOIN` , `OUTER` 可省略
   1. 左外连接: `LEFT OUTER JOIN` eg: `SELECT * FROM A LEFT OUTER JOIN B [WHERE condition];`
   2. 右外连接：`RIGHT OUTER JOIN` eg: `SELECT * FROM A RIGHT OUTER JOIN B [WHERE condition];`

## mysql engine

[参考博客](http://www.cnblogs.com/0201zcr/p/5296843.html)

- mysql数据库引擎类别有：
  - ISAM：
    - 读取速度很快，不占用大量内存资源；
    - 不足之处：不支持事务，不能容错（如果硬盘崩溃，则不能恢复了）；
  - MYISAM：
    - Mysql的默认引擎，是ISAM的扩展格式，提供了ISAM没有的索引和字段管理功能外，还使用表格锁定的机制，达到人优化多个并发的读写操作，代价是要常用`OPTIMIZE TABLE`来恢复被更新机制浪费的空间。
    - MYISAM强调用ISAM所有的快速读取操作，这也让MYISAM在WEB开发中受到青睐；
  - HEAP：
    - HEAP允许只驻留在内存里的临时表格。驻留在内存里让HEAP要比ISAM和MYISAM都快，但是它所管理的数据是不稳定的，而且如果在关机之前没有进行保存，那么所有的数据都会丢失。
    - 在数据行被删除的时候，HEAP也不会浪费大量的空间。
    - HEAP表格在你需要使用SELECT表达式来选择和操控数据的时候非常有用。要记住，在用完表格之后就删除表格；
  - INNODB和BEAKLEYDB（BDB)：
    - INNODB和BERKLEYDB（BDB）数据库引擎都是造就MYSQL灵活性的技术的直接产品，这项技术就是MYSQL++ API。
    - 在使用MYSQL的时候，你所面对的每一个挑战几乎都源于ISAM和MYISAM数据库引擎不支持事务处理也不支持外键。
    - 尽管要比ISAM和MYISAM引擎慢很多，但是INNODB和BDB包括了对事务处理和外来键的支持，这两点都是前两个引擎所没有的。

<!--more-->

### 关于INNODB引擎

**InnoDB:**
- Innodb引擎提供了对数据库ACID事务的支持，并且实现了SQL标准的四种隔离级别；
  - **ACID**
    - A  事务的原子性(Atomicity)：指一个事务要么全部执行,要么不执行.也就是说一个事务不可能只执行了一半就停止了.比如你从取款机取钱,这个事务可以分成两个步骤:1划卡,2出钱.不可能划了卡,而钱却没出来.这两步必须同时完成.要么就不完成.
    - C 事务的一致性(Consistency)：指事务的运行并不改变数据库中数据的一致性.例如,完整性约束了a+b=10,一个事务改变了a,那么b也应该随之改变.
    - I 独立性(Isolation）:事务的独立性也有称作隔离性,是指两个以上的事务不会出现交错执行的状态.因为这样可能会导致数据不一致.
    - D 持久性(Durability）:事务的持久性是指事务执行成功以后,该事务所对数据库所作的更改便是持久的保存在数据库之中，不会无缘无故的回滚.
  - 该引擎还提供了行级锁和外键约束，它的设计目标是**处理大容量数据库系统**，它本身其实就是基于MySQL后台的完整数据库系统，MySQL运行时Innodb会在内存中建立缓冲池，用于缓冲数据和索引。
- 与MYISAM相比：
  - MyIASM是MySQL**默认的引擎**，但是它没有提供对数据库事务的支持，也不支持行级锁和外键，因此当INSERT(插入)或UPDATE(更新)数据时即写操作需要锁定整个表，效率便会低一些。不过和Innodb不同，**MyIASM中存储了表的行数**，于是SELECT COUNT(*) FROM TABLE时只需要直接读取已经保存好的值而不需要进行全表扫描。
  - 如果表的**读操作远远多于写操作且不需要数据库事务的支持**，那么MyIASM也是很好的选择。
  - **大尺寸的数据集趋向于选择InnoDB引擎**，因为它支持事务处理和故障恢复。
  - 数据库的大小决定了故障恢复的时间长短，**InnoDB可以利用事务日志进行数据恢复**，这会比较快。
  - 主键查询在InnoDB引擎下也会相当快，不过需要注意的是如果主键太长也会导致性能问题，关于这个问题我会在下文中讲到。

### Index索引

> **索引（Index）是帮助MySQL高效获取数据的数据结构。**

- MyIASM和Innodb都使用了树这种数据结构做为索引。谈到树，就不得不谈到B-tree与B+tree；
- MyISAM引擎中用到的索引结构：
  - MyISAM引擎的索引结构为B+Tree，其中B+Tree的数据域存储的内容为实际数据的地址，也就是说它的**索引和实际的数据是分开的**，只不过是用索引指向了实际的数据，这种索引就是所谓的**非聚集索引**。
- Innodb引擎的索引结构：
  - 与MyISAM引擎的索引结构同样也是B+Tree，但是**Innodb的索引文件本身就是数据文件**，即**B+Tree的数据域存储的就是实际的数据**，这种索引就是**聚集索引**。
  - 这个索引的key就是数据表的主键，因此InnoDB表数据文件本身就是主索引。
  - 和MyISAM不同，InnoDB的辅助索引数据域存储的也是相应记录主键的值而不是地址，所以当以辅助索引查找时，会先根据辅助索引找到主键，再根据主键索引找到实际的数据。
  - 所以Innodb不建议使用过长的主键，否则会使辅助索引变得过大。建议使用自增的字段作为主键，这样B+Tree的每一个结点都会被顺序的填满，而不会频繁的分裂调整，会有效的提升插入数据的效率。
  - InnoDB的数据文件本身就是索引文件。从上文知道，MyISAM索引文件和数据文件是分离的，索引文件仅保存数据记录的地址。而在InnoDB中，表数据文件本身就是按B+Tree组织的一个索引结构，这棵树的叶节点data域保存了完整的数据记录。
  - 这个索引的key是数据表的主键，因此InnoDB表数据文件本身就是主索引。
- 这两种引擎都使用B+tree，不同之处在于：
  - MyISAM的的索引使用的是**非聚集索引**，而**InnoDB使用的是聚集索引**；
  - 所谓**非聚集索引就是索引与实际的数据是分开的**，索引只是指向了实际的数据；
  - **聚集索引指索引就是索引文件本身就是数据**，数据域存储的就是实际的数据；

