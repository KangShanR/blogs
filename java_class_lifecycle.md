---
title: Java类的生命周期
date: 2017-05-29 13:04:38
categories: programming
tags: [java,JVM,programming]
keywords: 
---

# Java类的生命周期 #
> java作为一门面向对象的高级语言，类是其面向对象思想的重要一环，也是JVM在为Java程序提供运行环境时的重要因素。只有在类的生命周期中，类才能被使用，被实例化；
<!--more-->

## 类的生命周期 ##
1. **加载**
	1. **装载**，**将类的`.class`文件的二进制数据读入到内存中，存放在运行时数据区的方法区中，同时在堆区创建相应的一个`java.lang.class`对象，这个class对象用来封装类在方法区中的数据结构**；
		1. 类加载来源有：
			1. **本地文件系统**中加载类的`.class`文件，常用到的加载方式；
			2. 网络下载的`.class`文件
			3. 从ZIP、JAR或其他类型的**归档文件**中提取`.class`文件，我们jre提供的jar包就是这一类型；
			4. 专有的数据库中提取`.class`文件
			5. 将**Java源文件动态编译**为`.class`文件，一般的IDE都可以实现动态编译，而我们安装jre环境后，命令行运行`java`就是执行这一编译操作；
		6. **类加载的结果就是在堆区一个个Class对象，而这些对象就提供了与方法区中的类的数据结构的接口**；
		7. 类加载器：
			1. JVM提供的类加载器有：启动类加载器、扩展类加载器、系统类加载器
			1. 用户自定义加载器：继承`java.lang.ClassLoader`类即可；
	2. **链接**，链接的目的是**将加载到内存中的类的二进制数据合并到JVM的运行时环境中**。
		1. **验证**：目的是**保证被加载的类有正确的数据结构**并保持与其他的类的协调一致；
			1. 验证内容有：
				1. 类文件结构的检查：类文件是Java类文件的固定格式；
				2. 语义检查：类符合Java文言的语法，eg：final类没有子类，final成员没有被覆盖；
				3. 字节码验证：确保字节码流要吧被JVM安全地执行。**字节码流代表Java方法（类方法与实例方法）**，由操作码的单字节指令组成，每一个操作码后都跟着一个或多个操作数据。这个验证就是检查操作码是否合法，是否有合法的操作数；
				4. 引用验证：相互引用之间的类的协调一致；
		2. **准备**：为类的静态成员分配内存，并将其**初始化为默认值**。可以这样理解，当为一个成员分配内存时，这时你不得不用一个值来占用这个块内存，所以都会为各个数据设置一个默认值。
		3. **解析**：将类中的**符号引用转换成直接引用**，这是Java跨平台的重要一环，java代码里写好后，对于各个堆中的对象引用只是符号的引用，这个引用会随着运行平台的不同而不同，所以JVM就实现当在特定平台加载类时生成不同直接引用来指向这个对象。*[Java引用](http://kangshan.oschina.io/2017/08/13/java_reference/ "java底层之引用")*
	3. **初始化**，这一过程会将类中的**静态变量赋予指定的初始值**，如果没有初始值就保持默认值；
		1. 初始化的要点：
			1. 类的静态成员都会依次按**顺序**被初始化，不管是静态变量还是静态代码块；
			2. 初始化之前要先加载与链接；
			3. 初始化本类前先将其父类初始化（接口中不会存在），也就是是说程序中第一个被初始化的类应该是`Object`；
		4. 类的初始化时机：
			1. 创建类的实例时（new、反射、克隆、反序列化）；
			2. 调用类的静态方法；
			3. 访问类或接口的静态变量，或对其静态变量赋值；
			4. Java API的反射方法：Class.forName(String methodName);
			5. 对子类的静态成员调用，也就使其自己初始化；
			6. JVM启动时被标明为启动类的类；
			7. 当一个静态变量是一个`final`修辞的常量时，如果编译阶段就能算出其值，比如：
					class Test{
						public static final int a=2*4;
						public static final int b = (int)Math.random()*10;//b这个常量需要通过其他方法计算得出，因此不会像a一样在编译阶段就将其值算出来
					}
				1. `a`就是编译时常量，对于其引用变量`a`的方法来说，其字节码流中不是有一个相应的符号引用指向这个变量`Test.a`，而是一个常量值`6`（作为一个常量，反正也改变不了，直接让代码在编译阶段可以算出的值直接算出来不是更合理吗）。也正因为如此，其他类在初始化过程中调用`Test.a`这个变量时，并不会唤起对Test类的初始化，如果是`Test,b`就会；
			2. 而通过子类来调用父类的静态变量时，初始化并不会涉及到子类，因为这个变量是父类中定义；
4. **实例化**，如果要用类生成对象，则有**实例化**这一步。也就是说类成为对象之前必须先加载；
2. **卸载**
	1. 类的卸载只取决于类的Class对象结束生命周期。
	2. JVM自带的类加载器（根类加载器、扩展加载器、系统加载器）在JVM的生命周期中会一直被JVM所引用，而不会被卸载；
	3. 只有自定义的类加载器所加载的类才可以被卸载；
	4. JVM中的class要被卸载还得满足三个条件：
		1. 该类的所有实例对象被GC（有一个实例对象就意味着这个对象调用一些方法时会通过该类的class对象调用方法区的二进制数据结构，这个calss对象就是指向类在方法区的数据结构的接口）；
		2. 该类的class对象没有被引用；
		3. 该类的类加载器被GC；
	4. 类加载器同样会在堆区有一个**类加载器对象**，这个对象与类的class对象**双向关联**。而类实例对象与class对象单向关联；


----------
**Note：***整个类的生命周期的笔记总结中，对JVM的机制的了解增加了不少，特别是类在加载后，从.class二进制文件加载到内存中，再从内存被合并到JVM运行时环境中。这整个从内存到方法区的过程，是对类与类实例关系理解的重点。*


1. 更多的总结需要在后面补上：
	1. 其中包括：
		1. 类加载器
		2. 对象的生命周期；
		3. 垃圾回收机制；
		4. 值传递与址传递；