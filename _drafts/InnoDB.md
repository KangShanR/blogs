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

- 在并发写同一行数据时，如果 where 条件字段没有加索引，innodb 会对所有行加行锁，再对条件进行筛选，不符合条件的行再释放锁。一锁一放损耗极大，所以建议适当添加索引。
