---
date: 2020-12-10 18:01:00
categories: programming
tags: [programming, linux, shell]
description: linux shell script
---

# Shell

> Shell 是一个可以执行命令／程序／shell 脚本的环境，提供了访问 Unix 系统的接口。其根据输入执行程序，执行完成后将结果展示出来。 [Shell](https://www.tutorialspoint.com/unix/unix-what-is-shell.htm)

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

`command` 使用 back quote 将命令包围起来，命令执行结果将返回。eg: echo "today is `date`"

### Variable Substitution

> 变量转义

使用变量转义可以将对变量进行检查，并对其返回值或变量作修改。

- `${var:-word}` 如果变量 var 为 null  或未设置，将使用 word 转义为结果， var 变量不会被设置成 word.
- `${var}` 转义 var 的值
- `${var:=word}` 转义 var 并将 word 赋值给 var。
- `${var:?message}` 如果 var 未设置或为 null ，message 将打印到 standard error。用以检测变量 var 是否正确设置。
- `${var:+word}` 如果 var 已设置，work 将转义给 var ，但 var 不会改变。

## Quoting Mechanisms

> [引用机制](https://www.tutorialspoint.com/unix/unix-quoting-mechanisms.htm)

### Meta Characters

> Unix 元字符在 shell 中有特殊含义，所以在命令中如果要使用其为普通字串，需要在其前加上转义符号 backslash `\`。 Unix 中的元字符包括：

```shell
* ? [ ] ' " \ $ ; & ( ) | ^ < > new-line space tab
```

`?` 代表任何一个字符，而 `*` 代表任意多个字符。

### quoting

> 引用方式有四种

1. 单引号引用 `'`，特殊符号会将全部的元字符给转义为字面量。当需要输出单引号时，此时可以使用 backslash 将其转义输出。
2. 双引号引用 `"`，大部分特殊符号被双引号引用有将丢失其特殊意义，但有例外：$ ` \$ \' \" \\
3. backslash `\`, 所有特殊变量在 backslash 后都将丢失其特殊意义
4. back quote `, 被 back quote 包围的任何字符都将会被当作命令执行。

## IO Redirection

> IO 重定向

重定向的命令有：

1. `pgm > file` 重定向到输出文件
2. `pgm >> file` 将输出追加到指定文件
3. `pgm < file` 程序从文件读取输入
4. `n > file` 将 n fd 的流输出重定向到文件 file
5. `n >> file` 将 fd n 的流输出重定向追加到文件 file
6. `n >& m` 合并 fd n 流与 fd m 流输出
7. `n <& m` 合并输入流 fd n 与 fd m
8. `<< tag`  Standard input comes from here through next tag at the start of line
9. `|` 管道，将前一个程序／应用的输出发送到下一个

NOTE: *file descriptor（fd） 在 Unix 中使用非负整数表示，其中 0 表示标准输入 STDIN，1 表示标准输出 STDOUT，2 表示错误输出 STDERR*
在 Unix 系统中每个非守护进程都有以上三个 IO 流，进程通过 kernel 访问文件 file table / inode table。

## Functions

> Unix shell function

- 定义 function 其语法是：在方法名后跟上 `function_name () {}`，传递参数直接在命令行中添加，在方法中调用参数使用 `$n`
- 返回数据使用关键字 `return`
- `exit` 会终结整个 shell 执行，而不是 function 。
- `$?` 获取上一次命令返回值 `ret=$?`，此值只是一个整数代表结果，不能返回字符及其他
- shell 文件调用需要使用 `. shell_file`
- shell 文件后缀可以不用写，一样的效果
- shell 文件头指定 shell  执行 bash 类型也可不写，让系统默认的 shell 执行即可
- `echo $PATH` 可以看到系统全局变量，其中一般包括了 ~/bin ，所以要以在此文件路径中添加自己想要的全局 shell ，而实现任何地方不添加绝对路径前缀调用此 shell 。
- `$HOME` 调用当前用户目录，在 shell 中使用 ～ 不会生效。

## Alias

> 给 bash shell 命令添加别名.[reference](https://blog.csdn.net/doiido/article/details/43762791)

- 使用命令 `alias command='command arguments'` 给命令添加别名，从而缩小写常用参数的工作。eg: `alias grep='grep -iE --color=auto'`，可以实现使用 grep 命令自动添加两个参数
- 如果要永久实现别名生效可以将命令添加到 ~/.bashrc 文件中并执行此文件
- 解除别名设置使用命令 `unalias name`
