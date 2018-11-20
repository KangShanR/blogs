---
layout: "post"
title: "maven dependency"
date: "2018-11-20 16:53"
---

# maven 依赖

> maven 会将整个项目上下所有的依赖全部集合起来而避免各个不同的模块依赖到相同的构件，出现重复。

- 注意 pom 中的 `<parent>` tag ，这个标签将各个模块的父模块引出，父模块中有的依赖，子模块中不需要引入。
