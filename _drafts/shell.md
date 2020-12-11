---
date: 2020-12-10 18:01:00
categories: programming
tags: [programming, linux, shell]
description: linux shell script
---

# Shell

> [Shell scripts in Unix](https://www.tutorialspoint.com/unix/unix-using-variables.htm)

## variable

> 变量

- 只读变量 `readonly variable`
- `unset {variable_name}` 重置一个变量，只读变量不可重置，重置后不能再访问
- 变量类型
    - Local variable.存在于当前shell实例中的变量，shell开启程序后，程序不能访问local variable .
    - Environment variable. 环境变量，shell进程中任何地方可以获取环境变量.
    - Shell variable.shell脚本中定义的变量，可以是 local variable ,也可是Environment variable .
