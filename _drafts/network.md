---
layout: "post"
title: "network"
date: "2018-10-17 14:37"
---

# 网络配置

刷新 dns 数据命令：
```
ipconfig/flushdns
```

查看电脑网络端口使用情况：
- `netstat -ano` 命令行命令可以看到各个进程 pid 与 address 占用情况
- `netstat -ano|findstr portNum` 查看指定端口占用情况，这儿查看的端口占用包括了本地地址的占用与外部地址的占用
- `tasklist|findstr 'pid'` 查看指定 pid 的进程使用

 _note:_
 - 前两个步骤任选一个与最后个连用就可查找出端口被哪个进程给占用。
- 也可以使用前两个步骤配合任务管理器查找，相对于命令行来说更直观。
