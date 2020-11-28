---
date: 2020-5-12 18:52:00
categories: programming
tags: [programming, linux, OS]
description: linux
---

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
2. `kill -9 PID` 杀掉进程；
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

- `cat [OPTION]... [FILE]...` concatenate 将文件输出级联到另一个文件或终端
    - `cat > {file}` 指定到同一个文件
- `lsb_release [OPTION]` 查看系统发布版本
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
- [`vim [file]` 或者 `vi [file]` 命令](https://vim.rtorr.com/) [cheat sheet](https://spin.atomicobject.com/2016/04/19/vim-commands-cheat-sheet/)
    - 进入到文件 vi 编辑状态后，有三种状态：
        - command mode 命令模式，此模式下按 `:` 进入底行模式， `i`/`a`/`o` 进入到插入模式
            - `i` 在光标前插入 `shift + i` 在光标所在行前插入
            - `a` 在光标后插入， `shift + a` 在光标所在行最后插入
            - `o` 在光标所在行下一行插入新行， `shift + o` 在光标所在行前一行插入新行
            - i - Insert at cursor (goes into insert mode)
            - a - Write after cursor (goes into insert mode)
            - A - Write at the end of line (goes into insert mode)
            - ESC - Terminate insert mode
            - u - Undo last change
            - U - Undo all changes to the entire line
            - o - Open a new line (goes into insert mode)
            - dd - Delete line
            - 3dd - Delete 3 lines.
            - D - Delete contents of line after the cursor
            - C - Delete contents of a line after the cursor and insert new text. Press ESC key to end insertion.
            - dw - Delete word
            - 4dw - Delete 4 words
            - cw - Change word
            - x - Delete character at the cursor
            - r - Replace character
            - R - Overwrite characters from cursor onward
            - s - Substitute one character under cursor continue to insert
            - S - Substitute entire line and begin to insert at the beginning of the line
            - `~` Change case of individual character, 前面加上数字将实现指定数量个字符的大小写切换
        - last line mode 底行模式， w write, q quit 退出到编辑模式, ! force
        - insert mode 插入模式，esc 退出到底行模式
    - copy & paste
        - `yy` 单行复制 yank current line
        - `y$` 复制光标到行尾 yank to end of the current line from cursor
        - `yw` 复制从光标处到 token 尾 yank from cursor to end of the current word
        - `yW` 从光标处复制到 word 尾
        - `nyy` n 行复制
        - `p`/`P` 在光标后/前粘贴
        - `gg` 到文本第一行
        - `shift + gg` 到文本最后一行
    - delete
        - `dd`删除光标所在行
        - `ndd` 删除 n 行
    - `u` undo
    - ctrl + `r` redo
    - Movement
        - h j k l 左下上右四个方向移动 cursor 在前加上数字可实现多个字符的跳动
        - b w B W 向 back forward 方向移动整个 token/word ，在其前数字指定移动数量
        - ^ $ 移动光标到行首尾
        - ctrl + u/d 向上下翻页up/down ，cursor 跟着一起翻页
        - *<line-number>*G 跳到指定行
        - H M L 跳到页面上中下 High Middle Low
        - `*` `#` 下一个与当前 cursor 所在 token 相同的 token ，前面加上数字指定跳跃数量
        - `n` `N` 重复上一次命令结果往上/下找
        - `''` 两次 `'` ，跳回上一次输入此命令的行首。相当于对特殊行进行一次记录，但只能记录一个历史，感觉没啥用。
- `grep [parameter] [regular expression] {file}` Global Regular expression print 过滤搜索特定字符。匹配到的结果是行，rgep 可以多个，file 也可多个，也可用 * 匹配当前路径所有文件
    - `-i` 或 `-ignore-case` 忽略大小写
    - `--color[=auto|=never|=always]` ：可以将找到的关键词部分加上颜色的显示
    - `-A2` after 显示匹配行+其后2行
    - `-B` before
    - `-C` 前后都有
    - c 统计行数
    - r recursive 递归查找包括子路径文件
    - n number of line 显示行号
    - E extend 扩展 rege
- 命令行中使用命令
    - `ctrl + e` 移动到行尾 end
    - `ctrl + a` 移动到行 首
    - `ctrl + w` 删除光标前一个 token
    - `esc + d` 删除光标后一个 token
    - `ESC + f` 向前移动一个 token
    - `esc + b` 向后移动一个 token

### 系统命令

- `ps [parameter]` process status 进程运行状态
    - `-e` 与 `-A` 相同显示所有进程
    - `-f` 显示UID, PPIP, C, STIME 栏位
- `kill [parameter] [process]`
    - `-9 pid` 强制终止

- sudo superuser do
    - sudo [command] 给普通用户命令加上超级用户权限
- 没有安装 systemmd(systemctl) 命令，使用 service {servicename} {start|stop|restart} 代替
- `uname` unix name 查看系统版本信息
- 

### firewall 防火墙

[reference](https://blog.csdn.net/u011846257/article/details/54707864)

添加 iptables 规则时需要将其添加成 REJECT 所有其他的端口之前，否则将无效。

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

#### windows operation

windows 操作系统中相关命令行。

- 刷新 dns 数据命令：

  ```sh
  ipconfig/flushdns
  ```

- 查看电脑网络端口使用情况，类似于在 linux 中使用 `ps` 命令
    - `netstat -ano` 命令行命令可以看到各个进程 pid 与 address 占用情况
    - `netstat -ano|findstr portNum` 查看指定端口占用情况，这儿查看的端口占用包括了本地地址的占用与外部地址的占用
    - `tasklist|findstr 'pid'` 查看指定 pid 的进程使用
    - `taskkill /pid pid /f` 杀死进程 pid 指 progress id 进程 id
        - 前两个步骤任选一个与最后个连用就可查找出端口被哪个进程给占用。
        - 也可以使用前两个步骤配合任务管理器查找，相对于命令行来说更直观。
        - 在 windows 系统中可用，不知在 linux 中是否可用

### rpm

> resources package manager 资源包管理器，其相应的包文件后缀为 `.rpm` 在 red hat/Fedora/SUSE 系列使用此包管理，而在 Dibian/Ubuntu 系列使用 `dpkg` 命令，其包文件后缀为 `.deb` 。

command lind : `rpm [parameter] [软件]

使用 rpm 管理系统软件包，可以定制软件的更新、安装与卸载。[reference](https://dev.mysql.com/doc/mysql-yum-repo-quick-guide/en/)

1. 下载软件包的 rpm 文件，再使用 rpm 命令安装仓库
2. 安装后，可以使用 yum-util 的 yum-config-manager 管理包，也可以直接进入 /etc/yum.repos.d/{software}.repo 文件查看管理软件包数据
3. 使用 yum intall {software} 安装软件，安装时会自动根据其 repo 仓库配置文件安装指定的软件版本

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

## windows10 & Ubuntu

> win10 + ubuntu 双系统安装与使用

### 安装问题

- 目前在台式机双显卡上遇到最大的问题是使用nvidia独显并不能设置分辨率为1920x1080，相反使用核显反而能。但是如果连接核显VGA接口又不能正常启动系统，只能先连接独显DVI－I接口让系统启动，启动后再将连接线接到VGA接口。
    - [显示器接口认识](https://www.zhihu.com/question/19571221)
    - [修改分辨率](https://blog.csdn.net/shixin_0125/article/details/72793300)，在使用DVI-I接口时并不能设置成功，只能设置到未连接的VGA 或 HDMI 接口上。
    - 使用 ｀xrandr｀ 命令查看系统正使用的接口与其分辨率
- [双系统下时间相差 8 小时](https://www.jianshu.com/p/5c1db6364141)
- ubuntu 默认使用其 snap 安装软件，其安装的 vscode 被阉割，不能在其中输入中文，需要自行安装完整版。[reference](https://blog.csdn.net/qq_35092399/article/details/105526908)
- ubuntu 中安装软件包使用 dpkg 命令，一般流程：
    - 使用 `wget` 下载 `＊.deb` 包，或直接在其官方网站上手动下载其提供的 linux 版本包；
    - [安装](https://help.ubuntu.com/kubuntu/desktopguide/zh_CN/manual-install.html)。可直接在右击 .deb 文件选择 Kubuntu Package Menu -> install。也可以使用 dpkg 命令安装。卸载时使用 apt-get remove 包名。
- [禁用启动时显卡加载参数](https://itectec.com/ubuntu/ubuntu-what-do-the-nomodeset-quiet-and-splash-kernel-parameters-mean/)
    - `nomodeset` 添加此参数指令会让 kernel 不加载显卡驱动使用 BIOS 模式也并非一定要 X server 被加载。
    - `quiet splash` 使用静默状态的启动，否则任何消息会打断 splash 桌面

### 使用

- [在 Ubuntu 中使用 U 盘](https://blog.csdn.net/a999wt/article/details/8227154)
    - 需要将 U 盘挂载（ `mount` 命令）到系统中，一般使用 /mnt 目录挂载
    - 挂载后在 /mnt 目录中就可以看到 U 盘内容了。此处有个坑是如果挂载前已经进入 /mnt 目录，挂载后是看不到 U 盘内容的，需要重新进入目录才能看到。
    - 操作完 U 盘内容后使得命令 `umount` 命令卸载 U 盘。
        - 卸载 U 盘可能会出现 device is busy 的提示，等一会儿就自动卸载。
