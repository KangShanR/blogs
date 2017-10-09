---
title: spring bean的理解
date: 2017-08-23 02:04:38
tags: [framework,java,programming]
categories: programming

---

# Spring中的bean配置 #

> **前言：**
> Spring中的bean配置就是将各个类配置在bean.xml文件中，成为一个个的组件，方便实现各个组件之间的重新装配，这也是实现spring的依赖注入的方便法门；
> 
> 因此就可以理解，一个个的bean就是一个个的类的实例，但在spring运行时，spring容器装配各个组件时初始化这些类实例时，也就会涉及到类的构造函数，装配各个组件时会涉及到各种类型参数；
> 
> Spring中的配置各个bean时有许多不曾注意到的小知识点，这儿一并给总结出来。

<!--more-->

## 知识点: ##
1. Spring容器初始化各个bean组件时，默认组件为**单态模式**（singleton，也叫单例模式）也就是当这个类只有一个实例，如果要实现非单态（prototype，标准类型），则将这个bean的`singleton`属性设置为`false`；
2. **构造函数**的参数的配置，使用`<constructor-arg>`标签，多个参数就使用多个此标签，且要保证各个参数的顺序要与构造函数的参数顺序保持一致；
3. bean的属性的配置的前提是这个类中相关的属性要有`setter`方法；
4. 在bean中配置属性使用`<property>`标签，给其赋值时可以后直接使用`value`属性也可以使用子元素`<value>`标签；
5. **空字符**的设置：`<value></value>`设置的是空字符串`""`，如果要设置为`null`，要使用`<null/>`或者干脆不设置;
6. **匿名对象的配置**，类似于java中的匿名对象，如果要在一个属性中配置一个未曾配置的对象（也就是这个对象只会被使用一次的情况下，如果专门给其装配一个bean组件会造成一定的内存浪费），则其配置时直接将该类的路径写在`<property>`标签中，而不用在其中引入需要提前配置好的bean的id：
		<property name="dao">
			//使用匿名对象
			<bean class="com.snail.springdemo.dao.impl.UserDaoImpl"></bean>
		</property>
1. ref与idref之间的区别：
	1. 两者都是用来设置对象属性的；
	2. 两者检查其引用对象bean是否存在：**ref**只有在第一次调用时会检查，也就是在**程序运行中才会抛出错误**，而**idref在程序启动时就会抛出错误**；
	3. **idref只有bean、local属性，没有parent属性**，而ref三个属性都有。（local表示就在当前配置文件中查找相关的id，而parent表示在父配置文件中找。而bean则不会限制，可以在其本身找也可以在其父配置文件中找）；
4. depends-on，设置依赖对象：
	1. 当我们设置的bean实例a之前要确保另外一个一bean实例先实例化，这时就可以使用`depends-on`属性：
			<bean id="a" class="com.snail.springdemo.A" depends-on="b"></bean>
			<bean id="b" class="com.snail.springdemo.B"></bean>

1. ** 初始化方法的执行**：当我们想要一个bean在实例化过程中执行一些初始化方法，同时这些初始化执行过程不能放在构造函数中，这是就可以借助初始化方法的属性`init-method`在配置中来达到执行初始化的目标；
		<bean id="test" class="com.kk.springdemo.A" init-method="initMethodName"></bean>


