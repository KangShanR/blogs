---
title: spring bean的理解
date: 2017-08-23 02:04:38
tags: [framework,java,programming]
categories: programming

---

# bean in spring

> **前言：**
> Spring 中的 bean 配置就是将各个类配置在 bean.xml 文件中，成为一个个的组件，方便实现各个组件之间的重新装配，这也是实现 spring 的依赖注入的方便法门；
>
> 因此就可以理解，一个个的 bean 就是一个个的类的实例，但在 spring 运行时，spring 容器装配各个组件时初始化这些类实例时，也就会涉及到类的构造函数，装配各个组件时会涉及到各种类型参数；
>
> Spring中的配置各个 bean 时有许多不曾注意到的小知识点，这儿一并给总结出来。

<!--more-->

## 知识点

1. Spring 容器初始化各个 bean 组件时，默认组件为 **单态模式**（singleton，也叫单例模式）也就是当这个类只有一个实例，如果要实现非单态（prototype，标准类型），则将这个bean的 `singleton` 属性设置为 `false` ；
2. **构造函数** 的参数的配置，使用 `<constructor-arg>` 标签，多个参数就使用多个此标签，且要保证各个参数的顺序要与构造函数的参数顺序保持一致；
3. bean 的属性的配置的前提是这个类中相关的属性要有 `setter` 方法；
4. 在bean中配置属性使用 `<property>` 标签，给其赋值时可以后直接使用 `value` 属性也可以使用子元素 `<value>` 标签；
5. **空字符** 的设置： `<value></value>` 设置的是空字符串 `""` ，如果要设置为 `null` ，要使用 `<null/>` 或者干脆不设置;
6. **匿名对象的配置** ，类似于 java 中的匿名对象，如果要在一个属性中配置一个未曾配置的对象（也就是这个对象只会被使用一次的情况下，如果专门给其装配一个 bean 组件会造成一定的内存浪费），则其配置时直接将该类的路径写在 `<property>` 标签中，而 **不在其中引入需要提前配置好的 bean 的 id**
		<property name="dao">
			//使用匿名对象
			<bean class="com.snail.springdemo.dao.impl.UserDaoImpl"></bean>
		</property>
7. ref 与 idref 之间的区别：
	1. 两者都是用来设置 bean 的注入对象的；
	2. 两者检查其引用对象bean是否存在：**ref** 只有在第一次调用时会检查，也就是在 **程序运行中才会抛出错误** ，而 **idref 在程序启动时就会抛出错误** ；
	3. **idref只有bean、local属性，没有parent属性** ，而 ref 三个属性都有。（ local 表示就在当前配置文件中查找相关的 id，而 parent 表示在父配置文件中找。而 bean 则不会限制，可以在其本身找也可以在其父配置文件中找）；
8. depends-on，设置依赖对象：
	1. 当我们设置的 bean 实例 a 之前要确保另外一个 bean 实例先实例化，这时就可以使用 `depends-on` 属性：
			<bean id="a" class="com.snail.springdemo.A" depends-on="b"></bean>
			<bean id="b" class="com.snail.springdemo.B"></bean>

9. **初始化方法的执行：** 当我们想要一个 bean 在实例化过程中执行一些初始化方法，同时这些初始化执行过程不能放在构造函数中，这是就可以借助初始化方法的属性 `init-method` 在配置中来达到执行初始化的目标；
	```
	<bean id="test" class="com.kk.springdemo.A" init-method="initMethodName"></bean>
	```

## spring bean 的自动装配

> 上述情况每个 bean 的装配都由我们自己来在 xml 文件中通过 ref 属性来显式指定。但 spring 中有更为方便的方法：自动装配。
> - 在 `<beans>` 中，指定自动装配的属性 `default-autowire` ，对整个 beans 中的 bean 都生效。
- `<bean>` 中，指定自动装配的属性 `autowire` ，此属性只对当前 bean 生效。

`deault-autowire` 与 `autowire` 可以接受的值与其意义：
- `no` 不使用自动装配。这个时候的 bean 的属性都得使用 ref 指定依赖。 **默认值** ，较大的部署环境中都这样，显式地指定出来方便后期检索。
- `byName` 此种装配方法是 spring 会在 bean 库中去查找 bean 的 id 属性与当前需要装配的 setter 方法名（会将 setter 方法前面 `set` 去掉，并小写首字母）
- `byType` 查找 setter 方法中形参的类型与 bean 库中的类型进行匹配。如果找到多个 bean 将会拋出异常，如果没找到不会发生动作
- `constructor`  匹配 bean 的构造器与 setter 方法形参的构造器是否相同。同样如果找不到会拋出异常。
- `autodetect` spring 根据 bean 的内部结构自行决定采用 byType 策略还是用 constructor 策略。

_当一个 Bean 既使用自动装配依赖，又使用 ref 显式指定依赖时，则**显式指定的依赖覆盖自动装配依赖**；对于大型的应用，不鼓励使用自动装配。虽然使用自动装配可减少配置文件的工作量，但大大降低了依赖关系的清晰性和透明性。依赖关系的装配依赖于源文件的属性名和属性类型，导致Bean与Bean之间的耦合降低到代码层次，不利于高层次解耦。_

- 通过设置可以将 Bean 排除在自动装配之外

```
<!--通过设置可以将Bean排除在自动装配之外-->
<bean id="" autowire-candidate="false"/>

<!--除此之外，还可以在beans元素中指定，支持模式字符串，如下所有以abc结尾的Bean都被排除在自动装配之外-->
<beans default-autowire-candidates="*abc"/>
```

## 创建 bean 的方式

> 共 3 种

创建 bean 的三种方式：
1.
