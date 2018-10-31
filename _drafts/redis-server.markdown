---
layout: "post"
title: "redis server"
date: "2018-10-31 11:07"
---

# redis server

> redis 服务器相关

使用命令行查看相关信息：
- `INFO [section]` 查看服务器所有信息， 能查看到的信息（section 参数）有：
    - 服务器的连接信息 server
    - 客户端列表信息 clients
    - 键概要 keyspace
    - Clients 客户端概要信息
    - Memory 内存使用
    - Persistence 持久化
    - Stats 数据统计
    - Replication 应答
    - CPU central process unit
    - Cluster 集群
- `CLIENT LIST` 查看连接在服务器上的所有客户端列表。其中各个字段表示了不同的信息，看得懂的有：
    - db 该客户端当前连接的 db
    - cmd 最后一次执行的命令
    - addr 客户端地址端口号
- `DBSIZE` 查看当前 db key 数量
- `SAVE` 保存当前内存数据到硬盘
- `ROLE` 查看当前实例所属角色

## 性能测试

> 使用 redis-benchmark.exe 进行测试单秒各个命令执行请求次数测试 redis 服务器性能测试。

- 在运行 redis-benchmark 时可以指定各种参数让指定测试数据。[redis server 性能测试](http://www.runoob.com/redis/redis-benchmarks.html)


## 数据的备份与恢复

> redis 在使用过程大部分数据在内存中运行，关闭时会自动保存在硬盘上，下次使用再读出来。

备份:
- 使用命令 `SAVE` 可以将数据同步到 硬盘文件 dump.rdb 中；
- 也可以使用命令 `BGSAVE` 进行后台保存；

恢复：
- 将 dump.rdb 重新拷到 redis 安装 目录，即恢复了数据。
