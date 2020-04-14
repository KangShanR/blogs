# spring boot

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#getting-started-scoop-cli-installation)

安装 spring CLI(command line interface)，后再运行本地 groovy 文件。

## springboot 的简化处理

1. 将所有依赖都写入其基本配置中，最大简化其配置；
2. 自动扫描，一个注解自动识别所有的需要扫描的包与 bean；
3. `@RestController` 指定此类为一个特殊处理器， spring 会识到并用来处理网络请求；
4. `@RequestMapping` 提供 “路由” 信息给 spring ，spring 也会对处理结果进行渲染返回给调用者；
5. `@EnableAutoConfiguration` 有这个注解 spring boot 会主动根据 pom 文件引入的依赖为你配置，eg: pom 中配置了 `spring-boot-starter-web` , spring boot 会假设工程是一个 web 应用并为其添加相应的依赖。
6. `@ComponentScan` 自动扫描包中的 spring 组件。
7. `@Configuration` 相当于 spring bean 的容器，也可以外部的 bean 或配置组件导入。
8. `@SpringBootApplication` 声明 spring boot 入口，拥有的功能相当于 `@EnableAutoConfiguration` `@Configuration` `@ComponentScan` 三者之和。同时， spring boot 的入口类最好放在包根路径下，因为组件扫描时时会隐式地将此注解所在类定义为 search package 。`@SpringBootApplication` 和 `@EnableAutoConfiguration` 永远只添加一个，一般建议选择任意一个添加到主 `@Configuration` 类上。
9. 组件导入
   1. 建议使用 `@Configuration` java 组件， `@Import` 可用来将其他组件导入，也可以使用 `@ComponentScan` 自动扫描所有的 spring 组件，包括 `@onfiguration` 配置组件
   2. 如果非要使用 xml 配置，使用 `@ImportResource`

### create an executable jar

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#getting-started-first-application-dependencies)

对于一个工程来说依赖各个 jar 包，而 java 并没有提供一个标准的方式去加载内嵌的 jar ，因此不能发布一个独立的程序会。

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
