---
tag: [ssh, port forwarding]
date: 2020-11-30 15:09:00
description: open ssh
---

# SSH

> secure shell, open ssh

[终于找到了](https://www.ssh.com/ssh/config)

[port forwarding](https://fzp.github.io/2019/11/15/ssh-proxy-port-forwarding.html)

调整 windows GitBash UI。编辑用户目录下 `~/.minttyrc` 文件

## 使用 gitbash 登录主机

```host = da
hostname = {ip}
user = {service user:root}
identityFile = {生成的私钥文件}
```

1. 使用 ssh-keygen 命令生成授权码
2. ssh-copy-id -i {public key file} user@host 上传公钥
3. ssh -i {private key file} user@host 测试登录

配合 config 文件直接使用命令 `ssh {host}` 登录
