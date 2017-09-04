---
title: ASCII编码
date: 2017-04-23 15:02:43
categories: programming
tags: programming
description: 计算机语言中ASCII码的使用
---
### ASCII码 ###


> （American Standard Code for Information Interchange，美国信息交换标准代码）是基于拉丁字母的一套电脑编码系统，主要用于显示现代英语和其他西欧语言。它是现今最通用的单字节编码系统，并等同于国际标准ISO/IEC 646。


- 关键字符ASCII值：
	- 0:46 0~9的ASCII码值就是48~57，习惯于在代码函数中用左闭右开的区间来表示：[48,58)
	- eg:（Java language）
			//用于判断字符属于哪个类型的方法
			static String typeChar(char _char){
				    int valueChar = Integer.valueOf(_char);
				    if(valueChar >= 48 && valueChar < 58)
				    	return "number";
				    else if(valueChar >= 65 && valueChar < 91)
				    	return "uppercase";
				    else if(valueChar >= 97 && valueChar <123)
			    		return "lowercase";
				    else
			    		return "symbol";
			    }
	- 测试：
			public static void main(String[] args) {
				System.out.println(typeChar(','));
			}
	- 测试结果：
			symbol


- 常用到的字符ASCII值：

|字符|ASCII值|
|:----:|:------|
|A|65|
|A-Z|65-90|
|a|97|
|a-z|97-122|