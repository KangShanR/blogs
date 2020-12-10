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

## questions

### 授权失败次数过多而被拒绝连接

> 使用 ssh ＋ config 连接远程主机时出现 *Received disconnect from x.x.x.x port 22:2: Too many authentication failures*
> [referrence](https://www.tecmint.com/fix-ssh-too-many-authentication-failures-error/)
> 参考文章解释原因是因为多个 identityFile 被用于发起连接，而多个连接失败被远程主机拒绝。疑问在于我在本地的 config 文件中指定各个 host 所使用的 identityFile 为什么 ssh client 同样会用一个个地尝试。而非要加上 identitiesFileOnly=true 配置参数才能指定到特定的 identityFile 。
