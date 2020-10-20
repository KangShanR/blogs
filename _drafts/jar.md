---
date: 2020-10-20 11:02:00
categories: java
tags: [java, jar, package]
description: java jar
---

# Java jar

java archive

- 使用 jdk 自带工具 javac 编译 java 文件成 class 字节码，再用打包命令 `java jar cfmv jar-name manifest-file class-files`

## installed extensions

添加外部的 jar 用作扩展。

- 可在命令行中添加 classpath 的参数，指定 classpath 引用外部 jar 包，也可以在 manifest 文件中指定多个 classpath 。
- 可以直接在 jre 中添加扩展。path：jre/lib/ext
- 若有多个 jre，可以在用户目录中指定让多个 jre 使用同一个 jar 包：usr/java/packages/lib/ext
