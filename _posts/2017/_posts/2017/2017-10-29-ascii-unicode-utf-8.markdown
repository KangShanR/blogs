---
layout: "post"
title: "ASCII&Unicode&UTF-8"
date: "2017-10-29 00:51"
categories: programming
tags: ASCII,Unicode,UTF-8
---

# ASCII 码Unicode 码还有 UTF-8 之间的恩怨

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [ASCII 码Unicode 码还有 UTF-8 之间的恩怨](#ascii-码unicode-码还有-utf-8-之间的恩怨)
	- [简介](#简介)
	- [参考](#参考)

<!-- /TOC -->

<!--more-->

## 简介

这三者之间的定义：
- ASCII码：美国信息交换标准代码（American Standard Code Information Interchange），规定了所有的英文字母与常用的字符，同时包括32个不显示的控制字符（共128个）；一个字节（1byte）占用 8bit，可表达 2的8次方（256）个字符，表示时最前一位统一为 `0`，也就只占 7bit 。
- Unicode 码：多国语言对应的计算机 bit 信号，多国的语言字符与计算机中对应的 bit 码，因此数量庞大；
- UTF-8 编码方式应运而生，它是 Unicode 的实现方式之一。它按照一定规则将 Unicode 码转换成 UTF-8 ；
  - 利用计算机上的 notepad 就可以实现将字符进行各种编码格式之间的转换，选择不同的编码格式（ANSI、Unicode、Unicode big endian、UTF-8）即可，保存后可以用其它的文本编辑工具（如：UltraEdit、EditPlus）打开可以查看其十六进制编码，就可以看到不同的编码保存下来的文件的 bit 码值；

## 参考

访问阅读了下面相关的网站内容：
- [阮一峰相关博客][e7b776ef]
- [汉字Unicode码对照表][dd586ae8]

  [e7b776ef]: http://www.ruanyifeng.com/blog/2007/10/ascii_unicode_and_utf-8.html "关于 ASCII码、Unicde 码、UTF-8之间的关系"
  [dd586ae8]: http://www.chi2ko.com/tool/CJK.htm "查找汉字等东亚文字的Unicode表"
