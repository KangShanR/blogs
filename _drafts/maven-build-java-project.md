---
layout: "post"
title: "maven-build-java_project"
date: "2018-11-19 18:31"
---

# maven 构建 java 项目


命令行：`mvn archetype:generate -DgroupId=com.companyname.bank -DartifactId=consumerBanking -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false`

- `-DgourpId`: 组织名，公司网址的反写 + 项目名称
- `-DartifactId`: 项目名-模块名
- `-DarchetypeArtifactId`: 指定 ArchetypeId，maven-archetype-quickstart，创建一个简单的 Java 应用
- `-DinteractiveMode`: 是否使用交互模式

也可以不指定上面参数，而直接使用 `mvn archetype:generate` 命令来执行，此命令执行后，maven 会出现多个指定问题让我们指定项目的各个信息。如果不指定（直接按 enter ） 则会使用默认的信息（apche 项目名与各种 beta 版本号）。
