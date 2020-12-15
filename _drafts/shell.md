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

## Basic operators

[reference](https://www.tutorialspoint.com/unix/unix-basic-operators.htm)

### Arithmetic operators

- 使用基础运算符时引用变量需要同样使用 `$` 符号，但赋值时直接使用变量名即可。`expr a = $b`
- 运算符与表达式之间需要空格分开。`expr $a != $b`
- `*` 乘号使用需要添加转义 `\*`
- 条件表达式使用需要被方括号(括号与表达式之间需要空格分隔)所包围：　`[ $a == $b ]`

### Relational Operators

- 同样需要方括号与空格包围变量 `[ $a -eq $b ]`
- `-eq` equals
- `-ne` not equals
- `-gt` greater than
- `-lt` less than
- `-ge` greater or equals
- `-le` less or equals
- 关系运算符支付数值类，如果是string类必须是代表数值的string，eg:`"100"`

### Boolean Operators

- `!` 取反
- `-o` OR
- `-a` AND
- eg: [ $a -gt 100 -a $b -le 20 ]

### String Operators

- `=`
- `!=`
- `-z` zero 检测字串其长度是否为 ０  `[ -z $a ]`
- `-n` not-zero 字串长度不为 0 `[ -n $a ]`
- str 检测字串是否为 empty `[ $a ]`

### File Test Operators

检测关联到文件的变量属性。假如一个变量 `file` 关联到一个 test 文件，大小100bytes，有 read/write/execute 权限。其相关命令如下：

- `-b file` check if file is a block special file. `[ -b $file ]` false
- `-c file` check if file is a character special file. `[ -c $file ]` false
- `-d file` check if file is a directory. `[ -d $file ]` false
- `-f file` check if file is an ordinal file as opposed to a directory or special file. `[ -f $file ]` is true
- `-e file` check if file exists. `[ -e $file ]` is true
- `-r`/`-w`/`-x` check if file is readable/writable/executable.

## Decision Making

[reference](https://www.tutorialspoint.com/unix/unix-decision-making.htm)

shell 中两种类似 switch case 的分支语句：

1. if...elif...else...fi
2. case...esac
