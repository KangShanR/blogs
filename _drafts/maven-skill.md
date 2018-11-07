---
layout: "post"
title: "maven skill"
date: "2018-09-27 17:22"
---

# Maven struct

> maven 基于 项目对象模型构建（POM Project Object Model）。

> 后台项目 maven 构建

## Questions

- 新加了 maven 依赖，并依赖于内网的仓库，拉不下来包
  1. 将内网仓库 添加 到 host 中
  2. 使用 命令行 `mvn clean install` 重新构建
  3. 重启 IDE
