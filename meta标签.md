---
title: meta标签.md
date: 2015-03-23 14:41:48
tags: html
categories: 
description: 
---

###  标签简介：


- 位于文档的头部<head>标签之中，不包含任何内容；
- 所有的信息都由属性来存储，包含了文档的名称/值对；
- 其作用：搜索引擎优化（SEO)，定义的页面使用语言，自动刷新并指向新的页面，实现页面转换时的动态效果，控制页面缓冲，网页定级评价，控制网页显示的窗口；
- meta属性（就两个）：
	- name属性：
		- 用于描述网页与之对应的属性值为content,其中的内容便于搜索机器人查找和分类信息用；
		- 其语法格式：
					<meta name="参数“ content="具体的参数值">
		- name属性有以下几种参数：
			- Keywords，用来指定关键字
					<meta name="keywords" content="meta总结、html/meta、meta属性、meta跳转”>
			- desription，网站内容描述
			- robots,机器人向导
			- author，标网页的作者
			- generator，代表说明网站采用的什么软件制作
			- COPYRIGHT,版权信息
			- revisit-after,代表网站征文，7days代表7天
		- http-equiv,相当于文件头作用，可以向浏览器传回一些有用的信息，以帮助正确和精确地显示网页内容，与之对应的属性值为content,其中的内容就是各个参数的变量值；
					<meta http-equiv="参数" content="参数变量值”>
			- Expires，期限，设定的到期时间，一旦网页过期，呢额胆固醇服务器上重新传输。（必须使用GMT的时间格式化）
			- Pragma（cache模式）
				- 禁止浏览器从本地计算机的缓存中访问页面内容
						<meta http-equiv="Refresh" content="no-cache">
					- 这样设计，说教将无法脱机浏览
			- Refresh(刷新），自动刷新并指向新页面
					<meta http-equiv="Refresh"content="2;URL=http://www.haorooms.com"> //(注意后面的引号，分别在秒数的前面和网址的后面)
			- Sett-Cookie(cookie设定），如果网页过期，存盘的cookie将被删除
					<meta http-equiv="Set-Cookie"content="cookie value=xxx;expires=Friday,12-Jan-200118:18:18GMT；path=/">
				- 必须使用GMT的时间格式
			- Window-target(显示窗口的设定），强制页面在当前窗口以独立页面显示
					<meta http-equiv="Window-target"content="_top"> 
				- 用来毕业后公开了我在框架里调用自己的页面
			- content-Type，设定显示字符集
					<meta http-equiv="content-Type"content="text/html;charset=gb2312">
				- meta标签的charset的信息参数如GB2312时，代表说明网站是采用的编码是简体中文；
				- BIG5:繁体中文；
				- iso-2022:日文；
				- ks_c_5601:韩文；
				- ISO-8859-1:英文；
				- UTF-8:世界通用的语言编码；
			- content-Language,设定显示语言
					<meta http-equiv="Content-Language" content="zh-cn"/>
			- Cache-Control，指定评语和响应遵循的缓存机制
			- imagetollbar,指定是否显示图片工具栏，当为false娃给显示，当为true代表显示；
			- Content-Script-Type,W3C网页规范，指明页面中脚本中的类型；