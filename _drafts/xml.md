---
title: XML的认识
date: 2017-06-13 15:02:43
categories: programming
tags: [programming,leadingEnd]
description: xml的使用与理解
---

> XML：eXtensible Markup Language, 可扩展标记语言：被设计用来传输和储存数据。
> HTML： Hyper Text Markup Language 超文本标记语言。

## XML与HTML两种语言的区别

- HTML是被设计用来显示数据，XML被设计用来存储传输数据；
- HTML的焦点是数据的外观，而XML的焦点在于数据的内容；
- XML不是对HTML的替代，它们之间形式类似，但功能在本质上有区别；

```xml
<?xml version="1.0" encoding="utf-8"?>
<note>
  <to>Ross</to>
  <from>Jack</from>
  <heading>Reminder</heading>
  <body>Don't forget me.</body>
</note>
```

- 这就是一个简单的XML文件，它不会做任何事情，只是把数据定义好；
- *ps:在编程语言中，看到缩写为 ML 的首先想到是 Markup Language 标记语言*

### XML是W3C推荐标准

- WWW:World Wide Web万维网
- W3C： world wide web consortium 万维网联盟

### XML的特点

- 标签可自定义，比如上面的 `<to><from>` 这些标签都是创建者自己写的
- 对XML最好的描述是：独立于软件与硬件的独立传输工具；
- XML无处不在，不亚于HTML对于web的意义；
- XML以纯文本格式存储
- XML有很强的自我描述性
- XML的树形结构：
    - 像HTML标记语言一样，其结构明确，子元素包含于父元素中
    - XML必须有根元素
    - XML的声明如果有必须放在第一行
    - 上面例子 就声明了XML的版本与编码字符集为UTF-8，SQL,JS,HTML5，PHP的默认编码都是UTF-8
    - 所有的XML标签必须有一个关闭标签(声明不算是内容），这一点与HTML不同，HTML中有部分标签不用关闭标签eg：`<br><p>`
    - XML对大小写敏感
    - 嵌套要正确，当一个标签在另一个标签中打开，那么它的关闭也必须在这一个标签中
    - XML的属性值必须加引号

```xml
<note date="2011.10.5">
  <to>Jack</to>
  <from>Ross</form>
  <content>Thanks</content>
</note>
```

### 语法

- 所有xml元素必须有一个关闭标签
- xml标签对大小写敏感；
- XML标签必须嵌套：成对出现

```xml
<user>
  <id value="1"/>
  <username>kfc</username>
</user>
```

- xml文档必须有根元素
- xml属性值必须加引号
- 从上面的 xml 中，我们可以看到根元素是 user ，其中又有两个子元素： id/username ；而 id 这个元素有一个属性 value 这个属性值为 “1”，而另外一个子元素 username 有内容 kfc；

### xml文档的生成与解析方式

4 种方式

- DOM 生成和解析 xml 文档；
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

#### xml 解析示例

java 工程中各种 xml 配置文件的解析都可以用这些解析器来获取配置信息，常用 jar 包： `dom4j.jar` 。 spring 可以根据解析出来的各个 bean 的 class 信息，再利用反射实例化 bean 实现层层之间调用的解耦。

```java
public static String getDomElementValue(String url) throws DocumentException {
    SAXReader saxReader = new SAXReader();
//        Document doc = saxReader.read(new File("src/com/kang/jdbcdemo/web.xml"));
    Document  doc       = saxReader.read("src/com/kang/jdbcdemo/web.xml");
    Element   root      = doc.getRootElement();
    List<Element>      mappings  = (List<Element>)root.elements("servlet-mapping");
    String servletName = mappings.stream().filter(
            e -> url.equals(e.element("url-pattern").getText()))
            .findFirst().get().element("servlet-name").getText();

    List<Element>      servlets  = (List<Element>)root.elements("servlet");
    return servlets.stream().filter(e -> servletName.equals(e.element("servlet-name").getText()))
            .findFirst().get().element("servlet-class").getText();
}
```

## xml 声明部分

- 声明部分是xml文件的可选部分，如果存在就一定放在文档的第一行；
    - 其内容：`<?xml version="1.0" encoding="utf-8"?>`
    - 声明了xml的版本，与encoding格式，同时UTF-8 也是 HTML5, CSS, JavaScript, PHP, 和 SQL 的默认编码。

## 实体引用 entity

- 在 XML 中，一些字符拥有特殊的意义。
    - 如果您把字符 "<" 放在 XML 元素中，会发生错误，这是因为解析器会把它当作新元素的开始。
    - 这样会产生 XML 错误：`<message>if salary < 1000 then</message>`
    - 为了避免这个错误，请用实体引用来代替 "<" 字符：`<message>if salary &lt; 1000 then</message>`
- 在 XML 中，有 5 个预定义的实体引用：
  1. `&lt;` `<` less than
  2. `&gt;` `>` greater than
  3. `&amp` `&` ampersand
  4. `&apos;` `'` apostrophe
  5. `&quot;` `"` quotation mark
- 注释：在 XML 中，只有字符 "<" 和 "&" 确实是非法的。大于号是合法的，但是用实体引用来代替它是一个好习惯。[参考博客](http://www.runoob.com/xml/xml-syntax.html "参考的菜鸟教程")
- 如果在 xml 中需要写大量的此类符号（直接写代码或写 sql），如果使用实体引用可读性就低，书写难度也会增加，此时可以使用 `<![CDATA[]]>` 用来包装代码（代码写在最中间 `[]` 内）。 CDATA -> character data

### xml的可扩展性

- 任何一个 xml 的标签都可以无限扩展开，比如在 `<bean></bean>` 中，有一个元素为 `<date>`，我们可以将年月日按一定格式存储进去

```xml
<bean>
  <date>2017_7_30</date>
</bean>
```

- 利用元素的可扩展性就可以让我们这们来写：

```xml
<bean>
  <date>
    <year>2017</year>
    <month>7</month>
    <date>30</date>
  </date>
</bean>
```

- 这样我们的数据的扩展性变得更大；

## DTD

> document type definition 文档类型定义，用以规范 xml 文档

在 xml 文件中引入 dtd 的方法：

- 在 xml 内部申明：`<!DOCTYPE (root element) [dtd leaf element]>`
- 引入使用本地 DTD `<!DOCTYPE (name) SYSTEM "url/web-app_2_3.dtd(dtd path)">`
- 引入公共 DTD `<!DOCTYPE (name) PUBLIC "url/web-app_2_3.dtd(dtd path)">`

### DTD element

> In a DTD, XML elements are declared with the following syntax:
`<!ELEMENT element-name category>` or `<!ELEMENT element-name (element-content)>`

### DTD attribute

> An attribute declaration has the following syntax: `<!ATTLIST element-name attribute-name attribute-type attribute-value>`
DTD example: `<!ATTLIST payment type CDATA "check">`
XML example: `<payment type="check" />`

## schema

> An XML Schema describes the structure of an XML document.The XML Schema language is also referred to as XML Schema Definition (XSD).[reference](https://www.w3schools.com/xml/schema_intro.asp)

### xml 引入 schema

This XML document has a reference to an XML Schema:

```xml
<?xml version="1.0"?>

<note xmlns="https://www.w3schools.com" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="https://www.w3schools.com/xml note.xsd">
  <to>Tove</to>
  <from>Jani</from>
  <heading>Reminder</heading>
  <body>Don't forget me this weekend!</body>
</note>
```

### element

> The syntax for defining a simple element is:
<xs:element name="xxx" type="yyy"/>
where xxx is the name of the element and yyy is the data type of the element.

XML Schema has a lot of built-in data types. The most common types are:

- xs:string
- xs:decimal
- xs:integer
- xs:boolean
- xs:date
- xs:time

### indicators

指示符，用以指示 xml 各节点元素的限定。

> There are seven indicators:

- Order indicators:
    - All 不限顺序
    - Choice `<xs:choiece></xs:sequence>` 顺序出现
    - Sequence `<xs:sequence></xs:sequence>` 顺序出现
- Occurrence indicators: 出现频率指示
    - maxOccurs
    - minOccurs
- Group indicators:
    - Group name
    - attributeGroup name
