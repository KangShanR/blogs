---
title: Struts2框架的总结
date: 2017-09-03 12:23:30
categories: programming
tags: [programming,java,framework]
keywords: 

---

## Struts2 ##

> 谈到Struts2不得不谈到MVC模式：模型-视图-控制器;
> MVC架构将程序分割成若干逻辑部件，使程序设计变得坚韧更加容易。提供了一种按功能对各种对象（用来维护和表现数据的对象）进行分割的方法，而将各对象间的耦合程度降到最低。
> Struts2就实现了MVC架构下的各个功能；<!--more-->

### Ajax ###
> 概念：**Asynchronous javascript and xml，异步js和xml，所谓异步就是指异步调用web浏览器组件**。
> Google地图就对这个一技术完美应用，在地图中，网页是活动的，但你可以用鼠标实现各个组件功能，当用户界面与Ajax结合后，只有在必要时才会向服务器发起请求，获得少量必要的信息。提高了信息的**可重用性**。

### Struts2的优点 ###
1. 无侵入性设计；
2. 提供了拦截器，可以实现AOP编程，比如权限的拦截；
3. 提供了类型转换器，可以把特殊的请求参数转换成需要的类型；
4. 支持多种表现层技术；
5. 输入校验可以对指定方法进行校验；
6. 提供了全局范围、包范围和Action范围的国际化资源文件管理实现；

### Struts2的配置文件 ###
> Struts2r的配置文件有以下5个，但除web.xml外，其他都是可选的；
> 其中，常用到的配置struts.xml；

1. web.xml，web部署描述符，包括所有必需的框架组件；
2. struts.xml，主要的配置文件，包含result映射、action映射、拦截器配置等；
3. struts.properties，Struts2框架的属性配置
4. struts-default.xml，默认配置，由框架提供
5. strtus-plugin.xml，Struts2插件所用的配置文件，由插件提供；

#### Struts.xml配置 ####
1. 常量配置：Constant，
	1. 常量的配置改变Struts2的框架属性与插件行为；
	2. 常量可以在多个配置文件中声明，但Struts2框架按照以下文件顺序来搜索常量，**越先后的文件优先级越高**：
		1. struts-default.xml
		2. struts-plugin.xml
		3. struts.xml
		4. struts.properties
		5. web.xml
	6. 常量的属性有：
		1. name，常量的名字（必需）
		2. value，常量的值（必需）
3. 包的配置：Package
	1. 包提供了**action/result/result类型、拦截器、拦截器栈组织为一个逻辑单元**，提高重用性，简化维护工作；
	2. 包可以实现继承，将重用的写成父包，但由于**配置文件内容有先后顺序，所以父包一定在子包之前定义**；
	3. 同时可以定义抽象包（abstract属性为true），作为一个**抽象包不能有action定义**，因为action就是实现了这个具体的动作嘛。抽象包用于被子包继承。
	4. 包的完整属性有点：
		1. name，必需，被其他包引用时的key键
		2. extends，非必需，指定要扩展的包
		3. namespace，非必需，指定命名空间
			1. 包的命名空间有三种：
				1. 默认命名空间：default，eg：`<package name="my" namespace="default" extends="struts-default">`
				2. 根命名空间：`/`，eg：`<package name="my" namespace="/" extends="struts-default">`
				3. 自定义的命名空间：eg：`<package name="my" namespace="/myNamespace" extends="struts-default">`
			4. 这三种命名空间的被**查找顺序**是：**先在指定的命名空间（可以是自定义的，也可以是指定的根命名空间）去找，没找到再到默认命名空间去找**；
		4. abstract，非必须，声明包为抽象的；
