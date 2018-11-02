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
