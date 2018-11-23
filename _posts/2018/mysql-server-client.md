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
- 启动服务 `NET START MySQL`（到此，MySQL 的 server 算是启动完成，可以使用 client 命令进行登录访问了）
- mysql 服务： `net stop MySQL`
- 删除 mysql 服务（需要先停止服务）： `mysqld -remove`
    - 以上添加 mysql 到电脑服务系统步骤可以不用做一样可以启动 mysql 服务：
        - 需要的操作是：控制台进入到 mysql 安装目录 bin 中
            - 直接启动 mysqld.exe `mysqld --console` --console 打开日志记录
            - 关闭服务：`mysqladmin -uroot shutdown`
- 查看当前 mysql server 连接情况命令： `show processlist` 将会看到当前 mysql server 连接的各个 client 的 ip/port/status/user/db 等



## mysql client operate

> 在 mysql 控制台操作数据库

_note: mysql server 命令行都必须使用 `;`  或 `\g` 结尾，否则不会执行。_

- 登录：`mysql -h hostadrress -u username [-p]` -p 决定是否带上密码
登录全更改初始密码：
- `SET PASSWORD FOR 'root'@'localhost' = PASSWORD('password');`
- `UPDATE mysql.user SET authentication_string = PASSWORD('password'), password_expired = 'N' WHERE User = 'root' AND Host = 'localhost';` （本机改为 `111`）
    - 从此行更改用户密码的命令可以得出：
        - mysql 库 user 表是存放用户的表
        - 其中的 aythentication_string 字段用于存放密码
        - 密码格式：PASSWORD('password')
        - 需要 **注意** 的是： mysql 5.7 后不再支持 PASSWARD() 函数，取而代之的是 MD%()
- 退出控制台：`exit|quit`


常用命令：
- 查找库内所有的表名： `select table_name from information_schema.tables where table_schema = 'dabataseName' AND table_type='base table';`
- 选择库：`USE database`
- 查看库内所有表名：`SHOE TABLES`
- 添加库： `CREATE DATABASE db_name;`
- 删除库： `DROP database;`
- 查看表所有 column : `SHOW COLUMNS FROM table`
- 查看表所有 index : `SHOW INDEX FROM table`
- 查看表状态信息： `SHOW TABLE STATUS [FROM db_name] [LIKE 'table'] [\G]`

### mysql 服务添加新用户

> 当 mysql 服务初始化完成并启动后，登录 root 账户后可以在 client 使用命令添加用户，只需要在 mysql 库 user 表中插入数据即可。

执行命令：
```
INSERT INTO user
          (host, user, password,
           select_priv, insert_priv, update_priv)
           VALUES ('localhost', 'guest',
           PASSWORD('guest123'), 'Y', 'Y', 'Y');
   ```

   _note: user 表中所有的以 `_priv` 结尾的字段都是表示相关的权限（privilege），`Y` 表示有， `N` 表示无此权限。_



### 创建表

```
create table if not exists `user`(
    `id` int unsigned  not null auto_increment,`username` varchar(20) NOT null,
    `remark` varchar(30), primary key (`id`)
    )
    engine=innodb default charset=utf8mb4;
```
