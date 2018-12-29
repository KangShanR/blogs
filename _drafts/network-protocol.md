---
layout: "post"
title: "network protocol"
date: "2018-12-29 15:04"
---

# network protocol

> 趣谈网络协议


## 一个简单的网络请求之路

1. 请求数据
2. **应用层** 加上 HTTP 头（应用层包括：HTTP HTTPS DNS）
3. **传输层** 加上 TCP 头，其中包括 请求客户端与目标服务器的 端口号信息
4. **网络层** 加上 IP 头，内容为 双方的 IP 地址
5. **MAC 层** 加上 MAC 头，内容为双方的 mac 地址
