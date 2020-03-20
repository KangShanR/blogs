# spring boot

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#getting-started-scoop-cli-installation)

安装 spring CLI(command line interface)，后再运行本地 groovy 文件。

## springboot 的简化处理

1. 将所有依赖都写入其基本配置中，最大简化其配置；
2. 自动扫描，一个注解自动识别所有的需要扫描的包与 bean；

### create an executable jar

[reference](https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#getting-started-first-application-dependencies)

对于一个工程来说依赖各个 jar 包，而 java 并没有提供一个标准的方式去加载内嵌的 jar ，因此不能发布一个独立的程序会。

为此，很多开发者的解决方案是创建一个 'uber' jar，所谓 uber jar 是指将应用所有的依赖的 class 文件整合成一个单独的包。这样做的问题在于：1、 难以查看依赖库；2、不同 jar 包中要是存在同名的 class 就会出现问题。

spring boot 使用了不同的方式达到直接使用内嵌包。

- 查看 jar 包内打包文件 `mvn tvf jar_file`
