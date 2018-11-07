---
layout: "post"
title: "Apollo-configuration-center"
date: "2018-10-31 17:15"
---

# Apollo configuration center

> Apollo 配置中心的使用 [quick-start wiki](https://github.com/ctripcorp/apollo/wiki/Quick-Start)

## java 项目中使用 Apollo 客户端列表信息

>  在使用中，客户端会将远程配置数据拉到本地来，防止断网类异常情况时不能获取到配置数据。本地路径：
- Mac/Linux: /opt/data/{appId}/config-cache
- Windows: C:\opt\data\{appId}\config-cache

**note:**
-  在本地配置并启动好 apollo 需要的数据库后，启动 demo.sh 脚本来启动 apollo service 与 config 服务。使用 命令行 `/path/demo.sh start` 后，控制台窗口很快消失，不知道是否已经启动好。再使用 `netstat -ano|findstr 8070|8090|8080` 检查，发现端口并未使用，说明并未启动好服务。这时 _解决办法_ 是：先 cd 到脚本所在目录再进行执行脚本命令。再检查，执行成功。

## 分布式部署 apollo 服务

[部署指南]( https://github.com/ctripcorp/apollo/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%83%A8%E7%BD%B2%E6%8C%87%E5%8D%97#2121-%E4%BB%8E%E5%88%AB%E7%9A%84%E7%8E%AF%E5%A2%83%E5%AF%BC%E5%85%A5apolloconfigdb%E7%9A%84%E9%A1%B9%E7%9B%AE%E6%95%B0%E6%8D%AE)

使用源码编译、打包:
1. 使用 mvn 命令打包在 target 文件夹中后，将压缩包拷到服务器上解压，再使用脚本命令依次执行相关命令而启动服务。

`./build.sh` 脚本会将会依次打包apollo-configservice, apollo-adminservice, apollo-portal

注：由于ApolloConfigDB在每个环境都有部署，所以对不同环境的 config-service 和 admin-service 需要使用不同的数据库连接信息打不同的包， **portal只需要打一次包即可**
