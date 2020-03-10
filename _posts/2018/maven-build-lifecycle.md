---
layout: "post"
title: "maven-lifecycle"
date: "2018-11-14 15:38"
---

> maven 生命周期

## 标准生命周期

maven 有三个标准生命周期：

1. clean 项目清理的处理，包括三个阶段
    1. pre-clean:执行一些需要在 clean 之前完成的工作
    2. clean 移除上一次构建生成的文件
    3. post-clean 执行一些需要在 clean 之后完成的工作
2. default/build 项目部署的处理，在此个周期中主要的阶段有：
    1. validate 验证项目是否正确，必有信息是否可用
    2. compile 将源代码进行编译
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
