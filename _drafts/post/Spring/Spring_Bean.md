---
layout: post
title: Spring bean
date: 2017-08-23 02:04:38
tags: [Spring, Java]
categories: Spring
---

# 1. Spring Bean

<!-- TOC -->

- [1. Spring Bean](#1-spring-bean)
  - [1.1. 知识点](#11-知识点)
  - [1.2. spring bean 的自动装配](#12-spring-bean-的自动装配)
  - [1.3. 创建 bean 的方式](#13-创建-bean-的方式)
  - [1.4. Bean Scope](#14-bean-scope)
    - [1.4.1. Web application bean scope](#141-web-application-bean-scope)
    - [1.4.2. 协调作用域不同的 bean](#142-协调作用域不同的-bean)
  - [1.5. Dependencies](#15-dependencies)
    - [1.5.1. Dependency injection](#151-dependency-injection)
      - [1.5.1.1. constructor injection](#1511-constructor-injection)
      - [1.5.1.2. setter injection](#1512-setter-injection)
      - [1.5.1.3. 依赖解析](#1513-依赖解析)
        - [1.5.1.3.1. 循环依赖](#15131-循环依赖)
        - [1.5.1.3.2. spring 依赖加载特性](#15132-spring-依赖加载特性)
    - [1.5.2. Depends On](#152-depends-on)
    - [1.5.3. lazy-initialized beans](#153-lazy-initialized-beans)
    - [1.5.4. AutoWiring Collaborators](#154-autowiring-collaborators)
      - [1.5.4.1. 使用自动装配的不足](#1541-使用自动装配的不足)
  - [1.6. 自定义 bean 特性](#16-自定义-bean-特性)
    - [1.6.1. 指定回调方法](#161-指定回调方法)
    - [1.6.2. Shutting Down the Spring IoC Container Gracefully in Non-Web Applications](#162-shutting-down-the-spring-ioc-container-gracefully-in-non-web-applications)
    - [1.6.3. ApplicationContextAware and BeanNameAware](#163-applicationcontextaware-and-beannameaware)
      - [1.6.3.1. ApplicationContextAware](#1631-applicationcontextaware)
      - [1.6.3.2. BeanNameAware](#1632-beannameaware)
  - [Bean Definition Inheritance](#bean-definition-inheritance)
  - [1.8. spring bean 零配置支持](#18-spring-bean-零配置支持)
    - [1.8.1. 自动装配与精确装配 spring 4.0](#181-自动装配与精确装配-spring-40)
      - [1.8.1.1. 自动装配微调](#1811-自动装配微调)
    - [1.8.2. @Resource 匹配](#182-resource-匹配)
    - [1.8.3. @Value 注入配置数据](#183-value-注入配置数据)
    - [1.8.4. 使用注解来定制 bean 方法成员的生命周期](#184-使用注解来定制-bean-方法成员的生命周期)
  - [1.9. Classpath Scanning and Managed Components](#19-classpath-scanning-and-managed-components)
    - [1.9.1. 自动检测 class 并注册 Bean Definition](#191-自动检测-class-并注册-bean-definition)
    - [1.9.2. Class Scanning Filter](#192-class-scanning-filter)
      - [1.9.2.1. Filter 类型](#1921-filter-类型)
  - [1.10. spring 容器中的 bean 实现不同方法](#110-spring-容器中的-bean-实现不同方法)
    - [1.10.1. @Bean Annotation](#1101-bean-annotation)
  - [1.11. Naming Bean](#111-naming-bean)
    - [1.11.1. Aliasing Bean](#1111-aliasing-bean)

<!-- /TOC -->

> **前言：**
> Spring 中的 bean 配置就是将各个类配置在 bean.xml 文件中，成为一个个的组件，方便实现各个组件之间的重新装配，这也是实现 spring 的依赖注入的方便法门；
>
> 因此就可以理解，一个个的 bean 就是一个个的类的实例，但在 spring 运行时，spring 容器装配各个组件时初始化这些类实例时，也就会涉及到类的构造函数，装配各个组件时会涉及到各种类型参数；
>
> Spring中的配置各个 bean 时有许多不曾注意到的小知识点，这儿一并给总结出来。

<!--more-->

## 1.1. 知识点

1. Spring 容器初始化各个 bean 组件时，默认组件为 **单态模式**（singleton，也叫单例模式）也就是当这个类只有一个实例，如果要实现非单态（prototype，标准类型），则将这个 bean 的 `singleton` 属性设置为 `false` ；
2. **构造函数** 的参数的配置，使用 `<constructor-arg>` 标签，多个参数就使用多个此标签，且要保证各个参数的顺序要与构造函数的参数顺序保持一致；
3. bean 的属性的配置的前提是这个类中相关的属性要有 `setter` 方法；
4. 在 bean 中配置属性使用 `<property>` 标签，给其赋值时可以后直接使用 `value` 属性也可以使用子元素 `<value>` 标签；
5. **空字符** 的设置： `<value></value>` 设置的是空字符串 `""` ，如果要设置为 `null` ，要使用 `<null/>` 或者干脆不设置;
6. **匿名对象的配置** ，类似于 java 中的匿名对象，如果要在一个属性中配置一个未曾配置的对象（也就是这个对象只会被使用一次的情况下，如果专门给其装配一个 bean 组件会造成一定的内存浪费），则其配置时直接将该类的路径写在 `<property>` 标签中，而 **不在其中引入需要提前配置好的 bean 的 id**

```xml
<property name="dao">
    //使用匿名对象
    <bean class="com.snail.springdemo.dao.impl.UserDaoImpl"></bean>
</property>
```

- ref 与 idref 之间的区别：
    1. 两者都是用来设置 bean 的注入对象的；
    2. 两者检查其引用对象bean是否存在：**ref** 只有在第一次调用时会检查，也就是在 **程序运行中才会抛出错误** ，而 **idref 在程序启动时就会抛出错误** ；
    3. **idref只有bean、local属性，没有parent属性** ，而 ref 三个属性都有。（ local 表示就在当前配置文件中查找相关的 id，而 parent 表示在父配置文件中找。而 bean 则不会限制，可以在其本身找也可以在其父配置文件中找）；
- depends-on，设置依赖对象：
    1. 当我们设置的 bean 实例 a 之前要确保另外一个 bean 实例先实例化，这时就可以使用 `depends-on` 属性：

    ```xml
    <bean id="a" class="com.snail.springdemo.A" depends-on="b"></bean>
    <bean id="b" class="com.snail.springdemo.B"></bean>
    ```

- **初始化方法的执行：** 当我们想要一个 bean 在实例化过程中执行一些初始化方法，同时这些初始化执行过程不能放在构造函数中，这是就可以借助初始化方法的属性 `init-method` 在配置中来达到执行初始化的目标；

```xml
<bean id="test" class="com.kk.springdemo.A" init-method="initMethodName"></bean>
```

## 1.2. spring bean 的自动装配

> 上述情况每个 bean 的装配都由我们自己来在 xml 文件中通过 ref 属性来显式指定。但 spring 中有更为方便的方法：自动装配。

- 在 `<beans>` 中，指定自动装配的属性 `default-autowire` ，对整个 beans 中的 bean 都生效。
- `<bean>` 中，指定自动装配的属性 `autowire` ，此属性只对当前 bean 生效。

`deault-autowire` 与 `autowire` 可以接受的值与其意义：

- `no` 不使用自动装配。这个时候的 bean 的属性都得使用 ref 指定依赖。 **默认值** ，较大的部署环境中都这样，显式地指定出来方便后期检索。
- `byName` 此种装配方法是 spring 会在 bean 库中去查找 bean 的 id 属性与当前需要装配的 setter 方法名（会将 setter 方法前面 `set` 去掉，并小写首字母）
- `byType` 查找 setter 方法中形参的类型与 bean 库中的类型进行匹配。如果找到多个 bean 将会拋出异常，如果没找到不会发生动作
- `constructor`  匹配 bean 的构造器与 setter 方法形参的构造器是否相同。同样如果找不到会拋出异常。
- `autodetect` spring 根据 bean 的内部结构自行决定采用 byType 策略还是用 constructor 策略。

_当一个 Bean 既使用自动装配依赖，又使用 ref 显式指定依赖时，则**显式指定的依赖覆盖自动装配依赖**；对于大型的应用，不鼓励使用自动装配。虽然使用自动装配可减少配置文件的工作量，但大大降低了依赖关系的清晰性和透明性。依赖关系的装配依赖于源文件的属性名和属性类型，导致Bean与Bean之间的耦合降低到代码层次，不利于高层次解耦。_

- 通过设置可以将 Bean 排除在自动装配之外

```xml
<!--通过设置可以将Bean排除在自动装配之外-->
<bean id="" autowire-candidate="false"/>
<!--除此之外，还可以在beans元素中指定，支持模式字符串，如下所有以abc结尾的Bean都被排除在自动装配之外-->
<beans default-autowire-candidates="*abc"/>
```

_一个模块的 spring 配置文件根节点就是 `<beans>` ，也就是用这个节点来配置了一个 bean 池，再在这个里面配置了各个属性，也就是在这其中配置了各个 bean 与池的其他属性。_

## 1.3. 创建 bean 的方式

> 共 3 种

创建 bean 的三种方式：

1. 构造器创建 bean ，最常见的创建方式。 如果不采用构造注入， spring 会自动加载此 bean 的默认无参构造器，并将其属性全部初始化（基本类型设置为 0/false，引用类型设为 null）
2. 静态工厂方法创建 bean 。使用静态工厂创建 bean 时必须指定 `<bean class="">` 这儿的 class 属性就是用来指定静态工厂， factory-method 属性指定工厂的创建方法， 如果此方法需要参数，通过 constructor-arg 属性来指定。
3. 实例工厂方法创建 bean 。顾名思义，此方法与 静态工厂方法 的不同之处在于使用工厂实例进行创建 bean 。所以这儿能过 factory-bean 来指定工厂实例，再通过 factory-method 指定创建 bean 的方法。如果需要参数通过 constructor-arg 指定参数值。

## 1.4. Bean Scope

Bean scope : bean 领域，指 bean 的生存策略，共 6 种，其中 4 种只存在于 web 应用 context 中。

1. singleton，spring bean 默认的单例，但 spring bean scope 单例与设计模式的单例不同。设计模式中单例是对一个特定 java 类来说的，每个 classloader 只生产一个实例。而 spring bean scope 是指一个 bean 在同一个 IoC 容器只生产一个实例。
2. prototype, 模版模式，每次请求此 bean 被注入其他 bean 中或通过 `getBean()` 方式获取容器中 bean 时都会创建一个实例。按惯例， prototype scope 用于带状态的 bean ，而 singleton scope 用于无状态 bean 。
    1. 对于 prototype scope 的 bean ， IoC 容器只负责其初始化、装配、交给需要此 bean 的客户端，并不负责其后的生命周期。所以对一个 prototype scope bean 就算配置了生命周期中 destruction 销毁的回调 IoC 也不会执行，而负责此任务的是 client。
    2. client 可使用 bean post-processor 对 bean 进行资源管理。在某些方面来讲，IoC 容器对于 prototype scope bean 相当于一个 java new operator。在此之后的生命周期管理都交给了 client。
3. 当一个 singleton scope beanA 中依赖注入了 prototype scope beanB ，同时，在 beanA 中需要 beanB 的多个不同的实例。IoC 容器在初始化 beanB 时只会在 beanA 中初始化一个 beanB 的实例，当 IoC 容器按顺序给 beanA 装配时只会装配同一个 beanB 实例到 beanA 中。[reference](https://spring.io/blog/2004/08/06/method-injection/)
   1. 解决问题的方案一：放弃 IoC ，让 beanA 实现 `ApplicationContextAware` 接口让其对 IoC 容器敏感，每个需要 bean 的地方使用 `ApplicationContext.getBean(Bean.class)` 的方式获取，这样获取的 bean 就是一个新的实例。缺点：业务代码与 spring 框架耦合在一起。
   2. 方案二：IoC 容器方法注入。

    ```xml
    <bean id="commandManager" class="fiona.apple.CommandManager">
        <lookup-method name="createCommand" bean="myCommand"/>
    </bean>
    ```

    或使用 annotation `@Lookup(value="")`
    3. 方法可以是抽象方法也可是具体方法，IoC 容器会通过 CGLIB 为方法所在的类生成子类覆盖方法，所以 `@Lookup` 只能在 IoC 容器能通过常规构造器初始化的 bean 中才能生效。也就是：Lookup 不能为工厂方法生产 bean 方法所替代，因为不能动态地为工厂方法所生产的 bean 提供子类。method 与 class 均不能为 final 修辞。
    4. 在 spring 使用场景中需要注意：需要为 Lookup 方法提供具体实现，否则 component scanning 之类会过滤掉抽象 bean。同时， Lookup method 不能在 configuration class 中配置的 `@Bean` 方法上生效，需要使用 `@Inject` 之类的注解代替。

### 1.4.1. Web application bean scope

 request/session/application/websocket scope 都用于 web application context，如果是一个普通的应用程序，使胳膊这几个 scope 会抛出 IllegalStateException。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-sing-prot-interaction)

初始化一个 web application configuration:

1. 如果使用 spring mvc scoped this beans，只需要注册一个 `DispatherServlet` 在 web 配置中即可。
2. 在初始化一个 web configuration 时，当使用的 Servlet2.5 web 容器，且请求非 spring mvc （struts 、 jsf 之类），需要注册 `org.springframework.web.context.request.RequestContextListener` 到 web 配置中，如果使用 servlet3.0 使用 `WebApplicationInitializer` 接口将自动完成以上注册。
3. 如果使用 listener 还有问题，可注册 `org.springframework.web.filter.RequestContextFilter` 到 web 中。
4. 前面的 servlet/listener/filter 的目标只有一个：将 HTTP request 对象按名绑定到服务此请求的线程上。这就让请求域与会话域的 bean 在调用链更下游可用。

四个 web bean scope

1. request scope
   1. 在 xml 配置中： `<bean id="loginAction" class="com.something.LoginAction" scope="request"/>`
   2. java configuration: `@RequestScope` 在请求类上注解
   3. 效果：每次请求调用此 bean 将会产生一个新的 bean 实例来处理此次请求，请求与请求之间不互扰。当此次请求完成，bean 被废弃。
2. session scope
   1. xml 配置方式: `<bean id="userPreferences" class="com.something.UserPreferences" scope="session"/>`
   2. java 配置方式: `@SessionScope`
   3. 效果：bean 实例的产生取决于一个 HTTP session 的生命周期，在同一个 HTTP 会话中，此 bean 实例都是有效的。只有当此次 HTTP 会话结束，bean 才会被废弃。所以在同一次会话中，不同的请求的状态变化将会相互影响。
3. application scope
   1. xml 配置方式：`<bean id="appPreferences" class="com.something.AppPreferences" scope="application"/>`
   2. java 配置方式： `@ApplicationScope`
   3. 效果：整个 web 应用只生产一个 application scope 的 bean 。bean 域被划分到 ServletContext 级别，并且被存储为一个常规的 ServletContext 属性。类似于 spring 的 singleton scope，但有两点不同：
      1. application scope  是每个 servlet 生产一个实例，而 spring 的 singleton scope 是每个 ApplicationContext 生产一个实例（一个应用中可能有多个 ApplicationContext）。
      2. application scope bean 实际上是显露在外的，在 ServletContext 中属性可见。
4. 依赖域的 bean[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes-custom)
   1. 当需要将一个短生命周期的 beanA(session scope) 注入一个相对长生命周期的 beanB(singleton scope) 中时，会出现 beanA 已经被丢弃而 beanB 依然去调用 beanA。
   2. 添加 AOP 代理配置到 beanA ，代理会将短生命周期的 beanA 序列化存储起来，在 beanB 需要调用时实际上调用代理，代理去查找需要实际调用的实例，找不到则反序列化成为对象 beanA 再次调用此 beanA。
   3. 如果代理的对象是 ptototype scope，则代理每次调用时将产生一个新的 beanA 实例供调用。
   4. 代理 scope 并非唯一的在长域 bean 访问短域 bean 的方式，也可定义注入点（构造器、setter argument、autowired field）为 `ObjectFactory<MyBean>` ，通过调用其 `getObject()` 获取新的实例 bean。

### 1.4.2. 协调作用域不同的 bean

> 当 singleton bean 依赖于 prototype bean 时，会因为 spring 窗口初始化时会先预初始化 singleton bean ，如果  singleton bean 依赖于 prototype bean ，就不得不先将依赖的 prototype bean 初始化好，再注入到 singleton bean。这就带来一个不同步的问题（多个 singleton bean 依赖了同一个 prototype bean）。

解决不同步的方法：

1. 放弃依赖注入。每次需要 prototype bean 时都向窗口请求新的 bean 实例，这样每次都会产生新的 bean 实例（但是作为 prototype bean 的确是每次都产生新的实例，这里没有搞懂）。
2. 利用方法注入。通常使用 `lookup` 方法注入，此方法会让 spring 容器重写容器中的 bean 的抽象或具体方法，返回查找容器中其他 bean 的结果，被查找的 bean 通常是一个 non-singleton bean 。 spring 通过使用 JDK 动态代理或者 cglib 库修改客户端代码实现上述动作。
   1. 为了使用lookup方法注入，大致需要如下两步：
        1. 将调用者Bean的实现类定义为抽象类，并定义一个抽象方法来获取被依赖的Bean。
        2. 在 `<bean.../>` 元素中添加 `<lookup-method.../>` 子元素让 Spring 为调用者 Bean 的实现类实现指定的抽象方法。

_Spring会采用运行时动态增强的方式来实现 `<lookup-method.../>`元素所指定的抽象方法，如果目标抽象类实现过接口，Spring 会采用 JDK 动态代理来实现该抽象类，并为之实现抽象方法；如果目标抽象类没有实现过接口，Spring会采用cglib实现该抽象类，并为之实现抽象方法。Spring4.0 的 spring-core-xxx.jar 包中已经集成了 cglib 类库。_

## 1.5. Dependencies

spring IoC 容器中各个 bean 相互依赖。

### 1.5.1. Dependency injection

依赖注入

依赖注入的方法主要有两种：构造器注入、工厂方法注入、setter 注入

区别：constructor 与工厂方法注入在初始化就注入，而 setter 注入在初始化后注入依赖。

#### 1.5.1.1. constructor injection

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-constructor-injection)

1. 构造器注入与工厂方法注入类似，都在本 bean 初始化时将依赖注入。
2. 当构造参数或工厂方法参数存在继承关系时，参数匹配会模糊不清。解决方案：
   1. 添加参数 index （从 0 开始）；
   2. 指定参数类型：

   ```xml
   <bean id="exampleBean" class="examples.ExampleBean">
        <constructor-arg type="int" value="7500000"/>
        <constructor-arg type="java.lang.String" value="42"/>
    </bean>
   ```

   1. 指定参数名，同时需要在方法上添加 `@ConstructorProperties({"years", "ultimateAnswer"})`；

#### 1.5.1.2. setter injection

在 bean 实例化后调用，同一个 bean 的依赖注入两种方式都可使用。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-setter-injection)

#### 1.5.1.3. 依赖解析

1. `ApplicationContext` 通过配置元数据创建并初始化，配置数据可以通过 xml/Java code/annotations 完成。
2. 每个 bean 的依赖表现为 bean 的属性、构造器参数、工厂方法参数形式，当 bean 被创建时，这些依赖已准备好。
3. 对于 bean 来说，每个依赖等同于被设置的值或 IoC 容器中其他 bean 引用。
4. bean 的每个属性实际上是从指定的格式转换为其需要的类型。spring 可以将一个 String 类型的值转换为内置的类型（如：int boolean long）。
5. 默认 spring IoC 容器在创建时会将 bean scope 域定为 singleton，且会预先初始化 bean ，否则只有在请求时才会被创建。
6. bean 的创建可能会形成图形结构：bean 的依赖的创建及其依赖的依赖的创建。

##### 1.5.1.3.1. 循环依赖

bean 之间相互 constructor 依赖。beanA 依赖了 beanB ，同时 beanB 依赖了 beanA，且两者的依赖都是通过构造器依赖。当出现循环依赖时，IoC 在运行时会抛出 `BeanCurrentlyInCreationException`。

解决方案：配置其中一个（或全部） bean 使用 setter 注入。虽然不推荐，但可以使用 setter 注入配置循环依赖。

循环依赖与正常依赖不同之处：循环依赖其中一个 bean 强制在完全初始化前注入另一个 bean 。_IoC container 来做的？_

##### 1.5.1.3.2. spring 依赖加载特性

1. spring 在容器加载时会自动检测配置的潜在问题，诸如：引用缺失、循环依赖；
2. spring 实际创建 bean 时会尽晚地设置属性和解析依赖（在未使用某个依赖前并不注入此依赖），这意味着在 spring container 正确加载后请求对象会出现创建对象或其依赖的异常，比如：bean throws a exception of  a missing of invalid property。为此，`ApplicationContext` 的实现默认预先初始化 singleton scope beans。用预先的时间与内存消耗来初始化 bean 在 `ApplicationContext` 创建时显露出配置的问题。

### 1.5.2. Depends On

使用 `depends-on` 属性决定本 bean 的初始化依赖于其他的 bean，Spring 会在本 bean 初始化前完成依赖的 bean 的初始化，同时在销毁依赖的 bean 前先销毁本 bean 。

### 1.5.3. lazy-initialized beans

指定懒加载 bean 。

- 使用 `lazy-init=true` 指定 bean 懒加载。
- 使用懒加载的 bean 即使是 singleton scope 也不会在 container 初始化时预先初始化此 bean，而是在第一次请求到此 bean 时才会初始化。
- 当一个懒加载的 bean 是一个非懒加载 singleton scope bean 的依赖时，此 bean 一样会因为需要预先实例化其他 bean 而被实例化用以装配。
- 设置容器全局懒加载 `<beans default-lazy-init="true">`。

### 1.5.4. AutoWiring Collaborators

设置自动装配 bean 。

- 在 `<bean/>` 配置中添加 `autowire` 属性使用自动装配。
- 自动装配有 4 种模式：
    - `no` ，默认模式，不使用自动装配，在大型系统中不推荐覆盖此配置。显式地指定各个 bean 更利于清晰控制系统。
    - `byName` ，通过 bean property 名自动装配，spring 在容器中查找与 property（此 property 必须有其 setter） 名相同的 bean 用以装配。
    - `byType`，通过 porperty 类型自动装配，spring 在容器中查找类型与其一致的 bean 进行装配，如果未找到，此 property 将不会被装配，如果找到多个将抛出异常。
    - `constructor`，与 `byType` 类似，不过只应用于 constructor 参数。如果没有类型一致的 bean，将抛出异常。
- `byType` 和 `constructor` 可以装配一个类型匹配的数组或集合。这种情况下，容器中所有类型匹配的 bean 都会被装配在其中，如果使用 map 来接收，其 key 就是 bean name 。

#### 1.5.4.1. 使用自动装配的不足

1. 显式地指定装配会自动装配，同时自动装配不能装配一个基本类型数据、String、Class 和这些类型的数组；
2. 自动装配相对显式装配指代不够清晰；
3. 当容器中有多个符合条件的 bean 而装配处只需要单个时，会抛出错误。

解决方案：

1. 放弃使用自动装配，使用显式装配；
2. 将被装配 bean `autowire-candidate` 设为 `false`（此属性只对 `byType` 自动装配有效，如果装配处指定自动装配为 `byName` 一样可以自动装配到此 bean）；
3. 在多个符合条件的 bean 中挑选一个作为主候选 bean ，指定其 `<bean/>` 中属性 `primary=true`；
4. 使用更细粒度控制的注解配置。

## 1.6. 自定义 bean 特性

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-nature)

- 使用 `@PostConstruct` 与 `@PreDestroy` 代替实现 `InitializingBean` 与 `DisposableBean` 接口，代码与 spring 解耦；
- 指定 `<bean/>` 属性 `destroy-method=inferred`，可使 spring 自动推断 bean 销毁前执行公共回调方法，如：`close()/shutdown()`。同理在 `<beans/>` 中设定属性 `default-destroy-method=inferred` 可指定所有的 bean 的销毁前回调方法；
- 可在 `<beans/>` 中添加属性 `default-init-method="init"` 指定默认的初始化回调方法名，让配置中所有的 bean 都保持一致调用名此回调；

### 1.6.1. 指定回调方法

在 spring 2.5 后，指定回调方法有 3 种

1. 实现 `InitializingBean` `DisposableBean` 回调接口；
2. 自定义的 `init()` `close()` 方法；
3. 使用 `@PostConstruct` `@PreDestroy` 注解在方法上。

当 bean 有多个生命周期回调且方法不一样时，回调都将被执行，其顺序是 : 3 -> 1 -> 2

**Note:** 回调方法执行是在当前对象的依赖都准备好之后，但在代理、拦截器这些机制应用之前，所以 init() 前置回调如果需要访问代理、拦截器之类是做不到的。

### 1.6.2. Shutting Down the Spring IoC Container Gracefully in Non-Web Applications

[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-shutdown)

在 web 工程中， ApplicationContext 的实现代码会在工程关闭时正确地关闭 Spring IoC 容器。而在一个非 web 工程中需要手动地将 Spring IoC 容器关闭注册到 JVM ，以保证在关闭时能释放 singleton 资源。

```java
public final class Boot {

    public static void main(final String[] args) throws Exception {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

        // add a shutdown hook for the above context...
        ctx.registerShutdownHook();

        // app runs here...

        // main method exits, hook is called prior to the app shutting down...
    }
}
```

### 1.6.3. ApplicationContextAware and BeanNameAware

[reference](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-factory-lifecycle-processor)

#### 1.6.3.1. ApplicationContextAware

实现 ApplicationContextAware 接口获取 ApplicationContext 。以获得操纵 ApplicationContext 的方法。但这样会让业务代码与 Spring 耦合。

可以使用 Spring AutoWiring 特性，自动注入 ApplicationContext 。

#### 1.6.3.2. BeanNameAware

实现此接口的 bean 会提供一个定义 bean name 的方法，在 bean properties 设置之后且在其初始化回调（`InitializingBean` `afterPorpertiesSet` 或自定义初始化方法）执行之前会执行此方法。

```java
public interface BeanNameAware {
    void setBeanName(String name) throws BeansException;
}
```

Note: BeanNameAware 回调执行是在 bean 基础属性配置好之后，在初始化回调 自定义 init 方法/afterPropertiesSet/InitializingBean 执行之前。

## Bean Definition Inheritance

[reference](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-child-bean-definitions)

> Bean Definition 继承

- xml bean 配置中添加 abstract=true　属性就指定此 bean definition 为抽象，抽象的可以不指定 class
- `parent=beanId` 指定当前 bean definition 继承目标 bean 。被继承的 bean 可以是抽象的。
- 抽象的 bean definition 不能被初始化。ApplicationContext 默认会预初始化所有的 singleton，因此所有想被当作模版用 parent bean definition 在指定了class 后一定要指定其为 abstract=true，否则 application context 会对其进行初始化。

## 1.8. spring bean 零配置支持

> spring 零配置是指通过**注解**来实现 beans.xml 中配置 spring bean 容器的功能
> 在 spring 配置文件中指定自动扫描的包： `<context:component-scan base-package="package.path.name"/>`

- 使用注解完成配置相对于 xml 配置更为精简，但也与源码耦合，修改配置需要重新编译。通常同一个项目中混合使用两种配置方式。
- xml 配置中兼容注解配置，使用 `<context:annotation-config/>`
    - 此配置隐匿地注册了很多 post-processor 包括： `AutowiredAnnotationBeanPostProcessor, CommonAnnotationBeanPostProcessor, PersistenceAnnotationBeanPostProcessor, and the aforementioned RequiredAnnotationBeanPostProcessor`
    - 此配置只查询同一级别的应用上下文的注解，所以如果只是在 DispatcherServlet 的 `WebApplicationContext` 配置，那么就只会扫描到 Controller 而不会扫描到 Service 的注解。

### 1.8.1. 自动装配与精确装配 spring 4.0

`@Autowired` 指定自动装配

- 可以用来修辞 setter方法/普通方法/实例变量/构造器
- 使用的是 byType 策略从 spring 容器中查找 bean ，如果容器中有多个同一类型的 bean 就会拋出异常。同时确保所需要的至少有一个是 declared by type ，否则抛出 "no type match found"
- 为解决上述问题，spring 4.0 就增加了 `@Qualifier` 注解
    - `@Qualifier` 用于精确装配 bean ，其方法是在其中指定 bean id 。因此如果要使用此注解来装配，就得将被装配的 bean id（也就是 beanName，通常的标注注解就这一个属性，默认为 ""） 标注出来。
- 对于 xml 配置与 classpath 扫描，容器能知道具体的类型。但对于工厂方法的 `@Bean` 需要保证返回类型足够明确，特别是对于实现多个接口的 bean ，最好返回 bean 的最具体的类型（至少指定到需要的 bean 类型）。
- 注解在 bean 集合上
    - 如果是有序集合（数据、List），可以使用 `@Ordered` `@Priority` 注解标明其顺序，如果未标注，则其顺序为其在容器中注册的顺序。
    - 如果是 Map ，其 key 是 bean name (String)。
- `BeanFactory, ApplicationContext, Environment, ResourceLoader, ApplicationEventPublisher, and MessageSource` 这些 Spring 基础工具都是自动解析，直接使用 `@AutoWired` 即可。
- 可用于 Constructor 上标明此构造器用于生产 bean 用于自动装配。

#### 1.8.1.1. 自动装配微调

1. 使用 `@Primary` 指定众多实现 bean 中一个为主 bean，当自动装配时优先使用此 bean；
2. 使用 `@Qualifier` 指定修辞词，在 bean 定义上加入修辞词：`<qualifier value="main"/>` ，使用处加上注解 `@Qualifier("main")` 即指定相应的 bean 为需要的装配对象。
   1. bean name 是一个默认的后备 qualifier value，所以不用内嵌一个 qualifier 定义 bean，直接使用其 name/id 即可。
   2. `@AutoWired` 其根本上是类型匹配，附加了 Qualifier 语义匹配。所以不管是指定一个 Qualifier value 还是使用备选的 bean name qualifier，会窄化类型匹配的含义。一个好的 qualifier value 应该独立于其 bean id/name 定义其组件角色，诸如：`main` `EMEA` `persistent`。bean id 在匿名定义时会自动生成。
   3. 如果通过 bean name 查找 qualifier value 可以不用在注入点添加 `@Qualifier` ，Spring 在没有其他解析指示器（qualifier/primary）处，类型匹配到多个依赖的情况下，会自动匹配注入点名与 bean name 相同的 bean。
   4. 在使用自动装配加上 `@Qualifier` 注解或相关的 bean name 标记模式下，其查询机制是在 类型匹配的结果集上再进行 bean name 匹配。而 `@Resource` 注解是直接使用 bean id/name 匹配。
3. 如果注入本身定义为集合、数组类型的 bean，直接使用 `@Resource` 匹配其 bean id/name 。
4. 从 Spring 4.3 开始，bean 注入可以注入自身。自注入只是一个备选方案，常规的注入中其他的依赖的有更高的优先权。如果注入时有其他可选项，bean 本身不会被注入。可以使用 `@Resource` 来指定 bean id 来注入本身。实际编码中，如果出现需要在事务中同一个 bean 中调用其他方法，就得使用自注入实现 bean 代理（可选的方案还有将需要事务代理的方法抽取到另一个 bean 中）。
5. `@Resource` 如果注解在方法上，方法只能有一个参数。
6. 自定义 Qualifier ，自定义一个Qualifier 注解，其被 `@Qualifier` 注解，可为其加上属性，定义 bean 时，为其加上属性值，在注放处加上注解并指定其属性，自动装配时会主动匹配各属性一致的 bean。
7. 可使用范型约束来达到 qualifier 的效果。在 `@AutoWired` 注入依赖时，如果依赖实现的是一个范型接口，注入点就使用此范型类型作为 type 即可以注入此实现。
8. `@Qualifier` 定义 bean metadata 直接注解于 class 或 method 之上即可。

### 1.8.2. @Resource 匹配

> 位于 javax.anotation 包

- 使用 `@Resource` 配置一个 bean 类的依赖，用于指定协作 bean 。这就与 `<property ...>` 中的 ref 属性有相同的效果。
- 可以用于修改 setter 方法
- 还可以直接修辞 实例变量。这样使用更为简单，spring 将会使用 javaEE 规范的 field 注入，setter 方法都不用写了。
- @Resource 注解主要基于 bean name 匹配，同时 byType 类型匹配将作为一个备选策略，如果在容器中找不到指定的 bean name，将进行类型匹工配。
- 注解在 field 上与字段名匹配，注解在 setter 方法上与类的 property 名匹配。
- `BeanFactory, ApplicationContext, Environment, ResourceLoader, ApplicationEventPublisher, and MessageSource` 这些 Spring 基础工具都是自动解析，不用 bean define 直接使用 `@Resource` 可进入注入。

### 1.8.3. @Value 注入配置数据

`@Value` 注入外部属性。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-autowired-annotation)

1. 在配置上下文添加外部文件：`@PropertySource("classpath:application.properties")`
2. 需要注入外部属性数据的地方：`@Value("${upper_case}")` 会注入外部文件的值。
3. Spring 默认的解析器会解析此配置，如果未找到此属性名，那么会注入此注解的 value `${upper_case}`。
4. 如果需要更严格地控制外部数据注入，需要静态注入一个 bean : `PropertySourcesPlaceholderConfigurer`

    ```java
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
    ```

    1. 使用此配置 bean 作为解析器，在 Spring 上下文初始化时就需要保证各个配置点位符能被正确解析（ key 必须存在），否则会抛出错误。
    2. 此解析器可以添加配置 key 的前后缀。使用方法： `setPlaceholderPrefix()` `setPlaceholderSuffix`
    3. 可添加默认值：`@Value("${upper_case:true}")`
5. 内置的解析器提供了简单地类型转换，数据类型可以直接转换成相应类型。
   1. 可自定义数据转换器

        ```java
        @Bean
        public ConversionService conversionService() {
            DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
            conversionService.addConverter(new MyCustomConverter());
            return conversionService;
        }
        ```

6. Spring Boot 默认使用 `PropertySourcesPlaceholderConfigurer` ，其配置的外部文件为 `application.properties` `application.yml`

### 1.8.4. 使用注解来定制 bean 方法成员的生命周期

现个注解实现(javax.annotation 包)：

- `@PostConstruct` 顾名思义，是在 bean 构造之后执行，修辞的是 bean 的初始化方法；
- `@PreDestroy` 修辞 bean 销毁之前执行的方法

## 1.9. Classpath Scanning and Managed Components

配置元数据，Spring 容器会根据元配置数据生成 BeanDefinition，bean 的注入可以通过前面介绍的使用注解实现，但基本的 bean 定义还是使用的 xml 配置。Spring 3.0 开始引入 classpath scan，用以检测 Spring bean 组件。在扫描到的 classes 中，匹配到指定条件且有在容器中注册相应的 bean 定义。

- `@Component` 标注为一个基本组件类，所有的 bean 组件注解都继此元注解，Spring 组件管理的标版。
- `@Repository` 标注为持久层 DAO 组件类
- `@Service` 标注为一个 service 层业务逻辑组件类
- `@Controller` 标注为一个表现层控制器组件类

### 1.9.1. 自动检测 class 并注册 Bean Definition

- Spring 会自动检测各个标准版本 class 并注册相应的 BeanDefinition 实例（ApplicationContext 信息）。
- 需要在配置 `Configuration` 类上添加 `@ComponentScan` 注解，其中 `basePackages` 属性可以是基础包名也可以是多个 bean class（用 `,` `;` 或空格 ` ` 分隔）。

### 1.9.2. Class Scanning Filter

在 Spring Class 扫描中添加过滤器。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-value-annotations)

- Spring 默认的过滤效果是将 `@Component, @Repository, @Service, @Controller, @RestController, @Configuration` 6 个和自定义的注解 class 过滤出来。
- 在 `@ComponentScan` 中指定属性 `useDefaultFilters=false` 将取消默认扫描的类
- `includeFilters` 添加过滤器
- `excludeFilters` 拦截过滤器
  
#### 1.9.2.1. Filter 类型

1. annotation 默认，指定有某个注解的类为目标组件
2. assignable 指定某个类或接口为目标组件
3. aspectj AspectJ 类型表达式匹配目标组件
4. regex 正则表达式匹配目标组件的 bean name
5. custom 自定义实现 `org.springframework.core.type.TypeFilter` 过滤器

## 1.10. spring 容器中的 bean 实现不同方法

[参考](https://www.cnblogs.com/duanxz/p/7493276.html)

可以通过 java 配置类来实现 spring beans 的配置：

- @Configuration 等价于 `<Beans></Beans>`
- @Bean 等价于 `<Bean></Bean>`
- @ComponentScan 等价于 `<context:component-scan base-package="com.dxz.demo"/>`

[获取 xml applicationContext 方法参考](https://www.cnblogs.com/yjbjingcha/p/6752265.html)

### 1.10.1. @Bean Annotation

使用 `@Bean` 注册一个实例到 IoC 容器中。

- 当要配置一个需要构造函数构造的 bean 时，需要指定其构造函数的参数。[指定构造器参数 bean](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-constructor-injection)
- `@Bean` 是一个方法级别的注解，使用在方法之上，方法返回类型即 bean 实例类型。
- 推荐方法返回具体实例类型而不是接口类型。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-java-bean-annotation)
- 对一个 bean 指定生命周期回调
    - 在 `@Bean` 中指定 `initMethod` `destroyMethod` 两个 bean 方法名，用以决定 bean 在初始化后现销毁前的回调。
    - `destroyMethod` 默认为 `deferred` 推断模式，在容器销毁前自行推断其销毁方法，如果想在容器销毁时保留 bean ，可以指定 `destroyMethod=""`。

## 1.11. Naming Bean

bean 的命名[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-basics)

- 按惯例， bean 命名使用小驼峰。
- 命名规范保持一致，有助于读与理解。同时，有助于 AOP 通过名字查找 bean 进入增强。
- 对于 component scan ，Spring 为未命名的 componet 命名。取类的 simple name 小驼峰化为其名。特例：对于类名字母数量不只1个且前两个字符都是大写字母的情况， spring 会保留其原名。
- 指定多个名：可使用逗号 `,`，分号 `;`，空格 `` 加以分隔。

### 1.11.1. Aliasing Bean

给 bean 起别名。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-basics)

`@Bean`
当一个应用中存在多个子系统，系统之间需要使用同一个 bean
