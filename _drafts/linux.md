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

问题：March 12 2020 在虚拟机上 linux 设置自动启动 mysql 后，在使用 `service mysql stop` 命令出现错误：“ERROR! MySQL server PID file could not be found”。最后使用 stackoverflow 上方法解决：[stackoverflow](https://stackoverflow.com/questions/41616251/error-mysql-server-pid-file-could-not-be-found-in-osx-sierra)

1. `ps -ef | grep mysql --color` 查找出正在运行的 mysql 进程；
2. `kill -9 PID` 杀死掉这些进程；
3. 再使用 `service mysql start` 启动 mysql ，后面再使用 `service mysql stop` 正常停止 mysql。

## redis

在 linux 上安装 redis 使用

1. 编译好 redis 后运行 redis  不能直接使用 `redis-server` ，需要使用 `./redis-server` 才能启动；
2. 修改 redis 服务后台启动：
   1. 将 redis.conf 文件 copy 到 bin 目录下，将其 `daemonize` 修改为 `yes`;
   2. 启动 redis-server 时加上参数：`./redis-server redis.conf`；

## 常用命令

- scp secure copy 可以从远程服务器拷贝，同时加密

### 磁盘管理命令

- `ls [参数] [文件或目录]` list 查看目录下所有文件 
  - `-a` 或 `--all` 查看全部，包括隐藏的
  - `-l` 简化版: `ll` 详细格式列表
- `cd [目录]` change directory
  - `~` 当前用户目录
  - `/` 根目录
  - `.` 当前目录
  - `..` 上级目录
  - `-` 上次所在目录
- `pwd` print working directory 打印当前所在目录
- `mkdir` make directory
- `rmdir` remove directory 移除空目录

### 文件管理

- `cat {文件}` catenate ? 显示文件所有内容
- `more {file}`  分页显示文件内容
  - enter 向下显示一行（默认1行，可定义）
  - 空格/ctrl+F 显示下一页
  - `B`  back 返回上一屏
  - `q` 退出 more
- `less [参数] {file}` 
  - `-m` 显示 more 命令一样的 百分比
  - `-N` 显示行号
  - 后续命令
    - 空格/page down 向下翻页
    - page up/b(back) 向上翻页
    - 上下方向键/y与enter键 向上下一行
    - `?` `/` 向上/下搜索
      - `n`/`N` 跳到下一个/上一个查找的结果
    - `q` 退出
    - 左右方向键 左右滚屏
- `tail [parameter] [file]`
  - `-n` 显示行数
  - `-f` 动态跟踪文件，查看动态日志
  - ctrl + c 退出
- `find [path] [parameter]` 在目录中查找文件
  - `-name`  指定字符串作为查找的样本

### 文档编辑

- `|` 管道命令，一般与 grep 联用，在上一个结果中进行操作下一个命令
- `vim [file]` 或者 `vi [file]` 命令
  - 进入到文件 vi 编辑状态后，有三种状态：
    - commond mode 命令模式，此模式下按 `:` 进入底行模式， `i`/`a`/`o` 进入到插入模式
      - `i` 在光标前插入 `shift + i` 在光标所在行前插入
      - `a` 在光标后插入， `shift + a` 在光标所在行最后插入
      - `o` 在光标所在行下一行插入新行， `shift + o` 在光标所在行前一行插入新行
    - last line mode 底行模式， w write, q quit 退出到编辑模式, ! force
    - insert mode 插入模式，esc 退出到底行模式
  - copy & paste 
    - `yy` 单行复制
    - `nyy` n 行复制
    - `p` 在光标处粘贴
    - `gg` 到文本第一行
    - `shift + gg` 到文本最后一行
  - delete
    - `dd`删除光标所在行
    - `ndd` 删除 n 行
- `grep [parameter] [regular expression] {file}` Global Regular expression print 过滤搜索特定字符。
  - `-i` 或 `-ignore-case` 忽略大小写
  - `--color=auto` ：可以将找到的关键词部分加上颜色的显示喔！

### 系统命令

- `ps [parameter]` process status 进程运行状态
  - `-e` 与 `-A` 相同显示所有进程
  - `-f` 显示UID, PPIP, C, STIME 栏位
- `kill [parameter] [process]`
  - `-9 pid` 强制终止

### 备份压缩

- `tar`
  - `-c` create 创建压缩
  - `-x` extract 解压
  - `-z` 使用 gzip 压缩
  - `-v` verbose 显示压缩的文件
  - `-f` file 是否使用档名

### 关机重启

- `reboot` 重启 CentOS
- `halt` 关机 CentOS

### 文件权限

`chmod [parameter] <权限范围> <符号><权限代码>` change mode 更改权限

- `-R` 或 `-recursive` 递归将文件夹中所有文件都更改
- 权限范围
  - `u` user
  - `g` group
  - `o` other
  - `a` all
- 权限符号
  - `+` 添加
  - `-` 取消
- 权限代号
  - `r` read 4
  - `w` write 2
  - `x` execute 1
  - `-` none 0

### 网络配置

查看 网络配置 文件位置: `/etc/sysconfig/networking-scripts/ifcfg-eth0`

### rpm

> resources package manager 资源包管理器

command lind : `rpm [parameter] [软件]

- `-v` verbose 显示指令执行过程
- `-h` 或 `--hash` 套件安装时显示列出指令
- `-q` question 询问模式，遇到问题时指令会先询问用户
- `-a` all 显示所有的套件
- `-i` 或 `--install`<套件档> 安装指定的套件档
- `-U` 或 `--upgrade <套件档>` 更新指定的套件档
- `-e <套件档>` erase 删除指定的套件档
- `--nodeps` no dependence 不验证套件间的关联性
- 常用命令：
  - `rpm -qa` 查看安装的软件
  - `rpm -e --nodeps <软件名>` 删除软件
  - `rpm -iph <package>` 安装
  - `rpm -Uph <软件>` 更新软件

## 服务命令

- `service <service name> restart` 重启服务
- `service <service name> start` 启动服务
- `service <service name> stop` 停止服务
