# java compile

java 中的编译。

## 概述

- java 使用命令行编译源代码：`javac java_file`
- 使用命令行查询编译后的字节码：`javap -v class_file`。 `-v` verbose 可以查看详尽的字节码数据（如：常量池），不加此参数只能看到和源代码类似的数据。

## 使用命令行运行 java

[reference](https://javarevisited.blogspot.com/2015/04/error-could-not-find-or-load-main-class-helloworld-java.html)

- 命令：`java [-options] class_file [args]` to execute a class
    - `java [-options] -jar jarfile [args]` to execute a jar file 。一个 jar 包是如果运行起来的？其如果如何设置？
    - class_file 必须是此 java 文件的全名（带上包名，包之间使用 `.` 分隔）

where options insclude:

- `-cp` = `-classpath` 用以指定字节码路径。如果运行命令路径不在工程 class 根路径（idea 会将编译后的 class 文件放在 target/classes 文件下），那么此参数必须指定，否则会报出 “Error: Could not find or load main class”。
- `-D<name>=<value>` set a system property

- 终端打印环境变量：windows `echo %CLASSPATH%`; linux `echo $CLASSPATH`
    - 如果环境变量存在将打印出来，如果不存在将打印出命令。
