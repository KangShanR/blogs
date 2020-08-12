---
date: 2020-08-12 17:32:00
tags: [framework,java,file]
categories: programming
description: java class File
---

# File

java `File` 类

文件系统中前缀概念：

- Unix 系统文件系统中绝对路径前缀是 `/`，相对路径没有前缀
- Windows 平台路径前缀包括了：驱动符号后跟上 `:` (组成驱动指示符)，如果是绝对路径，将追加上 `\\`
    - UNC（universal Naming Conversion）路径名是 `\\\\`, hostname 与 share name 放在第一二位。相对路径无前缀、驱动指示符
