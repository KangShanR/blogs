---
title: spring bean的理解
date: 2017-08-23 02:04:38
tags: [framework,java,programming]
categories: programming

---

# 1. Spring Bean

<!-- TOC -->

- [1. Spring Bean](#1-spring-bean)
  - [1.1. 知识点](#11-%e7%9f%a5%e8%af%86%e7%82%b9)
  - [1.2. spring bean 的自动装配](#12-spring-bean-%e7%9a%84%e8%87%aa%e5%8a%a8%e8%a3%85%e9%85%8d)
  - [1.3. 创建 bean 的方式](#13-%e5%88%9b%e5%bb%ba-bean-%e7%9a%84%e6%96%b9%e5%bc%8f)
  - [1.4. 协调作用域不同的 bean](#14-%e5%8d%8f%e8%b0%83%e4%bd%9c%e7%94%a8%e5%9f%9f%e4%b8%8d%e5%90%8c%e7%9a%84-bean)
  - [1.5. spring 后处理器](#15-spring-%e5%90%8e%e5%a4%84%e7%90%86%e5%99%a8)
  - [1.6. spring bean 零配置支持](#16-spring-bean-%e9%9b%b6%e9%85%8d%e7%bd%ae%e6%94%af%e6%8c%81)
    - [1.6.1. 标注 bean 注解](#161-%e6%a0%87%e6%b3%a8-bean-%e6%b3%a8%e8%a7%a3)
    - [1.6.2. @Resouce 依赖配置](#162-resouce-%e4%be%9d%e8%b5%96%e9%85%8d%e7%bd%ae)
    - [1.6.3. 自动装配与精确装配 spring 4.0](#163-%e8%87%aa%e5%8a%a8%e8%a3%85%e9%85%8d%e4%b8%8e%e7%b2%be%e7%a1%ae%e8%a3%85%e9%85%8d-spring-40)
    - [1.6.4. 使用注解来定制 bean 方法成员的生命周期](#164-%e4%bd%bf%e7%94%a8%e6%b3%a8%e8%a7%a3%e6%9d%a5%e5%ae%9a%e5%88%b6-bean-%e6%96%b9%e6%b3%95%e6%88%90%e5%91%98%e7%9a%84%e7%94%9f%e5%91%bd%e5%91%a8%e6%9c%9f)
  - [1.7. spring 容器中的 bean 实现不同方法](#17-spring-%e5%ae%b9%e5%99%a8%e4%b8%ad%e7%9a%84-bean-%e5%ae%9e%e7%8e%b0%e4%b8%8d%e5%90%8c%e6%96%b9%e6%b3%95)
    - [1.7.1. @Bean Annotation](#171-bean-annotation)
    - [1.7.2. Bean Scope](#172-bean-scope)

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
3. 实例工厂方法创建 bean 。顾名思义，此方法与 静态工厂方法 的不同之处在于使用工厂实例进行创建 bean 。所以这儿能过 factory-bean 来指定工厂实例，再通过 factory-method 指定创建 bean 的方法。如果需要参数通过 ceonstructor-arg 指定参数值。

## 1.4. 协调作用域不同的 bean

> 当 singleton bean 依赖于 prototype bean 时，会因为 spring 窗口初始化时会先预初始化 singleton bean ，如果  singleton bean 依赖于 prototype bean ，就不得不先将依赖的 prototype bean 初始化好，再注入到 singleton bean。这就带来一个不同步的问题（多个 singleton bean 需要同一个 prototype bean）。

解决不同步的方法：

1. 放弃依赖注入。每次需要 prototype bean 时都向窗口请求新的 bean 实例，这样每次都会产生新的 bean 实例（但是作为 prototype bean 的确是每次都产生新的实例，这里没有搞懂）。
2. 利用方法注入。通常使用 `lookup` 方法注入，此方法会让 spring 容器重写容器中的 bean 的抽象或具体方法，返回查找容器中其他 bean 的结果，被查找的 bean 通常是一个 non-singleton bean 。 spring 通过使用 JDK 动态代理或者 cglib 库修改客户端代码实现上述动作。
   1. 为了使用lookup方法注入，大致需要如下两步：
        1. 将调用者Bean的实现类定义为抽象类，并定义一个抽象方法来获取被依赖的Bean。
        1. 在<bean.../>元素中添加<lookup-method.../>子元素让Spring为调用者Bean的实现类实现指定的抽象方法。

_Spring会采用运行时动态增强的方式来实现 `<lookup-method.../>`元素所指定的抽象方法，如果目标抽象类实现过接口，Spring 会采用 JDK 动态代理来实现该抽象类，并为之实现抽象方法；如果目标抽象类没有实现过接口，Spring会采用cglib实现该抽象类，并为之实现抽象方法。Spring4.0 的 spring-core-xxx.jar 包中已经集成了 cglib 类库。_

## 1.5. spring 后处理器

spring 提供两种后处理器：

1. bean 后处理器 对容器中的 bean 进行后处理 加强
    1. bean 后处理器是一种特殊的 bean 无需 id 属性，不对外提供服务对 容器内其他 bean 进行后处理，其他的 bean 创建成功后，对 bean 进行近一步的增强处理。
    2. bean 后处理器必须实现 BeanPostProcessor 接口。该接口有两个方法：
        1. Object postProcessBeforeInitialization(Object bean, String beanName)
        2. Object postProcessAfterInitialization(Object bean, String beanName)
            1. 这两个方法的参数：bean 是即将进行后处理的 bean ， beanName 是这个 bean 在容器中配置的 id
            2. before 方法用于 bean 初始化（调用 afterPropertiesSet; 调用 init-method 指定的方法）之前， after 方法用于 bean 初始化之后。
    3. 如果使用 BeanFactory 作为 spring 容器，必须手动注册 bean 后处理器，必须获取 bean 后处理器实例
2. 容器后处理器 对 ioc 容器进行加强处理，增加其功能
    1. 负责处理容器本身，用于增加容器功能
    2. 容器后处理器必须实现 BeanFactoryPostProcessor 接口，其中方法：
        1. postProcessBeanFactory(ConfigurableListableBeanFactory BeanFactory)
    3. 同样，如果 使用 BeanFactory 作为容器，必须手动调用容器后处理器来处理 BeanFactory 容器。

## 1.6. spring bean 零配置支持

> spring 零配置是指通过**注解**来实现 beans.xml 中配置 spring bean 容器的功能
> 在 spring 配置文件中指定自动扫描的包： `<context:component-scan base-package="package.path.name"/>`

### 1.6.1. 标注 bean 注解

- `@Repository` 标注为 DAO 组件类
- `@component` 标注为一个普通组件类
- `@Service` 标注为一个业务逻辑组件类
- `@Controller` 标注为一个控制器组件类

### 1.6.2. @Resouce 依赖配置

> 位于 javax.anotation 包

- 使用 `@Resource` 配置一个 bean 类的依赖，用于指定协作 bean 。这就与 `<property ...>` 中的 ref 属性有相同的效果。
- 可以用于修改 setter 方法
- 还可以直接修辞 实例变量。这样使用更为简单，spring 将会使用 javaEE 规范的 field 注入，setter 方法都不用写了。

### 1.6.3. 自动装配与精确装配 spring 4.0

- `@autowired` 指定自动装配
    - 可以用来修辞 setter方法/普通方法/实例变量/构造器
    - 当用来修辞 setter 或实例变量时，使用的是 byType 策略从 spring 容器中查找 bean ，如果容器中有多个同一类型的 bean 就会拋出异常。
    - 为解决上述问题，spring 4.0 就增加了 `@Qualifier` 注解
        - `@Qualifier` 用于精确装配 bean ，其方法是在其中指定 bean id 。因此如果要使用此注解来装配，就得将被装配的 bean id（也就是 beanName，通常的标注注解就这一个属性，默认为 ""） 标注出来。

### 1.6.4. 使用注解来定制 bean 方法成员的生命周期

现个注解实现(javax.anotation 包)：

- `@PostConstruct` 顾名思义，是在 bean 构造之后执行，修辞的是 bean 的初始化方法；
- `@PreDestroy` 修辞 bean 销毁之前执行的方法

## 1.7. spring 容器中的 bean 实现不同方法

[参考](https://www.cnblogs.com/duanxz/p/7493276.html)

可以通过 java 配置类来实现 spring beans 的配置：

- @Configuation 等价于 `<Beans></Beans>`
- @Bean 等价于 `<Bean></Bean>`
- @ComponentScan 等价于 `<context:component-scan base-package="com.dxz.demo"/>`

[获取 xml applicationContext 方法参考](https://www.cnblogs.com/yjbjingcha/p/6752265.html)

### 1.7.1. @Bean Annotation

使用 `@Bean` 注册一个实例到 IoC 容器中。

- 当要配置一个需要构造函数构造的 bean 时，需要指定其构造函数的参数。[指定构造器参数 bean](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-constructor-injection)
- `@Bean` 是一个方法级别的注解，使用在方法之上，方法返回类型即 bean 实例类型。
- 推荐方法返回具体实例类型而不是接口类型。[reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-java-bean-annotation)
- 对一个 bean 指定生命周期回调
    - 在 `@Bean` 中指定 `initMethod` `destroyMethod` 两个 bean 方法名，用以决定 bean 在初始化后现销毁前的回调。
    - `destroyMethod` 默认为 `deferred` 推断模式，在容器销毁前自行推断其销毁方法，如果想在容器销毁时保留 bean ，可以指定 `destroyMethod=""`。

### 1.7.2. Bean Scope

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
        1. 方法可以是抽象方法也可是具体方法，IoC 容器会通过 CGLIB 为方法所在的类生成子类覆盖方法，所以 `@Lookup` 只能在 IoC 容器能通过常规构造器初始化的 bean 中才能生效。也就是：Lookup 不能为工厂方法生产 bean 方法所替代，因为不能动态地为工厂方法所生产的 bean 提供子类。method 与 class 均不能为 final 修辞。
        2. 在 spring 使用场景中需要注意：需要为 Lookup 方法提供具体实现，否则 component scanning 之类会过滤掉抽象 bean。同时， Lookup method 不能在 configuration class 中配置的 `@Bean` 方法上生效，需要使用 `@Inject` 之类的注解代替。
