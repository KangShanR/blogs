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
- eg: `[ $a -gt 100 -a $b -le 20 ]`

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

## Ｐrocess Control

- until 与 while 中的条件相反，满足条件在 while中循环，而不满足条件才在 until 中循环。
- `break n` 跳出循环，其中 n 代表正整数，默认不写为 1 只跳出当前层循环，而如果需要跳出第二层循环（从内往外数）就指定 n 为 2 。

## Substitutions

> 转义

常用转义符号：

- `\n` new line
- `\r` carriage return
- `\t` horizontal tab
- `\\` back slash
- `\a` alert
- `\b` backspace
- `\c` suppress trailing line
- `\f` form feed
- `\v` vertical tab

### Command Substitution

`command` 使用 backquote 将命令包围起来，命令执行结果将返回。eg: echo "today is `date`"

### Variable Substitution

> 变量转义

使用变量转义可以将对变量进行检查，并对其返回值或变量作修改。

- `${var:-word}` 如果变量 var 为 null  或未设置，将使用 word 转义为结果， var 变量不会被设置成 word.
- `${var}` 转义 var 的值
- `${var:=word}` 转义 var 并将 word 赋值给 var。
- `${var:?message}` 如果 var 未设置或为 null ，message 将打印到 standard error。用以检测变量 var 是否正确设置。
- `${var:+word}` 如果 var 已设置，work 将转义给 var ，但 var 不会改变。
