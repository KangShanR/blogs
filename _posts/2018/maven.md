---
layout: "post"
title: "maven"
date: "2018-11-20 16:53"
---

# maven

> maven 会将整个项目上下所有的依赖全部集合起来而避免各个不同的模块依赖到相同的构件，出现重复。

## 标准生命周期

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

### clean lifecycle

> 项目清理的处理

包括三个阶段：

1. pre-clean:执行一些需要在 clean 之前完成的工作
2. clean 移除上一次构建生成的文件
3. post-clean 执行一些需要在 clean 之后完成的工作

**note:**
clean 并不会将 localRepository 中 installed 的包清除，所以本地的包过期后需要手动删除。

### defalut(build) lifecycle

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

### site lifecycle

> site 插件用来创建新的报告文档、部署站点等。

site 生命周期的 phase 有：

1. pre-site：执行一些需要在生成站点文档之前完成的工作
2. site：生成项目的站点文档
3. post-site： 执行一些需要在生成站点文档之后完成的工作，并且为部署做准备
4. site-deploy：将生成的站点文档部署到特定的服务器上 _对应到 pom 文件 distributionManagement - site 标签的内容_

---
note： **运行任何生命周期中的任何阶段之一，其同生命周期内的之前的阶段都会被被运行** 。也就是说：在 clean lifecycle 中如果执行 post-clean 命令，则前面的 pre-clean 与 clean 都会被执行到。同时，在父项目中执行此命令，在子项目中也同样会执行同样的命令。

## maven dependency

- 注意 pom 中的 `<parent>` tag ，这个标签将各个模块的父模块引出，父模块中有的依赖，子模块中不需要引入。
- groupId 、 artifactId、 version 三个属性用于指定 jar 包坐标，用于在仓库中查找需要的 jar 包；
- 如果本地仓库找不到会去公司私服找，没有私服或私服没有指定的包，将去远程仓库找。

### scope 依赖范围

maven 共有 6 种 scope ，用以指定依赖的可及性，针对不同 build 任务修改 classpath（？）。[reference](https://www.baeldung.com/maven-dependency-scopes)

1. compile 默认范围（当不显式指定 scope 时，使用该范围）。依赖在构建中会被传递到项目路径中，依赖同时也是可传递的。
2. provided 依赖在运行时为 jdk 或其他容器所提供的，比如：Servlet/jsp。这些依赖在编译阶段与测试阶段是可用的，不可被传递。
3. runtime 依赖在运行时需要，但在项目源码编译时不被需要。eg：JDBC driver （只需要在运行时使用反射加载驱动，不需要被编译？）。
4. test 指定只需要在测试阶段使用的依赖，这种依赖不需要参与到应用运行阶段。比如：Junit。
5. system 与 provided 类似，system 指定此依赖已经在 system 中已经存在不用再提供，引外 system 类依赖需要指定此依赖在系统中的位置 `<systemPath>${project.basedir}/libs/custom-dependency-1.3.2.jar</systemPath>`。
6. import 只在 type 为 pom `<type>pom</type>` 的依赖中出现，表明此依赖应该被其 POM 中所有有效的依赖所替代。

## maven 插件

> maven 的所有命令执行都是在执行相应的插件。

**note:**

- 一个 maven 插件有多个目标 goal
- maven 插件执行命令行: `mvn [plugin-name]:[goal-name]`
- maven 的三个标准生命周期中每一个都包含了多个 phase ，每个 phase 都是 maven 提供的统一接口，每个接口的实现都是通过插件来实现的。

### maven 常用插件

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

### note

- 插件是在 pom.xml 中使用 plugins 元素定义的。
- 每个插件可以有多个目标。
- 你可以定义阶段，插件会使用它的 phase 元素开始处理。我们已经使用了 clean 阶段。
- 你可以通过绑定到插件的目标的方式来配置要执行的任务。我们已经绑定了 echo 任务到 maven-antrun-plugin 的 run 目标。
- 就是这样，Maven 将处理剩下的事情。它将下载本地仓库中获取不到的插件，并开始处理。也就是说插件也会优先使用本地仓库里面的，如果本地仓库插件有问题同样可以将其删除再让 maven 从远程仓库拉取。
