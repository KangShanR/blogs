# linux

desktop virtual machine: root 123456

## tomcat

在 linux 上安装 Tomcat 并启动。(root 123456)

1. 安装 tomcat 后，进入 bin 中启动 `./startup.sh`;
2. 必须开启防火墙：
   1. `vim /etc/sysconfig/iptables` 打开系统 ip 配置文件
   2. 添加配置 `-A INPUT -m state --state NEW -m tcp -p tcp --dport 8080 -j ACCEPT`
      1. (直接调用服务写配置：`/sbin/iptables -I INPUT -p tcp --dport 6379 -j ACCEPT`)
   3. 重启防火墙 `service iptables restart`
      1. 加上路径的命令为： `/etc/rc.d/init.d/iptables save`
3. 通过外部网络可访问 tomcat 了。

## mysql

在 linux 安装 mysql 并运行。（本地虚拟机上：root 12345）

## redis

在 linux 上安装 redis 使用

1. 编译好 redis 后运行 redis  不能直接使用 `redis-server` ，需要使用 `./redis-server` 才能启动；
2. 修改 redis 服务后台启动：
   1. 将 redis.conf 文件 copy 到 bin 目录下，将其 `daemonize` 修改为 `yes`;
   2. 启动 redis-server 时加上参数：`./redis-server redis.conf`；
