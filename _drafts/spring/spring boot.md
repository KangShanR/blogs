---
date: 2020-04-10 12:14:38
tags: [framework,java,spring boot]
categories: programming
description: spring boot
---

# spring boot

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#getting-started-scoop-cli-installation)

安装 spring CLI(command line interface)，后再运行本地 groovy 文件。

## spring boot 的简化处理

1. 将所有依赖都写入其基本配置中，最大简化其配置；
2. 自动扫描，一个注解自动识别所有的需要扫描的包与 bean；
3. `@RestController` 指定此类为一个特殊处理器， spring 会识到并用来处理网络请求；
4. `@RequestMapping` 提供 “路由” 信息给 spring ，spring 也会对处理结果进行渲染返回给调用者；
5. `@EnableAutoConfiguration` 有这个注解 spring boot 会主动根据 pom 文件引入的依赖为你配置，eg: pom 中配置了 `spring-boot-starter-web` , spring boot 会假设工程是一个 web 应用并为其添加相应的依赖。
6. `@ComponentScan` 自动扫描包中的 spring 组件。
7. `@Configuration` 相当于 spring bean 的容器，也可以外部的 bean 或配置组件导入。
8. `@SpringBootApplication` 声明 spring boot 入口，拥有的功能相当于 `@EnableAutoConfiguration` `@Configuration` `@ComponentScan` 三者之和。同时， spring boot 的入口类最好放在包根路径下，因为组件扫描时时会隐式地将此注解所在类定义为 search package 。`@SpringBootApplication` 和 `@EnableAutoConfiguration` 永远只添加一个，一般建议选择任意一个添加到主 `@Configuration` 类上。
9. 组件导入
   1. 建议使用 `@Configuration` java 组件， `@Import` 可用来将其他组件导入，也可以使用 `@ComponentScan` 自动扫描所有的 spring 组件，包括 `@Configuration` 配置组件
   2. 如果非要使用 xml 配置，使用 `@ImportResource`

### create an executable jar

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#getting-started-first-application-dependencies)

[build spring boot on maven](https://docs.spring.io/spring-boot/docs/2.3.2.BUILD-SNAPSHOT/maven-plugin/reference/html/)

对于一个工程来说依赖各个 jar 包，而 java 并没有提供一个标准的方式去加载内嵌的 jar ，因此不能发布一个独立的程序包。

为此，很多开发者的解决方案是创建一个 'uber' jar，所谓 uber jar 是指将应用所有的依赖的 class 文件整合成一个单独的包。这样做的问题在于：1、 难以查看依赖库；2、不同 jar 包中要是存在同名的 class 就会出现问题。

spring boot 使用了不同的方式达到直接使用内嵌包。

1. 打包： `mvn package`
2. 查看 jar 包内打包文件 `mvn tvf jar_file`
3. pom 中 groupid `org.springframework.boot`
4. 指定打包为可执行 jar 包的插件

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

## how to serias

[spring boot HOWTO 系列](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-properties-and-configuration)

[设置 spring 项目 profile](https://stackoverflow.com/questions/38520638/how-to-set-spring-profile-from-system-variable)

### spring 项目中使用 profile

[spring 项目 profile](https://www.baeldung.com/spring-profiles)

用以指定当前运行环境，可以 maven 打包时也可以设置针对环境打包。

获取当前活跃的 profile ，参考 `6. Get Active Profiles`

## Structing Code

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#using-boot-structuring-your-code)

### Locating the Main Application Class

- 在根目录添加 Application.class 主类放在根目录上，使用 `@SpringBootApplication` 注解在主类上。此注解也隐匿地定义了某些特定项目的基础查找包 `base search package` 。_如果在root package 定义多个 application.class 打包发布时会认定哪个为项目入口呢？_亲测结果：`spring-boot-maven-plugin:2.3.1.RELEASE:repackage failed: Unable to find a single main class from the following candidates` 打包插件将报出不能找到唯一的主类异常
- 若不使用 `@SpringBootApplication` 注解，可以使用 `@EnableAutoConfiguration` `@ComponentScan` 两个注解实现相同功能

### Auto-configuration

> 自动配置

- 使用 `@EnableAutoConfiguration` 后，spring boot 会检测 classpath 所有的内容进行自动添加配置。
- 可以在运行时加上 --debug 进入 debug 模式查看自动配置了哪些东西。
- 应该逐渐用特定的配置代替自动配置。
- 若 spring boot 自动配置了不需要的配置，可以在 `@SpringBootApplication` 或 `@EnableAutoConfiguration` 添加排除 exclude ，若排除的类不在 classpath 中可以指定其全限定名到 `excludename`。也可以添加配置 `spring.autoconfigure.exclude` 指定自动配置排除配置。

### Spring Beans and Dependency Injection

- spring 中的 bean 注入：使用构造器注入 `private final Dependency d;` 使用 final 修辞字段让其不能被修改。

### Using the @SpringBootApplication Annotation

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#using-boot-spring-beans-and-dependency-injection)

@SpringBootApplication 注解有三个特性，分别对应三个注解：

1. @EnableAutoConfiguration
2. @ComponentScan
3. @Configuration

@SpringBootApplication 提供别名以实现定义以上前两个注解的功能。

## Developer Tools

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#using-boot-devtools)

添加 spring-boot-devtools 依赖，实现热启动。其实现原理是固有的jar包代码使用一个固定的 classloader，而开发变动的代码使用另一个classloader，当发生变动时，重启一个 classloader 加载新的编译的代码。所以热启动相对冷启动要快些，因为其不用加载旧的不变的依赖的jar包中的代码。

- 使用 trigger-file 触发项目重启。`spring.devtools.restart.trigger-file=<.reloadtrigger>` 手动更新了 trigger 文件才会触发更新。trigger 文件可以自定义在 classpath 中任意地方，而配置文件指定配置文件不需要全限定名，更新代码后只需要修改保存一下trigger文件即可触发reload。
- 如果使用了Ultimate Edition IDEA 可以点击 relauch 触发重启。
- 生产模式下devtool 自动关闭，如果项目启动通过 java -jar 运行一个包或从一个特定的 classloader 中开始，devtools 将自动识别在生产模式中。如果应用在窗口中开启需要排除 devtools 或者直接添加系统参数 `-Dspring.devtools.restart.enabled=false`。

## Application

### Application Event and Lisener

Application Event 发布后父子 Context 中的注册的lisener 都会监听到，为了区别来自哪里，可以将 Context 注入对比。如果是个 bean 直接使用 @Autowired 注入，如果 lisener 不是 bean 需要实现 ContextAware 接口注入。
