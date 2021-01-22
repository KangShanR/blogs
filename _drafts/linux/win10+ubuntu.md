---
categories: programming
date: "2017-11-09 10:52"
tag: miniprogram
---

# windows10 & Ubuntu

> win10 + ubuntu 双系统安装与使用

## 安装问题

- 目前在台式机双显卡上遇到最大的问题是使用nvidia独显并不能设置分辨率为1920x1080，相反使用核显反而能。但是如果连接核显VGA接口又不能正常启动系统，只能先连接独显DVI－I接口让系统启动，启动后再将连接线接到VGA接口。
    - [显示器接口认识](https://www.zhihu.com/question/19571221)
    - [修改分辨率](https://blog.csdn.net/shixin_0125/article/details/72793300)，在使用DVI-I接口时并不能设置成功，只能设置到未连接的VGA 或 HDMI 接口上。
    - 使用 ｀xrandr｀ 命令查看系统正使用的接口与其分辨率
- [双系统下时间相差 8 小时](https://www.jianshu.com/p/5c1db6364141)
- ubuntu 默认使用其 snap 安装软件，其安装的 vscode 被阉割，不能在其中输入中文，需要自行安装完整版。[reference](https://blog.csdn.net/qq_35092399/article/details/105526908)
- ubuntu 中安装软件包使用 dpkg 命令，一般流程：
    - 使用 `wget` 下载 `＊.deb` 包，或直接在其官方网站上手动下载其提供的 linux 版本包；
    - [安装](https://help.ubuntu.com/kubuntu/desktopguide/zh_CN/manual-install.html)。可直接在右击 .deb 文件选择 Kubuntu Package Menu -> install。也可以使用 dpkg 命令安装。卸载时使用 apt-get remove 包名。

## cheat tips

- [添加交换区swap](https://blog.csdn.net/rlhua/article/details/24669037)
