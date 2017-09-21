---
title: JSP的九大内置对象与四大作用域
date: 2017-07-25 13:04:38
categories: programming
tags: [java,jsp,programming]
keywords: 
---

# JSP的内置对象 #
> jsp作为一个特殊的Servlet，在JavaWeb动态页面中经常使用到它。作为一个特殊的Servlet它同样为Servlet容器所管理，在同样有生命周期，具体可以参考：[Jsp的认识](http://kangshan.oschina.io/2017/09/14/jsp/ "Jsp的认识")
> Jsp的内置对象共有9个，每个对象的类型与常用到的方法各不相同。

<!--more-->

## Jsp的九大内置对象 ##

1. **page**（Object)，当前jsp的本身，相当于`this`，当前对象，类型为Object；
2. **pageContext**，就是`PageContext`类型，页面上下方，域对象，是内置对象中范围最大的对象；
3. **config**，(ServletConfig对象)，就对象page中的`ServletConfig`对象，代表了jsp页面的配置信息，但一般不存在配置信息，在servlet中使用；
4. **exception**（Throwable对象)，只有在错误处理页面（即在page指令中指定isErrorPage属性为true时）才可以使用这个对象；
5. **session**（HttpSession)，代表一次会话，从客户端连接上服务器到关闭浏览器结束访问，这个会话对象一直存在于这其中；
	1. `<%@page session="false"%>`，这个指令一旦形成，就会造成这个页面不能使用；
	1. 常用的方法：
		1. getAttribute();
		2. setAttibute();
6. **request**(HttpServletRequest)，该对象封装了一次浏览器对服务器的请求，客户端请求的参数都封装在这个对象中；
	1. 常用的方法：
		1. getParameter(String paramName);
		2. getAttribute(String attributeName);
3. **response**(HttpServletResponse)，代表**服务器对客户端的响应**。但我们一般响应也用out对象输出响应；
	1. sendRedirect(String url);
4. **out**（JspWriter)，用于把结果输出到网页上；
	1. 方法：
		1. clear()，清除缓冲区的内容，不会输出到页面上；
		1. clearBuffer()，清除缓冲区的内容，并输出到页面上；
		2. close()，关闭输出流，清除所有内容；
		3. void println(Object obj)，将指定的类型的对象输出到Http流，换行。可以用print()，方法不换行，参数类型可以是多种，类似`System.out.println()`;
4. **application**(ServletContext)，用于取得或更改Servlet的设定；
	1. 该对象的**生命周期是服务器启动到服务器关闭**，主要用于实现用户数据共享，存放应用的全局变量，这样不同用户登录同一页面可以实现对同一数据的更改；
	1. 方法：
		1. Object getAttribute(String name)；
		2. void setAttribte(String name,Object value);

## Jsp四大域对象 ##
> 在jsp九大内置对象中，其中有四大域对象，可用于指定域中进行查找对象；

### 四大域对象： ###

|名称|作用域|
|:--:|:--|
|application|整个应用程序|
|session|当前会话有效|
|request|当前请求有效|
|page|在当前页面有效|
