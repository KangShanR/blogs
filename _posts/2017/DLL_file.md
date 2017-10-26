---
title: dll文件的认识
date: 2016-06-30 15:02:43
categories: programming
tags: programming
description: 关于dll文件的认识，以及与exe文件的区别
---
#### DLL动态链接库：Dymanic Link Library ####


- DLL是将程序相关的功能实现封装在一起放在指定目录（后缀名为.dll），程序运行时，再来动态链接起执行。这样就能达到多个程序共享同一个库文件，而减少程序安装大小；
- DLL文件与exe文件都是编译语言生成，但dll没有程序入口，所以dll不能独自执行；