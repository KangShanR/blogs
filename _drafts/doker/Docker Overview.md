---
tag: [mysql, Docker]
categories: programming
description: The Overview of Docker
date: "2021-1-16 23:13:00"
---

# Overview of Docker

[reference](https://docs.docker.com/engine/)

Docker 引擎是一个构建并包含应用的开源容器技术。Docker 引擎作为一个 cs 应用主要有三种形式：

1. 长生命周期的守护进程(Docker daemon) dockerd
2. 与 dockerd 会话并指令的 api 接口
3. 命令行接口（CLI）客户端 docker

CLI 使用 Docker APIs 通过脚本或 CLI 命令控制并与 Docker daemon 交互。许多其他的 Docker 应用都使用 CLI、API 。守护进程 daemon 创建并管理 Docker 对象（object），比如：Images,containers,networks,volumes。
