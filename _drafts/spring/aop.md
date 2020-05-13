---
layout: "post"
title: "aop"
date: "2018-11-26 10:50"
---
<!-- TOC -->

- [1. aop 面向切面](#1-aop-%e9%9d%a2%e5%90%91%e5%88%87%e9%9d%a2)
  - [1.1. AOP Concepts](#11-aop-concepts)
  - [1.2. aop 实现分为两类](#12-aop-%e5%ae%9e%e7%8e%b0%e5%88%86%e4%b8%ba%e4%b8%a4%e7%b1%bb)
  - [1.3. spring aop](#13-spring-aop)
    - [1.3.1. AOP Proxies](#131-aop-proxies)
  - [1.4. @AspectJ Support](#14-aspectj-support)
    - [1.4.1. Enable @AspectJ Support](#141-enable-aspectj-support)
    - [1.4.2. Declaring an Aspect](#142-declaring-an-aspect)
    - [1.4.3. Declaring a Pointcut](#143-declaring-a-pointcut)
      - [1.4.3.1. Supported Pointcut Designators](#1431-supported-pointcut-designators)
        - [1.4.3.1.1. Spring AOP 与 AspectJ 不同之处](#14311-spring-aop-%e4%b8%8e-aspectj-%e4%b8%8d%e5%90%8c%e4%b9%8b%e5%a4%84)
        - [1.4.3.1.2. Notes](#14312-notes)
      - [1.4.3.2. Combining Pointcut Expressions](#1432-combining-pointcut-expressions)
      - [1.4.3.3. Examples](#1433-examples)
      - [1.4.3.4. optimize](#1434-optimize)
    - [1.4.4. Declaring Advice](#144-declaring-advice)

<!-- /TOC -->

# 1. aop 面向切面

> aop aspect oriented programming 。面向切面编程，常用于具有横切性质的系统级服务，如：事务管理、安全检查、缓存、对象池管理。
> **需要说明的是** aop 是一种编程思想，并不仅限于 java 更不仅限于 java spring 。但这儿主要针对 java spring aop 进行讨论。纵向重复代码在横向上抽取。

aspcetj 是基于 java 语言的 aop 框架，提供了强大的 aop 功能，其他众多的 aop 框架都借鉴了其思想。包括两个部分：

- 定义如何表达/定义 aop 语法规范。用于解决 java 中的交叉关注点问题。
- 工具部分：编译/调试。

## 1.1. AOP Concepts

Aspect Oriented Programming 基本概念

1. Aspect: 多个类间的模块化的事物。企业应用中的事务管理就是很好的跨类切面的例子。在 Spring AOP 中通过常规类（schema 途径）或注解 `@AspectsJ` 实现切面。
2. Join Point: 程序执行中的某一点，如一个方法执行或异常处理。在 Spring AOP 中，一个 Joint Point 通常代表一个方法执行。
3. Advice: 切面在入点（Joint Point）所采用的动作。大多框架（包括 Spring AOP）将 Advice 建模成拦截器，并为 Join Point 维护一个拦截器链。Advice 类型有："Around" "Before" "After" 。Spring AOP 包括以下类型：
   1. Before Advice：在 Join Point 前执行的增强。除非抛出异常，此类型的 Advice 并不能阻止 Joint Point 流程的执行。
   2. After Returning Advice：在 Join Point 正常流程执行完成（未抛出异常）后执行。
   3. After Throwing Advice: 在方法因异常退出时执行
   4. After(finally) advice: 在 join point 方法结束后执行（不管是正常结束还是异常结束）。
   5. Around advice：在 join point 前后都可执行的 advice。最强势的 advice ，可自定义方法调用前后的行为，也可以决定 join point 是否执行或通过返回自定义的结果（或抛出异常）实现增强方法的快捷执行。
4. Pointcut： 匹配 Join Point 的判断。Advice 关联一个 Pointcut 表达式，并在每一个匹配上 Pointcut 的切点处执行（eg：执行有某个特定名字的方法）。匹配上 Pointcut 表达式的 Join Point 的概念是 AOP 的核心，Spring 默认使用使用 AspectJ pointcut 表达式语言。
5. Introduction: 引入外部方法或字段到一个类。Spring AOP 可以引入新的接口与相应的实现到被增强的类。在 AspectJ 社区中，introduction 通常被当作一个内部类的定义。
6. Target Object:被一个或多个增加的对象。也被当作被增强的对象。Spring AOP 通过运行时代理实现，所以 Target Object 也是一个代理对象。
7. AOP Proxy：AOP 框架实现 aspect 规约（增加方法执行等等）而创建的对象。在 Spring AOP 中，AOP proxy 通常为 JDK 动态代理或 CGlib 代理。
8. Weaving: 将 aspect 与其他应用的类型或对象连接以创建增加类。此动作可在编译期（通过 AspectJ Compoler）、加载期、运行时进行。Spring AOP 与大多 Java AOP 框架一样都在编译期执行 weaving。

## 1.2. aop 实现分为两类

- 静态 AOP 实现：在编译阶段就对程序进行修改，即实现对目标类的增强，生成静态的 AOP 代理类，以 aspcetj 为代表。具有良好的性能，但需要特殊的编译器。
- 动态 AOP 实现：AOP 框架在运行阶段动态生成 AOP 代理，以实现对目标对象的增加，如： spring AOP 。纯java 实现，无需特殊编译器，性能相对略差。

**基本概念**：

- Aspect 切面：用于组织多个 advice ， advice 就放在 aspect 中定义；
- Joinpoint 连接点：程序执行过程中明确的点，如：方法的调用/异常的拋出。 在 Spring AOP 中，连接点总是 方法调用。
- Advice 增强：AOP 框架支持在特定的切入点执行的增加处理。类型有：Before Around After
- Pointcut 切入点：中以插入增强处理的连接点。当连接点满足指定要求时，该连接点将被添加增加处理，该连接点也就说变成了切点。

## 1.3. spring aop

- Spring AOP 代理由 IoC 容器负责生成、管理，其依赖关系也由 IoC 窗口负责管理。
- 在 Spring 使用 Aspectj 支持需要添加三个库：
    - aspcetjweaver.jar
    - aspectjrt.jar
    - aopalliance.jar
- 配置文件加如下配置：

  ```xml
  <!--启动@AspectJ支持-->
  <aop:aspectj-autoproxy/>

  <!--指定自动搜索Bean组件、自动搜索切面类-->
  <context:component-scan base-package="edu.shu.sprint.service">
      <context:include-filter type="annotation" expression="org.aspectj.lang.annotation.Aspect"/>
  </context:component-scan>
  ```

### 1.3.1. AOP Proxies

- Spring AOP 默认使用 JDK 动态代理，也可以使用 CGLIB 代理，一般在被代理对象没有实现接口的情况下使用。

## 1.4. @AspectJ Support

### 1.4.1. Enable @AspectJ Support

- 在 `@Configuration` 上添加 `@EnableAspectJAutoProxy` 注解，让被增加的 bean 自动代理。
- 使用 XML 配置 添加标签 `<aop:aspectj-autoproxy />`

### 1.4.2. Declaring an Aspect

当 @AspectJ 打开后，Spring 会自动检测容器中定义的 Aspect 。定义 Aspect 的方法有两种：

1. 使用 XML 配置添加了 `@Aspect` 的 bean
2. 使用自动扫描注解 `@Aspect` 的 bean，使用自动扫描 bean 方式时需要在 bean 上添加额外的 `@Component` 或自定义的扫描组件注解。

- 使用 `@Aspect` 注解后的 bean 与其它类一样可以有自己的字段方法，同样可以定义 pointcut/advice
- Aspects 不能成为其他 aspect 增强的目标，因为 aspect 已经被 `@Aspect` 注解为一个 Aspect 被自动代理排除在外。

### 1.4.3. Declaring a Pointcut

一个 Pointcut 的定义包括两部分:

1. 由名与任意参数组成的签名（由一个方法定义，此方法签名的返回值必须为 `void`）；
2. 使用 `@Pointcut` 注解表达的 pointcut 表达式。

#### 1.4.3.1. Supported Pointcut Designators

支持 Pointcut 的标识符（PCD pointcut designators 用来匹配符合的 join point，限制 Spring AOP 方法执行）

- execution: 匹配 join points，主要的 pointcut 标识符
- within: 限制 join points 所在的类需要是特定类型
- this: bean reference（Spring AOP 代理对象）为指定类型的实例限定 join points 匹配
- target: 目标对象（被代理的应用对象）是指定 type 的实例
- args: 限制参数（arguments）为指定类型（type） 的实例
- @target：限制执行对象需要有指定类型的注解
- @args: 限制运行时被传递的参数必须有指定类型的注解
- @within: 限制 join points 所在的类需要有指定的注解
- @annotation: 限制 join points (AOP 中所执行的方法) 需要有指定的注解。

##### 1.4.3.1.1. Spring AOP 与 AspectJ 不同之处

- 除以上几个标志符外，AspectJ 还有其他的 Designator（如：`call`,`withinCode`,`@withinCode`, etc.），但若在 Spring 中使用这些标志符会抛出 `IllegalArgumentException`。
- Spring AOP 窄化了 AspectJ 中标志符的定义，join points 只匹配了方法执行。
- AspectJ 还有基于类型的语义（type-based semantics），且其标志符 `this` `target` 都是指相同的对象：执行方法的对象。而在 Spring AOP 是基于代理的系统，其代理（与 `this` 绑定）与其在代理之后的目标对象（与 `target` 绑定）是不同的。
- 因为 Spring AOP is proxy-based 框架本质，所以直接调用其目标对象，并不能实现 AOP 拦截。JDK 动态代理只能对 public interface method 进行拦截，而CGLIB 代理 public/protected （如果需要，package-visible  方法也可以）都可被代理。
- `bean` Spring AOP 有一个原生 AspectJ 所没有的 PCD ： `bean`。通过 bean name 匹配 bean ，也可加上通配符 `*` 匹配 bean 集合。与其他标签符一样， bean 可以使用 `&&` `||` `!` 运算符。  
    - `bean` 是针对 Spring 扩展的 PCD ，因此在 `@Aspect` 模式中无效。 `bean` PCD 在实例级别上运行不仅仅是在类型级别上，instance-based PCD 是 Spring 基于代理的 AOP 框架的功能，与 Spring bean 工厂紧密整合，因此能自然而直接地通过 name 识别 bean。

##### 1.4.3.1.2. Notes

- 切点定义通常与任何拦截的方法相匹配。如果切入点被严格定义为只公开的，那么即使在 CGLIB 代理场景中，通过代理进行潜在的非公开交互，也需要相应地定义它。
- 如果拦截需要包含目标对象的方法调用或构造器，需要使用 Spring 驱动的 native AspectJ weaving 而不是 Spring AOP 代理驱动的框架。

#### 1.4.3.2. Combining Pointcut Expressions

[reference](https://docs.spring.io/spring/docs/5.2.5.RELEASE/spring-framework-reference/core.html#aop-pointcuts-designators)

eg:

```java
@Pointcut("execution(public * *(..))")
private void anyPublicOperation() {}

@Pointcut("within(com.xyz.someapp.trading..*)")
private void inTrading() {}

@Pointcut("anyPublicOperation() && inTrading()")
private void tradingOperation() {}
```

- 第一个 pointcut 使用 `execution` 匹配访问级别为 public 的方法
- 第二个 pointcut 使用 `within` 匹配 `trading` 包下的方法
- 第三个 pointcut 使用 `&&` 运算符将前两个 pointcut 交集

- 使用更小的命名组件组装一个更复杂的 pointcut expression 更为合适，通过 name 引用 pointcut 时，java 的可见性规则（private/default/protected/public）会被引入。但 visibility rules 并不影响 pointcut 匹配。

#### 1.4.3.3. Examples

最常用的 PCD : `execution`，对于此 PCD ，其标准的 pointcut expression 是：`execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) thrown-pattern?)`

- 除 ret-type-pattern (返回类型字段) /name-pattern/param-pattern 外，其他字段都是可选的。
- returning-type-pattern 指定 join points 匹配的返回类型，`*` 表示任何类型都匹配。全限定名的类型只匹配返回类型为指定类型的方法。
- name-pattern 匹配方法名，可以使用 `*` 通配所有或部分方法名，如果定义了 declaring-type-pattern（指定定义方法所在类），在其后追加一个 `.` 与 name-pattern 组件连用。
- param-pattern 相对复杂一些。`()` 表示匹配无参数的方法，`(..)` 表示匹配任意数量参数的方法，`(*)` 表示匹配含一个任何类型的参数，`(*,String)` 表示匹配有两个参数的方法，其中第一个参数为任何类型，第二个参数为 String 。[reference](https://www.eclipse.org/aspectj/doc/released/progguide/semantics-pointcuts.html)
- `execution(param)` 模式下的匹配与 `args()` 模式匹配的不同：execution 模式下表示一个方法在签名处定义的参数为指定类型，而 args() 模式表示在方法在运行时被传递的参数为指定类型。

#### 1.4.3.4. optimize

优化 PCD

为了优化性能，AspectJ 在编译期处理 pointcut。检查代码并决定 join points 是否匹配(静态或动态)一个指定的 pointcut 是耗能不低的。（动态匹配指通过静态分析并不能完全决定是否匹配，需要添加一个 test 在代码处在运行时决定实际是否匹配）。当首次解析一个 pointcut 时，AspectJ 会为匹配流程将 pointcut 重写成一个最优形式。一般来说，pointcuts 会被重写成 DNF(Disjunctive Normal Form)，且 pointcut 组件会按越易计算越先检查的顺序重排序。这就意味着不需要考虑不同的 PCD 性能开销与 pointcuts 定义的顺序。

已知的 PCD 自然地分为三组：

1. kinded designator 选择一个类型的 join point : `excution` `get` `set` `handler`
2. scoping designator 选择 join point 范围 : `within` `withincode`
3. contextual designators 根据上下文匹配 join points : `this` `target` `@annotation`

notes:

1. 一个优质的 pointcut 至少需要包含 kinded 与 scoping 两种类型的 designator;
2. 仅提供 kinded designator 或 contextual desinator 能够正常工作，但性能不佳，因为需要额外的解析
3. Scoping designator 可快速匹配，使用此类 PCD 可以快速地忽略不必要的 join points 组。

### 1.4.4. Declaring Advice

Advice 与一个 pointcut expression 相关联，并在此 pointcut 匹配的方法执行 before/after/around 切点执行。这个 pointcut expression 要么是一个被命名的 pointcut 简单引用，要么是一个相应位置的 pointcut expression。

- After Returning Advice :  一个正常执行完成的方法执行增强。使用注解 `@AfterReturning(returning="retVal")` 。可以指定方法执行的返回值为 Advice 方法的参数。指定属性 `returning` 的值与 Advice 定义的参数名要保持一致，同时 `returning` 语句也对 join points 进行约束，其方法执行与此处的类型需要一致。使用此类型的 Advice 返回的引用不可能完全不同。
- After Throwing Advice : 抛出异常的方法执行的增强。可以使用 `throwing=` 与其异常参数类型配合限制异常的匹配。
