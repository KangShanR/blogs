---
layout: "post"
title: "maven"
date: "2018-11-20 16:53"
---
<!-- TOC -->

- [1. maven](#1-maven)
  - [1.1. 标准生命周期](#11-%e6%a0%87%e5%87%86%e7%94%9f%e5%91%bd%e5%91%a8%e6%9c%9f)
    - [1.1.1. clean lifecycle](#111-clean-lifecycle)
    - [1.1.2. defalut(build) lifecycle](#112-defalutbuild-lifecycle)
    - [1.1.3. site lifecycle](#113-site-lifecycle)
  - [1.2. maven dependency](#12-maven-dependency)
    - [1.2.1. scope 依赖范围](#121-scope-%e4%be%9d%e8%b5%96%e8%8c%83%e5%9b%b4)
      - [1.2.1.1. 依赖范围对依赖传递的影响](#1211-%e4%be%9d%e8%b5%96%e8%8c%83%e5%9b%b4%e5%af%b9%e4%be%9d%e8%b5%96%e4%bc%a0%e9%80%92%e7%9a%84%e5%bd%b1%e5%93%8d)
    - [1.2.2. dependency version conflit](#122-dependency-version-conflit)
  - [1.3. maven 插件](#13-maven-%e6%8f%92%e4%bb%b6)
    - [1.3.1. maven 常用插件](#131-maven-%e5%b8%b8%e7%94%a8%e6%8f%92%e4%bb%b6)
    - [1.3.2. note](#132-note)
  - [1.4. maven 私服](#14-maven-%e7%a7%81%e6%9c%8d)
    - [1.4.1. 发布本地包到私服](#141-%e5%8f%91%e5%b8%83%e6%9c%ac%e5%9c%b0%e5%8c%85%e5%88%b0%e7%a7%81%e6%9c%8d)
    - [1.4.2. 下载私服包](#142-%e4%b8%8b%e8%bd%bd%e7%a7%81%e6%9c%8d%e5%8c%85)
  - [1.5. questions](#15-questions)

<!-- /TOC -->

# 1. maven

> maven 会将整个项目上下所有的依赖全部集合起来而避免各个不同的模块依赖到相同的构件，出现重复。

## 1.1. 标准生命周期

maven 有三个标准生命周期：

1. clean 项目清理的处理，包括三个阶段
    1. pre-clean:执行一些需要在 clean 之前完成的工作
    2. clean 移除上一次构建生成的文件
    3. post-clean 执行一些需要在 clean 之后完成的工作
2. default/build 项目部署的处理，在此个周期中主要的阶段有：
    1. validate 验证项目是否正确，必有信息是否可用
    2. compile 将源代码进行编译，不会编译测试代码
    3. test 运行单元测试框架
    4. package 打包（jar/war），定义在 pom 中的提及到的包
    5. verify 验证集成测试结果
    6. install 安装打包的项目到本地仓库，以供其他项目使用
    7. deploy 拷贝项目工程包到远程仓库，以共享其他开发人员和工程
3. site 项目站点文档创建的处理

### 1.1.1. clean lifecycle

> 项目清理的处理

包括三个阶段：

1. pre-clean:执行一些需要在 clean 之前完成的工作
2. clean 移除上一次构建生成的文件
3. post-clean 执行一些需要在 clean 之后完成的工作

**note:**
clean 并不会将 localRepository 中 installed 的包清除，所以本地的包过期后需要手动删除。

### 1.1.2. defalut(build) lifecycle

> 项目构建生命周期（也是项目构建核心生命周期所以叫 defalut/build）

build 生命周期的 phase 有：

1. validate 检查工程配置是否正确，完成构建过程的所有必要信息是否能够获取到。
2. initialize 初始化构建状态，例如设置属性。
3. generate-sources 生成编译阶段需要包含的任何源码文件。
4. process-sources 处理源代码，例如，过滤任何值（filter any value）。
5. generate-resources 生成工程包中需要包含的资源文件。
6. process-resources 拷贝和处理资源文件到目的目录中，为打包阶段做准备。
7. compile 编译工程源码。
8. process-classes 处理编译生成的文件，例如 Java Class 字节码的加强和优化。
9. generate-test-sources 生成编译阶段需要包含的任何测试源代码。
10. process-test-sources 处理测试源代码，例如，过滤任何值（filter any values)。
11. test-compile 编译测试源代码到测试目的目录。
12. process-test-classes 处理测试代码文件编译后生成的文件。
13. test 使用适当的单元测试框架（例如JUnit）运行测试。
14. prepare-package 在真正打包之前，为准备打包执行任何必要的操作。
15. package 获取编译后的代码，并按照可发布的格式进行打包，例如 JAR、WAR 或者 EAR 文件。
16. pre-integration-test 在集成测试执行之前，执行所需的操作。例如，设置所需的环境变量。
17. integration-test 处理和部署必须的工程包到集成测试能够运行的环境中。
18. post-integration-test 在集成测试被执行后执行必要的操作。例如，清理环境。
19. verify 运行检查操作来验证工程包是有效的，并满足质量要求。
20. install 安装工程包到本地仓库中，该仓库可以作为本地其他工程的依赖。
21. deploy 拷贝最终的工程包到远程仓库中，以共享给其他开发人员和工程。_对应到 pom 文件 distributionManagement - repository 标签的内容_

### 1.1.3. site lifecycle

> site 插件用来创建新的报告文档、部署站点等。

site 生命周期的 phase 有：

1. pre-site：执行一些需要在生成站点文档之前完成的工作
2. site：生成项目的站点文档
3. post-site： 执行一些需要在生成站点文档之后完成的工作，并且为部署做准备
4. site-deploy：将生成的站点文档部署到特定的服务器上 _对应到 pom 文件 distributionManagement - site 标签的内容_

---
note： **运行任何生命周期中的任何阶段之一，其同生命周期内的之前的阶段都会被被运行** 。也就是说：在 clean lifecycle 中如果执行 post-clean 命令，则前面的 pre-clean 与 clean 都会被执行到。同时，在父项目中执行此命令，在子项目中也同样会执行同样的命令。

## 1.2. maven dependency

- 注意 pom 中的 `<parent>` tag ，这个标签将各个模块的父模块引出，父模块中有的依赖，子模块中不需要引入。
- groupId 、 artifactId、 version 三个属性用于指定 jar 包坐标，用于在仓库中查找需要的 jar 包；
- 如果本地仓库找不到会去公司私服找，没有私服或私服没有指定的包，将去远程仓库找。

### 1.2.1. scope 依赖范围

maven 共有 6 种 scope ，用以指定依赖的可及性，针对不同 build 任务修改 classpath（？）。[reference](https://www.baeldung.com/maven-dependency-scopes)

1. compile 默认范围（当不显式指定 scope 时，使用该范围）。依赖在构建中会被传递到项目路径中，依赖同时也是可传递的。
2. provided 依赖在运行时为 jdk 或其他容器所提供的，比如：Servlet/jsp。这些依赖在编译阶段与测试阶段是可用的，不可被传递。
3. runtime 依赖在运行时需要，但在项目源码编译时不被需要。eg：JDBC driver （只需要在运行时使用反射加载驱动，不需要被编译？）。
4. test 指定只需要在测试阶段使用的依赖，这种依赖不需要参与到应用运行阶段。比如：Junit。
5. system 与 provided 类似，system 指定此依赖已经在 system 中已经存在不用再提供，引外 system 类依赖需要指定此依赖在系统中的位置 `<systemPath>${project.basedir}/libs/custom-dependency-1.3.2.jar</systemPath>`。
6. import 只在 type 为 pom `<type>pom</type>` 的依赖中出现，表明此依赖应该被其 POM 中所有有效的依赖所替代。

#### 1.2.1.1. 依赖范围对依赖传递的影响

|直接依赖\传递依赖|compile|provide|runtime|test|
|--|--|--|--|--|
|compile|compile|-|runtime|-|
|provided|provided|provided|provided|-|
|runtime|runtime|-|runtime|-|
|test|test|-|test|-|

note ：*横行表示传递的依赖范围，纵列表示自己依赖范围。 `-` 表示结果为无依赖。*

### 1.2.2. dependency version conflit

不同依赖传递必然会出现依赖版本冲突，不同包之间的相同依赖版本不一。

这种情况下 maven 的解决原则：

1. 调节原则
   1. 路径就近原则
   2. 第一声明优先原则
2. 排除原则，在依赖中写入排除标签，当冲突的两个包中其中一个被排除后，其他的自动获取到

   ```xml
   <dependency>
        <groupId>org.apache.struts</groupId>
        <artifactId>struts2-spring-plugin</artifactId>
        <version>2.1.5</version>
        <exclusions>
            <exclusion>
                <groupId><org.springframeword></groupId>
                <artifactId>spring-beans</artifactId>
                <!-- 不需要写 version -->
            </exclusion>
        </exclusions>
    </dependency>
   ```

3. 版本锁定原则，使用依赖管理对依赖进行指定，指定后各个冲突的包只要符合了依赖管理的坐标即为锁定的版本（可添加变量统一版本）。同时使用版本锁定后，在 `<dependency>` 中可以不用再指定 `<version>` 了。

   ```xml
   <properties>
        <spring.version>4.2.4.RELEASE</spring.version>
    <properties>
   <dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>...</groupId>
            <artifactId>...</artifactId>
            <!-- EL 表达式写法 -->
            <version>${spring.version}</version>
        </dependency>
    </dependencies>
    </dependencyManagement>
   ```

其他的属性值可在不同的标签里指定后，通过类似 el 表达式获取时的规则：直接取标签，标签间使用 `.` 分隔。eg:

```xml
<build>
        <finalName>basic-struts</finalName>
        <plugins>
            <plugin>
                <groupId>org.eclipse.jetty</groupId>
                <artifactId>jetty-maven-plugin</artifactId>
                <version>9.4.7.v20170914</version>
                <configuration>
                    <webApp>
                        <contextPath>/${project.build.finalName}</contextPath>
                    </webApp>
                    <stopKey>CTRL+C</stopKey>
                    <stopPort>8999</stopPort>
                    <scanIntervalSeconds>10</scanIntervalSeconds>
                    <scanTargets>
                        <scanTarget>src/main/webapp/WEB-INF/web.xml</scanTarget>
                    </scanTargets>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

## 1.3. maven 插件

> maven 的所有命令执行都是在执行相应的插件。

**note:**

- 一个 maven 插件有多个目标 goal
- maven 插件执行命令行: `mvn [plugin-name]:[goal-name]`
- maven 的三个标准生命周期中每一个都包含了多个 phase ，每个 phase 都是 maven 提供的统一接口，每个接口的实现都是通过插件来实现的。

### 1.3.1. maven 常用插件

> maven 插件有两中类型：
>
> - build plugins 构建插件，在构建时执行。在 pom 中 `plugins` 标签中配置。
> - reporting plugins 网站生成过程中执行。在 pom 中 `distributionManagement` tag 中配置

**maven 常用插件**

- clean 构建之后删除目标文件
- compiler 编译 java 文件
- surefile 运行单元测试，创建测试报告
- jar 从当前工程构建 jar 文件
- war 从当前工程构建 war 文件
- javadoc 为工程生成 JavaDoc
- antrun 可以在构建任何一个 phase 运行一个 ant 任务的集合。

### 1.3.2. note

- 插件是在 pom.xml 中使用 plugins 元素定义的。
- 每个插件可以有多个目标。
- 你可以定义阶段，插件会使用它的 phase 元素开始处理。我们已经使用了 clean 阶段。
- 你可以通过绑定到插件的目标的方式来配置要执行的任务。我们已经绑定了 echo 任务到 maven-antrun-plugin 的 run 目标。
- 就是这样，Maven 将处理剩下的事情。它将下载本地仓库中获取不到的插件，并开始处理。也就是说插件也会优先使用本地仓库里面的，如果本地仓库插件有问题同样可以将其删除再让 maven 从远程仓库拉取。

## 1.4. maven 私服

maven 私服是公司内搭建的服务，使用 nexus 。

### 1.4.1. 发布本地包到私服

1. 修改 maven setting 文件，添加上传账号与密码

   ```xml
   <server>
      <id>releases</id>
      <username>admin</username>
      <password>admin123</password>
    </server>
	<server>
      <id>snapshots</id>
      <username>admin</username>
      <password>admin123</password>
    </server>
   ```

2. 配置需要发布的包 pom ，指定私服位置与仓库

    ```xml
    <distributionManagement>
        <repository>
            <id>releases</id>
        <url>http://localhost:8081/nexus/content/repositories/releases/</url>
        </repository>
        <snapshotRepository>
            <id>snapshots</id>
        <url>http://localhost:8081/nexus/content/repositories/snapshots/</url>
        </snapshotRepository>
    </distributionManagement>
    ```

3. 执行 deploy 发布到指定私服 `mvn deploy`

### 1.4.2. 下载私服包

1. 在 maven setting 中添加/修改 profile

    ```xml
    <profile>
	<!--profile的id-->
    <id>dev</id>
    <repositories>
      <repository>  
		<!--仓库id，repositories可以配置多个仓库，保证id不重复-->
        <id>nexus</id>
		<!--仓库地址，即nexus仓库组的地址-->
        <url>http://localhost:8081/nexus/content/groups/public/</url>
		<!--是否下载releases构件-->
        <releases>
          <enabled>true</enabled>
        </releases>
		<!--是否下载snapshots构件-->
        <snapshots>
          <enabled>true</enabled>
        </snapshots>
      </repository>
    </repositories>  
	 <pluginRepositories>  
    	<!-- 插件仓库，maven的运行依赖插件，也需要从私服下载插件 -->
        <pluginRepository>  
        	<!-- 插件仓库的id不允许重复，如果重复后边配置会覆盖前边 -->
            <id>public</id>  
            <name>Public Repositories</name>  
            <url>http://localhost:8081/nexus/content/groups/public/</url>  
        </pluginRepository>  
    </pluginRepositories>
    </profile>
    ```

2. maven setting 中激活 profile

    ```xml
    <activeProfiles>
        <activeProfile>dev</activeProfile>
    </activeProfiles>
    ```

3. 更新依赖 `maven clean update`

## 1.5. questions

1. idea 中出出某个 module 在 maven 显示为灰色，且其他包始终引用不了此包资源。尝试使用 mvn clean package 都无效。
   1. 原因：idea maven 中已经将此 module 的 pom.xml 给 ignored 掉了，进入 setting-maven-Ignored files 中取消即可。
2. install module dao 到 service 时报错找不到 dao 中的 parent pom （Failed to read artifact descriptor for com.kang:springdemo-dao:jar），单独 install dao 执行成功。再直接构建 parent pom 工程，依然出错（maven.plugins:maven-surefire-plugin Error creating properties files for forking; nested exception is java.io.IOException: 系统找不到指定的路径。）。
   1. 解决：[reference](https://blog.csdn.net/prstaxy/article/details/46862757) 参照此见面时，在 parent pom 中添加 org.apache.maven.surefire plugin 同时解决。也解决了整个项目运行后不能注册 spring mvc 的问题。
