---
layout: "post"
title: "maven-build-project"
date: "2018-11-19 18:31"
---

# maven 构建项目

**构建普通 java 项目**：
命令行：`mvn archetype:generate -DgroupId=com.companyname.bank -DartifactId=consumerBanking -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`

- `-DgourpId`: 组织名，公司网址的反写 + 项目名称
- `-DartifactId`: 项目名-模块名
- `-DarchetypeArtifactId`: 指定 ArchetypeId，maven-archetype-quickstart，创建一个简单的 Java 应用
- `-DinteractiveMode`: 是否使用交互模式

也可以不指定上面参数，而直接使用 `mvn archetype:generate` 命令来执行，此命令执行后，maven 会出现多个指定问题让我们指定项目的各个信息。如果不指定（直接按 enter ） 则会使用默认的信息（apche 项目名与各种 beta 版本号）。


**构建 web 项目**：
`mvn archetype:generate -DgroupId=com.companyname.bank -DartifactId=consumerBanking -DarchetypeArtifactId=maven-archetype-webapp -DinteractiveMode=false`

注意这儿构建项目命令与上面项目的不同在于其 `archetypeArtifactId` 值为： `maven-archetype-webapp`。如果不指定这些参数，那么在只单纯执行 `mvn archtype:generate` 命令后指定其 DarchetypeArtifactId
