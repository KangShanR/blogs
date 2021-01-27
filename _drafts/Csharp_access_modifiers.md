---
title: C#编程中的访问修辞符
date: 2016-12-11 15:02:43
categories: programming
tags: [programming,C#]
description: C#中的访问修辞符的认识
---

C#中为了数据的安全性以及代码的拓展，定了几种访问级别，根据对应的访问修饰符，可以获取Class里面对应的字段、属性、方法等。

常见的访问修饰符有：
（1）public	公有访问。不受任何限制。
（2）private	私有访问。只限于本类成员访问，子类都不能访问。
（3）protected	保护访问。只限于本类和子类访问。
（4）internal	内部访问。只限于本项目内访问，其他不能访问。
（5）protected internal	内部保护访问。只限于本项目或是子类访问，其他不能访问。

根据MSDN上面的解释，我们也可以理解对应[访问修饰符](https://msdn.microsoft.com/zh-cn/library/wxh6fsc7.aspx)的作用。

[参考博客](http://www.cnblogs.com/liupeng61624/p/5122192.html "C#访问修辞符")

#### 常见成员类型的访问修饰符

**（1）接口(interface)**

接口成员访问修饰符默认为public,且不能显示使用访问修饰符。

**（2）类(class)**
构造函数默认为public访问修饰符。
析构函数不能显示使用访问修饰符且默认为private访问修饰符。
类的成员默认访问修饰符为private（*java中默认为同包权限，在同包中可以被访问的，默认不写*）;

**（3）枚举(enum)**
枚举类型成员默认为public访问修饰符，且不能显示使用修饰符。

**（4）结构(struct)**

结构成员默认为private修饰符。
结构成员无法声明为protected成员，因为结构不支持继承。

**（5）嵌套类型**
嵌套类型的默认访问修饰符为private。 和类，结构的成员默认访问类型一致。
