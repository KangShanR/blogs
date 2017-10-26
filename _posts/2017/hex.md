---
title: 十六进制的认识
date: 2017-04-23 15:02:43
categories: programming
tags: programming
description: 计算机语言中十六进制的使用
---
# 十六进制

- 在数字前直接加0x就可以使用，所用字符：
	- 0，1，2，3，4，5，6，7，8，9，a,b,c,d,e,f
- 两个byte的字符共占16位，因此我们看到unicode 码时，表示字符常用4位的十六进制数字来表现：
	- eg:0xfa14
- 可直接用十六进制来表现整形数据，但我们在编程中习惯于用十进制，要使用十六进制时直接使用就是；
	- 测试代码（java)：
			public static void main(String[] args) {
				System.out.println(0xeeda);
				System.out.println(0x0003);
				System.out.println(0xa);
				System.out.println(0xea);
			}
	- 测试结果：
			978346
			3
			10
			234
- 中文的unicode码的范围4e00-9fa5