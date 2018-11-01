---
layout: "post"
title: "mysql-server & client"
date: "2018-11-01 10:13"
---

# mysql server & client

> mysql 部署 server 及 client 请求

## mysql server

> mysql server 的部署及请求[参考](http://www.cnblogs.com/QingXiaxu/p/7987302.html)

localhost 的 testing 尝试：
- 下载 mysql 安装包，解压后将 bin 添加到环境变量中。
-  在其根目录添加 data 文件夹及 my.ini 配置文件
- 初始化 mysql 服务 `mysqld --initialize --user=mysql --console`
    - 初始化后，data 文件夹中会生成相应的各个文件
    - 生成临时账户及密码： root@localhost: W>pg*LYv>6up
- 添加 MySQL 服务到电脑系统服务中： `sc create MySQL binPath= "C:\Program Files\MySQL\MySQL Server 5.7\bin\mysqld.exe"`
    - 添加错了时，可以使用命令删除服务 `sc delete serviceName`
- 启动服务 `NET START MySQL`
- 更改初始密码 `MYSQLADMIN -U username - password` 这儿的密码使用初始化时生成的密码，但这个密码是临时且有几分钟的有效时间，如果 过期这儿是不能更改的，需要在 client 登录时才能更改
- 到此，MySQL 的 server 算是启动完成，可以使用 client 命令进行登录访问了
