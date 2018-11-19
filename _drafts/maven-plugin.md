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
> - reporting plugins 网站生成过程中执行。在 pom 中 `reportingManagement` tag 中配置


**maven 常用插件**
-
