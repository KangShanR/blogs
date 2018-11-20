---
layout: "post"
title: "worklist-2017-11-6"
date: "2017-11-06 13:12"
---

# 新一周工作列表 2017-11-6

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:1 -->

1. [新一周工作列表 2017-11-6](#新一周工作列表-2017-11-6)
	1. [2017-11-6](#2017-11-6)
			1. [明天要解决的问题](#明天要解决的问题)
		1. [继续上一周小程序的开发问题](#继续上一周小程序的开发问题)
		2. [待办事项](#待办事项)
	2. [2017-11-07](#2017-11-07)
		1. [](#)
	3. [整理点](#整理点)
		1. [腾讯云域名的使用](#腾讯云域名的使用)
			1. [通过 CNAME 将 github 博客域名映射到自己的域名里](#通过-cname-将-github-博客域名映射到自己的域名里)
			2. [DNS](#dns)
			3. [关于二级域名的部署](#关于二级域名的部署)
		2. [怎么查看所访问的网站的 ip 地址？](#怎么查看所访问的网站的-ip-地址)
			1. [目前已完成的 Demo 案例](#目前已完成的-demo-案例)
		3. [js 中 `=>` 的使用](#js-中-的使用)

<!-- /TOC -->

## 2017-11-6


#### 明天要解决的问题

- 集成第三方评论时：
  - 在畅言上注册获取 id 需要 ICP 备案号，网址域名怎么 ICP 备案？有这个必要吗？必须要买服务器吗？
 - 友言注册好了，有 id 并填入了模板中，但网站上说明要将一段 js 代码插入到模板中才能将评论 div 生成，没找到模板文件具体位置。
   - Answer：可以的，在 bluelake 这个主题模板中，只需要将友言的 id 填写到模板文件评论模板中友言中后，再用 hexo 生成新的页面文件部署后就可以看到评论体块在文章下方了。

### 继续上一周小程序的开发问题

### 待办事项

1. 二级域名的配置
2. desktop 的 hosts 配置
3. 域名解析
4. hexo 博客生成时的第三方评论系统


## 整理点

### 腾讯云域名的使用

在腾讯云中域名解析，添加域名记录到其中。

#### 通过 CNAME 将 github 博客域名映射到自己的域名里

在 github 项目设置中，将相关的网址设置在 custom domain 中时，系统会自动在仓库中生成或更新文件 CNAME；
同时，再将域名解析中添加这个仓库的域名到 【信息记录】中。

#### DNS

 DNS ：Domain Name System，域名系统；

 域名的作用是将原本很无趣的 ip 地址给映射成为有意义的名字。

#### 关于二级域名的部署

- 刚才再在腾讯云域名解析网页上看新增域名记录时，光看页面中前端的提示信息，再结合百度搜索出来的答案一下子就明白了。所谓将二级域名部署就是将这个二级域名（比如：blog.kangshan.xyz 这个二级域名就只写 blog 这个主机记录）写在一个 A 类型（将域名指向了一个 ipv4 地址）记录里，而这条记录的值就是自己买的域名的 ip 地址，保存后，就会解析完成。
- 而 A 类型记录与 CNAME 类型的区别就在于：CNAME 是将域名指向另一个域名；

### 怎么查看所访问的网站的 ip 地址？

 通过万能的控制台命令 `ping` ，输入此命令后接上域名后，控制台会在后显示所 ping 的网站的 ip 地址。

#### 目前已完成的 Demo 案例

 - 腾讯云域名已买： `kangshan.xyz`
 - 开发环境与生产环境搭建完；
 - [wafer2 服务端 SDK API文档][bb1e68d2]

   [bb1e68d2]: https://github.com/tencentyun/wafer2-node-sdk/blob/master/API.md#requireqcloud-weapp-server-sdkoptions "wafer 服务端 sdk api 文档"

### js 中 `=>` 的使用

 js 中常用到的表达式： `(x) => x + 3` 。其实它就等价于： `function method(x) {return x +3;}`

## 2017-11-07

### 今日待办事项

#### 社保卡
 - 包括：
	 - [ ] 找到交易密码
	 - [ ] 找到网上查询密码
	 - [ ] 拍照备份到
	 - [ ] 绑定到支付宝上
	 - [ ] 20栋拿快递

## 2017-11-8

不要自己攻击自己。

### 不知所云小点点

#### 所谓：ES6 与 ES5

ES6 是指 javascript 版本是6；

ES 的全称是 ECMA Script。而 ECMA 的全称为：European Computer Manufactures Association ，欧洲计算机制造协会，类似于 ISO（International Standardization Organization）国际标准化组织与 IEC（International Electrotechnical Commission）国际电工技术委员会性质类似，三者都旨在建立统一的电脑格式操作标准。

而 ECMAScript ECMA 就是基于 Netscape Script 标准上的一种标准语言。它往往被称为 javascript，实际上前者是后者的规格，后者是前者的实现。

##### ES6 与 ES5 的区别

babel 是 ES 的转译器，可以将 ES6 编译成 ES5。相对于 ES5 ，ES6 有更多优越的地方。详见网上各种博客。

##### 阮一峰关于 ES6 的书

[ECMAScript6 入门][a4569515]

  [a4569515]: http://es6.ruanyifeng.com/#docs/intro "阮一峰 著"

### 今日计划


## 2017-11-09

### 未完成事项

- [ ] 更新操作手册里权限不对的地方；
- [ ] 思维脑图的简化；
- [ ] 看是否需要将 pdf 手册的目录制作出来 [pdf 制作目录](https://jingyan.baidu.com/article/d2b1d102a262ac5c7e37d43d.html)
