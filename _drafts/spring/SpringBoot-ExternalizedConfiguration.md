---
date: 2020-07-19 23:48:38
tags: [java,SpringBoot,configuration]
categories: programming
description: spring boot 外部配置的应用
---

# Externalized Configuration

[spring boot 外部配置](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config)

> 外部配置

一个应用，其通常添加外部配置的方式：properties file, yml files, environment variables, 命令行参数扩展配置。配置属性可以通过 `@Value` 直接被注入到 bean 中，通过 `Environment` 抽象直接访问，也可以通过 `@ConfigurationProperties` 绑定到结构对象上。

Spring Boot 加载 `PropertySource` 有明确的顺序，以保证正确覆盖配置值。其**配置优先级**从高到低依次为：

1. 当 devtool 在激活状态时， `$HOME/.config/spring-boot` 中的 devtool 全局配置
2. `@TestPropertySource` 注解的 test 配置
3. `@SpringBootTest` 注解与局部应用 test 注解上的 `Properties` 属性
4. 命令行参数
5. 内嵌于环境变量或系统属性的行内 JSON `SPRING_APPLICATION_JSON` 属性
6. `ServletConfig` 初始化参数
7. `ServletContext` 初始化参数
8. `java:comp/env` JNDI 属性
9. `System.getProperties()` Java 系统属性
10. 操作系统环境变量 OS environment variables
11. `random.*` 中的随机属性值 `RandomValuePropertySource`
12. 包外的特定 profile 属性文件 `application-{profile}.properties` 与 YAML 变体
13. 包内特定 profile 属性文件 `application-{profile}.properties` 或 YAML 变体
14. 包外应用属性文件 `application.properties` 和 YAML 变体
15. 包内应用配置文件  `application.properties` 和 YAML 变体
16. `@PropertySource` 注解于 configuration 类上标注的属性文件。需要注意的是，这种配置在 application refreshed 前不会被加载到 `Environment` 中去，因此如果使用这种方式添加诸如 `logging.*` `spring.main.*` 配置是无效的，因为在 context refreshed 前，这些配置已经被读取了。
17. 通过 `SpringApplication.setDefaultProperties()` 设置的默认属性。

Spring Boot 在加载配置时支持通配路径，在外部指定不同路径下的同名配置文件时使用通配路径就会很方便。**通配路径必须包含且仅包含一个 `*` ，并且当以文件夹结尾时以 `/` 结尾，以文件为查找对象时以 `/<filename>` 结尾**。查找出的位置以文件路径的字母顺序排序。

## Configuring Random Values

配置随机值随机注入一个 integer/long/uuid/string :

```properties
my.secret=${random.value}
my.number=${random.int}
my.bignumber=${random.long}
my.uuid=${random.uuid}
my.number.less.than.ten=${random.int(10)}
my.number.in.range=${random.int[1024,65536]}
```

随机配置语法 `${random.int*}`是 `OPEN value (,max) CLOSE` `OPEN` 与 `CLOSE` 指代任意符号用以将最大值与最小值包起来，`value` 与 `max` 是 integer 。如果提供 `max`，`value` 就指最小值，`max` 指最大值（不包含）。

## Accessing Command Line Properties

[访问命令行参数](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config)

默认情况下命令行参数在 Spring 配置中有最高优先级别。在启动命令行中以 `--` 开始指定命令行参数。如果需要禁用命令行参数加入到系统 `Environment` 中，可以 `SpringApplication.setAddCommandLineProperties(false)`。

## Application Property Files

[应用配置文件](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-application-property-files)

`SpringApplication` 将不同位置的配置文件 `application.properties` 或 `.yaml` 数据加载到 `Environment` 中，不同位置加载优先级为：

1. 当前路径子路径 `/config`
2. 当前路径 The current directory
3. classpath `/config` 包
4. classpath 根

高优先级配置会覆盖低优先级配置。

如果需要修改配置文件，可以指定环境属性 `spring.config.name` 指定另一个配置文件，同时可指定 `spring.config.location` 多个配置文件路径（可为逗号 `,` 分隔的多个文件夹路径或文件路径，排列越靠后优先级越高）。这两个环境属性在很早期需要用以加载文件，所以必需以环境属性的方式（An OS Environment Variable, a system property, a command-line argument）指定。

当 `spring.config.location` 指定文件夹路径时，需要以 `/` 结尾，在运行时将在其后追加 `spring.config.name` 中分离出的文件名与 profile-specific file name 。当 location 指定了全文件名时，直接使用此文件名，但不支持 profile-specific variant，并被 profile-specific 属性覆盖。

`spring.config.additional-location` 用以指定额外的配置路径，其优先级大于默认的路径

## Profile-specific Properties

通过 `spring.profiles.active` 变量激活当前应用的配置文件。profile 配置文件规约其名为 `application-{profile}.properties` ，当没有指定 profile 时， Environment 自动使用 `default` 作为 profile 环境配置，`application-default.properties` 中的属性将被加载。

与标准的 `application.properties` 配置文件一样，profile 配置文件将从指定位置加载，同时不管所打包的 jar 包内还是包内，profile-specific file 配置覆盖 non-specific ones。If several profiles are specified, a last-wins strategy applies.如果指定多个，后者覆盖前者。

如果在 `spring.config.location` 中指定了配置文件，那么此文件在 profile-specific variant 匹配中就不再考虑了。换句话说，如果需要使用 profile-specific 策略匹配配置文件，就不要在 `spring.config.location` 中添加相关的文件，最好其中只指定 directories 不指定文件。

## Placeholders in Properties

在使用配置文件中 `application.properties` 配置的值时会通过存在的 `Environment` 过滤，所以，可以在配置中使用占位符 `${}` 引用先前定义好的配置（如：系统属性）。

## Type-safe Configuration Properties

> [类型安全配置属性](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-java-bean-binding)

### JavaBean Properties Binding

[JavaBean 属性绑定](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-java-bean-binding)

- Spring Boot 映射配置到 javaBean 属性中并不是直接使用类的 Accessors(getter/setter)；
- 如同 Spring MVC ，配置属性绑定通过标准的 Java Beans 属性描述符实现，所以在默认的空的构造器与 getter setter 通常是必须的；
- 在以下情况下 setter 可以省略：
    - 被初始化过的 Maps，只需要 getter 不需要 setter，因为他们可以通过 binder 改变。
    - Collections Arrays 要么通过 `.yml` index 要么通过 `.properties` 的逗号分隔的单个值。在后者，setter 是必须的。如果初始化一个 collection ，保证其是可变的。
    - 如果是内嵌的 POJO 属性，且已被初始化，setter 可以省略。如果需要让 binder 快速初始化 POJO ，这时 setter 是需要的。
- 使用 Lombok 自动生成时，保证不要生成特定类型的构造器，因为容器可能需要用来初始化对象。
- 只有标准 Java Bean 属性才能被绑定，**不支持绑定属性到静态字段上。**

### Constructor Binding

[构造器绑定](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-java-bean-binding)

- 在类上添加注解 `@ConstructorBinding`
- 内嵌的属性 POJO 也会使用构造器绑定方式绑定配置属性。
- 默认值可使用 `@DefaultValue("default")` 注解添加在属性上，当 binder 找不到相关的配置就会强制将默认值添加到指定属性上
- 默认情况下，如果没有为属性配置值，对象属性将为 null 。如果需要返回一个非 null POJO，可以指定一个空的 `@DefaultValue` 在其上。
- 使用构造器绑定，**需要添加 `@EnableConfigurationProperties` 或配置属性扫描**。常规 Spring Bean 创建机制创建的 Bean （@Component @Bean @Import）上并不能通过此构建器绑定属性。
- 如果绑定的 Class 有多个 Constructor ，可直接将 `@ConstructorBinding` 注解在需要的构造器上。

### Enable @ConfigurationProperties-annotated Types

[注册配置 bean](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-java-bean-binding)

- Spring Boot 提供了绑定配置到类的机制也提供注册其为 Bean 的机制。可以一个类一个类地配置也可像组件扫描一样配置属性扫描。
- 如果需要部分扫描到配置属性，可以在 `@EnableConfigurationProperties` 指定 type.class ，任意 `@Configuration` 组件是添加此属性。
- 添加 `@ConfigurationPropertiesScan` 注解在 Application 上会自动扫描包内所有的配置属性 Bean，注解上可添加包。
- 当 Bean 注册到容器中后，这个 bean 有一个便名 `<prefix>-<fqn>` ，`<prefix>` 是在 `@ConfigurationProperties(prefix="")` 上指定的前缀， `<fqn>` 指其全限定名。如果没有指定 prefix ，只有全限定名会为此 bean 所用。_这他妈有啥用？_

### Using @ConfigurationProperties-annotated Types

[使用自动配置属性 Bean](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-java-bean-binding)

在组件 Bean 直接无注解注入 `private final ConfiguredData configuredData;`

### Third-party Configuration

> 第三方 Bean 属性注入

- 直接在注入第三方 Bean 方法定义处加上 `@ConfigurationProperties` 注解，将自动将同名配置注入到 Bean 属性中。

### Relaxed Binding

> 松绑定

Spring Boot 松绑定 Environment 属性到 @ConfigurationProperties bean 中，所以不需要精确匹配 Environment 属性名与 bean 属性名。常见的例子是使用 dash-separated 或大小写环境属性 context-path 绑定到 contextPath，PORT绑定到 port。

使用 `@ConfigurationProperties(prefix="project.data")` 注解在有名为 `firstName` 属性的 bean 上（prefix 的值必须为 Kebab Case 写法），那么在配置文件中可以使用：

1. `project.data.first-name`: Kebab Case （小写，单词分隔使用 `-`，看起来像羊肉串，所以叫 Kebab Case），推荐在 `.properties` 或 `.yml` 中使用
2. `project.data.firstName` : standard camel case syntax 标准驼峰语法
3. `project.data.first_name` : underscore notation，下划线符号，在 `.properties` `.yml` 中一种可选的写法
4. `PROJECT_DATA_FIRSTNAME` : upper case format，推荐在系统环境变量中使用。

#### 从环境变量绑定数据 Binding from Environment Variables

大多操作系统的环境变量都使用严格命名规则，如 Linux 系统 shell 变量只能使用字母数字下划线，Unix 系统 shell 变量只能使用大写。Spring Boot 为与这些系统兼容，规则如下：

1. 用 `_` 代替 `.`
2. 移除所有的 `-`
3. 转换为大写形式

比如：配置属性 `spring.main.log-startup-info` 将被系统环境变量 `SPRING_MAIN_LOGSTARTUPINFO` 的值所绑定。note:_下划线并不会被 dash 所替换_

环境变量同样可以绑定到数组对象，数组的下标数字被下划线代替：`data[0].name` 被环境变量 DATA_0_NAME 值所赋。

### Merging Complex Types

使用 `.yml` 与 `.properties` 外部配置文件添加属性给bean 时，会自动将各个同配置注入到列表属性中，也可以在其中指定不同的 profile 下不同的属性。

### Properties Conversion

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-relaxed-binding)

Spring Boot 内置转换器可以将对多个类型数据进行转换，使用 `@**Unit` 注解指定单位

1. Duration，ms `@DurationUnit` ns us ms s m h d
2. Period ， days `@PeriodUnit` y m w(weeks is a shortcut means "7 days") d
3. DataSize, byte `@DataSizeUnit` B KB MB GB TB

可自定义 converter。

### @ConfigurationProperties Validation

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-validation)

- 在注解 `@ConfigurationProperties` 的类上添加 `@Validated` ， 用以约束字段。也可以使用 JSR-303 `@Validation` （需要保证 JSR-303 在 classpath 之中）。在 @Configuration 组件 bean 定义处添加 `@Validated` 注解用以验证 bean 。
- 验证内嵌的属性，其相应的字段需要添加 `@Valid` 注解
- 自定义 Spring Validator 通过添加一个名为 `configurationPropertiesValidator` bean 定义静态方法实现，之所以要为 静态的 ，因为 configurationProperties validator 在应用生命周期很早阶段就需要实例化并使用，为避免与外部 `@Configuration` 类耦合而需要过早地将外部组件类实例化引起的错误，所以需要将此 validator bean 方法定义为静态的。

### @ConfigurationProperties vs. @Value

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-external-config-validation)

- `@Value` 是容器核心特性，不提供类似类型安全的特性，支持部分 spring 松绑定，不支持 元数据，但比 `@ConfigurationProperties` 多支持 SpEL 表达式。
- 如果同一个组件定义了多个配置属性，推荐使用 `@ConfigurationProperties` 在bean 类上，这样可以做结构化类型安全的bean 用以注入到 bean 中。
- 如果需要使用 `@Value` ，推荐引用属性名通过其标准形式 Kebab-Case using only lowercase letters 。这样做可以让 Spring Boot 使用与 `@ConfigurationProperties` 松绑定相同的逻辑。如：使用 `@Value("{demo.first-name}")` ，那么配置文件中的 `demo.firstName` 与 `demo.first-name`与系统环境变量 `DEMO_FIRSTNAME` 都会被当作有效配置（优先级此处不作讨论）。而如果使用 `@Value("{demo.firstName}")` 只有配置文件中的 `demo.firstName` 会被识别到。
