---
layout: "post"
title: "maven-plugin"
date: "2018-11-19 11:06"
---

# maven 插件

> maven 的所有命令执行都是在执行相应的插件。

**note:**
- 一个 maven 插件有多个目标 goal
- maven 插件执行命令行: `mvn [plugin-name]:[goal-name]`
- maven 的三个标准生命周期中每一个都包含了多个 phase ，每个 phase 都是 maven 提供的统一接口，每个接口的实现都是通过插件来实现的。

## maven 常用插件

> maven 插件有两中类型：
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


## note

- 插件是在 pom.xml 中使用 plugins 元素定义的。
- 每个插件可以有多个目标。
- 你可以定义阶段，插件会使用它的 phase 元素开始处理。我们已经使用了 clean 阶段。
- 你可以通过绑定到插件的目标的方式来配置要执行的任务。我们已经绑定了 echo 任务到 maven-antrun-plugin 的 run 目标。
- 就是这样，Maven 将处理剩下的事情。它将下载本地仓库中获取不到的插件，并开始处理。也就是说插件也会优先使用本地仓库里面的，如果本地仓库插件有问题同样可以将其删除再让 maven 从远程仓库拉取。
