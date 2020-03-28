---
title: Struts2框架的总结
date: 2017-09-03 12:23:30
categories: programming
tags: [programming,java,framework]
keywords: 

---

# 1. Struts2

<!-- TOC -->

- [1. Struts2](#1-struts2)
	- [1.1. Struts2的优点](#11-struts2%e7%9a%84%e4%bc%98%e7%82%b9)
	- [1.2. Struts2 的配置](#12-struts2-%e7%9a%84%e9%85%8d%e7%bd%ae)
		- [1.2.1. 常量配置 Constant](#121-%e5%b8%b8%e9%87%8f%e9%85%8d%e7%bd%ae-constant)
		- [1.2.2. 包的配置](#122-%e5%8c%85%e7%9a%84%e9%85%8d%e7%bd%ae)
		- [1.2.3. 配置动态方法调用](#123-%e9%85%8d%e7%bd%ae%e5%8a%a8%e6%80%81%e6%96%b9%e6%b3%95%e8%b0%83%e7%94%a8)
	- [1.3. action 的书写方式](#13-action-%e7%9a%84%e4%b9%a6%e5%86%99%e6%96%b9%e5%bc%8f)
	- [1.4. questions](#14-questions)

<!-- /TOC -->

> 谈到Struts2不得不谈到MVC模式：模型-视图-控制器;
> MVC架构将程序分割成若干逻辑部件，使程序设计变得坚韧更加容易。提供了一种按功能对各种对象（用来维护和表现数据的对象）进行分割的方法，而将各对象间的耦合程度降到最低。
> Struts2就实现了MVC架构下的各个功能；<!--more-->

## 1.1. Struts2的优点

1. 无侵入性设计；
2. 提供了拦截器，可以实现AOP编程，比如权限的拦截；
3. 提供了类型转换器，可以把特殊的请求参数转换成需要的类型；
4. 支持多种表现层技术；
5. 输入校验可以对指定方法进行校验；
6. 提供了全局范围、包范围和Action范围的国际化资源文件管理实现；

## 1.2. Struts2 的配置

> Struts2r的配置文件有以下5个，但除web.xml外，其他都是可选的；
> 其中，常用到的配置struts.xml；

1. 配置文件
   1. web.xml，web部署描述符，包括所有必需的框架组件；
   2. struts.xml，主要的配置文件，包含result映射、action映射、拦截器配置等；
   3. struts.properties，Struts2框架的属性配置
   4. struts-default.xml，默认配置，由框架提供，struts.xml 中的包全都要继承自此配置文件
   5. strtus-plugin.xml，Struts2 插件所用的配置文件，由插件提供；
2. 引入其他 struts 配置 `<include>resouces_path</include>`

### 1.2.1. 常量配置 Constant

常量的配置改变 Struts2 的框架属性与插件行为。常量的配置有 3 种方式（加载顺序如下，后加载的会覆盖先加载的）

1. 直接添加一个 struts.properties 在 classpath 中，覆盖默认的
2. 在 struts.xml 配置文件中添加
3. 在 web.xml 配置文件中添加 `<context-param>`

需要注意的配置有

1. 国际化解决 post 请求乱码 `struts.i18n.encoding` `UTF-8`
2. 指定访问 action 后缀名 `struts.action.extension`，可指定多个用 `,` 分隔。 `action,,` 表示后缀是 `action` 或 不要后缀都可
3. 指定是否以开发模式运行 `struts.devMode` 默认为 `false`
   1. 开发模式下 struts 会有线程监控 struts.xml 主配置文件的变化，实现热部署

### 1.2.2. 包的配置

在 core 包中有相应的默认的配置

1. 包提供了**action/result/result类型、拦截器、拦截器栈组织为一个逻辑单元**，提高重用性，简化维护工作；
2. 包可以实现继承，将重用的写成父包，但由于**配置文件内容有先后顺序，所以父包一定在子包之前定义**；
3. 同时可以定义抽象包（abstract 属性为 true），作为一个**抽象包不能有action定义**，因为action就是实现了这个具体的动作嘛。抽象包用于被子包继承。
4. 包的完整属性有：
	1. name，必需，被其他包引用时的key键
	2. extends，非必需，指定要扩展的包
	3. abstract，标识性属性，非必须，声明包为抽象的，用于继承不能独立运行；
	4. namespace，非必需，指定命名空间，包的命名空间有三种（这三种命名空间的被**查找顺序**是：先在指定的命名空间（可以是自定义的，也可以是指定的根命名空间）去找，没找到再到默认命名空间去找）
		1. 默认命名空间：default，eg：`<package name="my" namespace="default" extends="struts-default">`
		2. 根命名空间：`/`，eg：`<package name="my" namespace="/" extends="struts-default">`
		3. 自定义的命名空间：eg：`<package name="my" namespace="/myNamespace" extends="struts-default">`
5. package 的子元素 action
	1. name 属性， 指定 action 的名，决定资源访问名
	2. class 属性，action 的完整类名，如果不指定，默认 `com.opensymphony.xwork2.ActionSupport`
	3. method 属性，指定由 action 哪个方法来处理请求，默认值 `excute`
	4. result 子元素，对结果配置
		1. type 属性，指定哪一个类来处理结果，默认使用 `dispatcher` 转发
		2. name 属性，标识结果处理名称，与 action 方法返回值对应，默认 `success`
		3. 标签体，指定页面相对路径
6. 指定包中默认 `action` : `default-action-ref name="action_name"` ，如果访问的 action 找不到将使用此指定 action

### 1.2.3. 配置动态方法调用

使用动态方法调用配置可实现配置一个 action 将 action 中所有方法都配置上。

两种方式

1. 打开 struts 的动态方法调用配置： `struts.enable.DynamicMethodInvocation=true` 默认为 false，在访问资源的时候在 action 类名后资源后缀（可能没有）前加上 `!` 并接上方法名
2. 关闭动态方法调用配置：`struts.enable.DynamicMethodInvocation=false`，在 action 配置时给 action 指定 `name` 属性时加上 `*`，eg:`SomeAction_*`，再在 `method` 上使用 `{1}` 引用 `*`，这儿的 `*` 可以指定多个，后面引用时相应地增加 index。在访问该 action 资源时，只需要匹配 name 符，struts 就会用此处点位符当作 `method` 值访问 action 指定的方法。

## 1.3. action 的书写方式

struts2 由 struts1 而来，所以 struts1 原来的包 xwork 在 struts2 中很常见。

1. 直接以 Action 为尾名写一个普通 java 类。eg : `LoginAction`。使 struts2 代码侵入性更低，相对 servlet，如果要实现一个 servlet 必须继承或实现生相应的接口，开发中并不常用。
2. 继承 Action，其中预定义了很 result 常量，诸如： `SUCCESS/ERROR`
3. 继承 `ActionSupport`，其继承自多个类，需要使用相应组件时实现即可。

## 1.4. questions

1. struts2 启动后，tomcat 报出错误 `org.apache.tomcat.util.bcel.classfile.ClassFormatException: Invalid byte tag in constant pool: 19` ?
   1. 参考[stackoverflow](https://stackoverflow.com/questions/23541532/org-apache-tomcat-util-bcel-classfile-classformatexception-invalid-byte-tag-in) 升级了 tomcat 版本到 8.5 后不再有。
