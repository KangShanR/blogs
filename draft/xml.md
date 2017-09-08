---
title: XML的认识
date: 2017-06-13 15:02:43
categories: programming
tags: [programming,leadingEnd]
description: xml的使用与理解
---
# XML概述 

> XML：eXtensible Markup Language,可扩展标记语言：被设计用来传输和储存数据。
> HTML： Hyper Text Markup Language超文本标记语言。


- #### XML与HTML两种语言的区别： ####
	- HTML是被设计用来显示数据，XML被设计用来存储传输数据；
	- HTML的焦点是数据的外观，而XML的焦点在于数据的内容；
	- XML不是对HTML的替代，它们之间形式类似，但功能在本质上有区别；
			<?xml version="1.0" encoding="utf-8"?>
			<note>
				<to>Ross</to>
				<from>Jack</from>
				<heading>Reminder</heading>
				<body>Don't forget me.</body>
			</note>

		- 这就是一个简单的XML文件，它不会做任何事情，只是把数据定义好；
		- *ps:在编程语言中，看到缩写为ML的首先想到是Markup Language标记语言*
- #### XML是W3C推荐标准： ####
	- WWW:World Wide Web万维网
	- W3C： world wide web consortium万维网联盟


#### XML的特点： ####


- 标签可自定义，比如上面的<to><from>这些标签都是创建者自己写的
- 对XML最好的描述是：独立于软件与硬件的独立传输工具；
- XML无处不在，不亚于HTML对于web的意义；
- XML以纯文本格式存储
- XML有很强的自我描述性
- XML的树形结构：
	- 像HTML标记语言一样，其结构明确，子元素包含于父元素中
	- XML必须有根元素
	- XML的声明如果有必须放在第一行
	- 上面例子 就声明了XML的版本与编码字符集为UTF-8，SQL,JS,HTML5，PHP的默认编码都是UTF-8
	- 所有的XML标签必须有一个关闭标签(声明不算是内容），这一点与HTML不同，HTML中有部分标签不用关闭标签eg：<br><p>
	- XML对大小写敏感
	- 嵌套要正确，当一个标签在另一个标签中打开，那么它的关闭也必须在这一个标签中
	- XML的属性值必须加引号
			<note date="2011.10.5">
				<to>Jack</to>
				<from>Ross</form>
				<content>Thanks</content>
			</note>


### 语法： ###

- 所有xml元素必须有一个关闭标签
- xml标签对大小写敏感；
- XML标签必须嵌套：成对出现
		<user>
			<id value="1"/>
			<username>kfc</username>
		</user>

- xml文档必须有根元素
- xml属性值必须加引号
- 从上面的xml中，我们可以看到根元素是user，其中又有两个子元素：id/username；而id这个元素有一个属性value这个属性值为“1”，而另外一个子元素username有内容“kfc”；

##xml文档的生成与解析方式:##

- 共4种方式：

	- DOM生成和解析xml文档；
		- 解析器将读入整个文档，然后构建一个**驻留内存的树结构**，然后代码就可以使用**DOM接口**来操作这个树结构；
		- 因为DOM是基于信息层次的，所以DOM被认为是基于树或基于对象的，而树在内存中是持久的，因此可以通过修改树来实现应用程序对数据和结构作出更改；也因为持久性的原因，DOM使用起来也要简单地得多；
		- 因此，其优点就在于整个文档在内存（包括无用的节点）便于操作；支持删除、修改、重新排列等多种功能；其缺点就在于是整个文档在内存中浪费时间与空间；
		- 使用场合：一里弄解析了文档还需多次访问这些数据；硬件资源充足（内存、cpu）；
	- SAX生成和解析xml文档；
		- 基于**事件驱动**解析，当解析器发现元素开始、元素结束、文本、文档的开始或结束等时，发送事件，响应事件的代码由程序员来编写，保存数据之类的；
		- 采用基于事件的模型，对于大型文档的解析比起DOM要更快且对内存的要求也更低；
		- 优点，不用事先地调入整个文档，占用资源少，加载负荷更小，加载的速度也更快。解析代码比DOM解析器代码少，适于Applet、下载；
		- 缺点，不是持久的，事件过后，若没有保存数据，那么数据就丢失，无状态性。从事件中只能得到文本，但不知道文本属于哪个元素；
		- 使用场合：Applet；只需xml文档的少量内容，很少回头访问，机器内存少；
	- JDOM生成和解析xml文档；
		- 设计目的是java特定文档模型，20-80原则（使用20%的精力解决80%的java、xml问题），解决DOM、SAX的编码量；
		- 本身不包含解析器，使用SAX2解析器来解析和验证输入XML文档；
		- 使用场合：实现功能简单，如解析、创建等；
	- DOM4J生成和解析xml文档；
		- 性能优异，功能强大，易用；
		- 也是一个开源的软件，Sun的JaxM也在用DOM4J，Hiberbate也使用Dom4j读取xml文件;

- 总结：
	- xml文件解析之后，成为一个document对象；
	- DOM4J性能最好，应用也更广泛；
	- JDOM和DOM在性能测试时表现不佳，在测试10M文档时内存溢出，在小文档情况下使用他们更好。但同时，DOM的跨语言解析要强一些，也正因为如此获得W3C的推荐；
	- SAX基于其事件驱动的解析方式，其在检测即将到来的xml流但并不载入内存中，让它在小文档解析中有一定的优势。


## xml声明部分 ##


- 声明部分是xml文件的可选部分，如果存在就一定放在文档的第一行；
	- 其内容：
			<?xml version="1.0" encoding="utf-8"?>

	- 声明了xml的版本，与encoding格式，同时UTF-8 也是 HTML5, CSS, JavaScript, PHP, 和 SQL 的默认编码。

## 实体引用


- 在 XML 中，一些字符拥有特殊的意义。
	- 如果您把字符 "<" 放在 XML 元素中，会发生错误，这是因为解析器会把它当作新元素的开始。
	- 这样会产生 XML 错误：
			<message>if salary < 1000 then</message>

	- 为了避免这个错误，请用实体引用来代替 "<" 字符：
			<message>if salary &lt; 1000 then</message>

	- 在 XML 中，有 5 个预定义的实体引用：
			&lt;	<	less than
			&gt;	>	greater than
			&amp;	&	ampersand
			&apos;	'	apostrophe
			&quot;	"	quotation mark

	- 注释：在 XML 中，只有字符 "<" 和 "&" 确实是非法的。大于号是合法的，但是用实体引用来代替它是一个好习惯。[参考博客](http://www.runoob.com/xml/xml-syntax.html "参考的菜鸟教程")

### xml的可扩展性 ###


- 任何一个xml的标签都可以无限扩展开，比如在<bean></bean>中，有一个元素为<date>,我们可以将年月日按一定格式存储进去
		<bean>
			<date>2017_7_30</date>
		</bean>

	- 利用元素的可扩展性就可以让我们这们来写：
		<bean>
			<date>
				<year>2017</year>
				<month>7</month>
				<date>30</date>
			</date>
		</bean>

	- 这样我们的数据的扩展性变得极性；