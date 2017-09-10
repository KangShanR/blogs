---
title: HTTP协议的理解与认识
date: 2017-05-06 09:54:30
categories: programming
tags: [programming,HTTP]
keywords: 
description: HTTP协议对于一个网络编程者来说很重要，这儿写一些http的认识。
---
#HTTP协议#
- 概念：
	- http：hyper text transfer protocol，超文本传协议，其设计之初的目的只是提供一种接收和发布html页面的方法；
	- 用于从WWW服务器传输超文本到本地浏览器的传送协议。它可以使浏览器更加高效，使网络传输减少。它不仅保证计算机正确快速地传输超文本文档，还确定传输文档中的哪一部分，以及哪部分内容首先显示(如文本先于图形)等。
	- HTTP是一个应用层协议，由请求和响应构成，是一个标准的客户端服务器模型。HTTP是一个无状态的协议。*所谓无状态协议，是指这一次客户端的请求与上一次的请求无关。*<!--more-->
	- **http的post和get方法性能上的区别：**
		- get是从服务器上获取数据，post是向服务器传送数据。
		- get是把参数数据队列加到提交表单的ACTION属性所指的URL中，值和表单内各个字段一一对应，在URL中可以看到。post是通过HTTP post机制，将表单内各个字段与其内容放置在HTML HEADER内一起传送到ACTION属性所指的URL地址。用户看不到这个过程。
		- 对于get方式，服务器端用Request.QueryString获取变量的值，对于post方式，服务器端用Request.Form获取提交的数据。
		- get传送的数据量较小，不能大于2KB。post传送的数据量较大，一般被默认为不受限制。但理论上，IIS4中最大量为80KB，IIS5中为100KB。
		- get安全性非常低，post安全性较高。但是执行效率却比Post方法好。
		- 因此建议：
			- get方式的安全性较Post方式要差些，包含机密信息的话，建议用Post数据提交方式；
			- 在做数据查询时，建议用Get方式；而在做数据添加、修改或删除时，建议用Post方式；

## OSI模型 ##
- 概念：Open System Interconnect，开放式系统互联，
	- 这个模型把网络通信的工作分为7层：
		- 由低到高：物理层（Physical Layer），设备：中继器，集线器，单位：比特（bit）
		- 数据链路层（Data Link Layer)，设备：二层交换机、网桥，单位：帧（frame)
		- 网络层（Network Layer)，设备：路由器，单位：数据包（packet)，相关：ip地址、数据包、路由协议、ARP（地址解析协议）
		- 传输层（Transport Layer)，提供端到端的透明数据传输服务，单位：数据包（packets)
		- 会话层（Session Layer)，建立访问验证会话管理在内的建立维护应用之间通信的机制
		- 表示层（Presentation Layer)，格式化的表示和转换数据服务，数据的压缩与解压，加密与解密
		- 应用层（Application Layer)，操作系统或网络应用程序提供访问网络服务的接口，此层协议就包括了：Http,FTP,SNMP,Telnet
	- 低三层负责创建通信连接的链路，高四层负责端到端的数据通信；
	- 网络通信在发送端自上而下，在接收端自下而上依层进行；但也不一定要经过全七层，比如：中继器之间的连接只需要在物理层中进行，而路由器之间的连接只需通过下三层；
	- 但双方端端之间的通信必须是对等的，不可能存在不对等层次间的通信（也就是在发送端通过了哪些层，在接收端也要反序通过这些层）；


### TCP/IP模型 ###
> OSI系统只是提供了网络通信的模型，实际应用中，我们使用的最成熟最广的互联协议是TCP/IP模型基础上建立的，它相对于OSI模型更为精简；
> TCP：传输控制协议（Transport Control Protocol)
> IP：Internet Protocol，网络协议
> UDP：User Datagram Protocol,用户数据包协议

**分为四层：**
- 网络访问层：对应OSI的物理层+数据链路层
- Internet层：对应OSI网络层
- 传输层：对应OSI的传输层
- 应用层：对应OSI的会话层、表示层、应用层