---
layout: "post"
title: "redis-transaction"
date: "2018-10-30 19:12"
---

# redis transaction

> redis 事务，redis 单个操作是原子性的，但 事务不是，事务中某个操作执行失败不会影响后面操作的执行，也不会让事务中已经执行的操作回滚。

## 命令

- `MULTI` 开启事务记录
- `DISCARD` 放弃开户的事务
- `EXEC` 执行事务
