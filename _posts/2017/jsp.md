---
title: jsp的认识
date: 2017-09-14 13:04:38
categories: programming
tags: [java,jsp,programming]
keywords: 

---


# JSP #

> 概念：Java Server Pages；
> jsp技术使用java语言作为脚本语言，用于提供一个接口用于服务器响应HTTP请求的程序；
> jsp是一个特殊的servlet,它最终被执行成一个java的class文件并置入内存，当响应开始后会利用输出流生成一个Html页面传送给请求方；

<!--more-->

## Jsp的工作原理 ##
1. 当请求方请求一个jsp页面时，jsp先被服务器中的**Servlet引擎转换成一个java源文件**，这个过程会对jsp页面中的**语法进行检查**，如果页面语法没有错则转换成功，这时Jsp引擎再利用**javac**将java源文件**编译成class文件加载到内存中**；
2. 其次再创建一个**Servlet的对象**，并执行该对你的`jspInit()`方法，与Servlet生命周期中一致这个`jspInit()`方法在其生成周期中只被执行一次；
3. **创建并启动一个线程**，线程来调用对象的`jspService()`方法（如果有多个请求同时请求一个jsp，则jsp引擎**会创建多个线程来请求**）；
4. Servlet容器将**请求方的参数封装到HttpServletRequest对象**中并再生成一个HttpServletResponse对象，将这两个对象作为参数传给`jspService()`方法；
5. 与servlet一样，jspServlet会调用Request对象中相应的方法将封装在其中的参数取出来，再**调用Response中相关的方法进行业务逻辑处理**，生成响应数据；
6. `jspService()`方法将执行完的结果返回给请求方；

## Jsp指令 ##

1. **page：**
	1. `<%@ page%>`作用于整个jsp页面，包括静态的包含文件；
	2. 一般放在jsp页面顶部，但不管放在哪个地方其作用范围都是整个jsp；
	3. 该指令不能作用于动态包含文件
	4. 可以在一个页面中用上多个，但是其中的属性只能用一次，不过`import`属性像java中的`import`一样，可以多次使用；
5. **taglib:**
	1. 表明此页面是使用的什么标签，也指定标签的前缀（比如，struts的标签，jstl标签）；
2. **include**
	1. 包含指令，将会在jsp编译时插入一个包含文本或代码的文件；
	2. 这个包含的文件可以是html文件也可以是jsp文件、文本文件、java代码。但在这些包含文件中不能使用<html><body>，这些标签将会影响jsp文件中的标签的解析；