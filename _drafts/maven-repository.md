---
layout: "post"
title: "maven-repositories"
date: "2018-11-19 09:42"
---

# maven 仓库

> - maven 中所有的插件、依赖还包括构建成功的 jar 包（war/zip/pom 等等）都可称之谓 构件。
> - 放置这些依赖与构件的地方叫 **仓库** ，用来管理构件。

maven 仓库分为三种：
1. 本地仓库(local)
2. 中央仓库(central)
3. 远程仓库(remote)

**note:**
- maven 查找构件的顺序是依次是 local -> central -> remote 。所以，如果 本地仓库的构件版本过期了，需要删除重新从中央/远程仓库拉新版本的构件下来。
- 本地仓库可以在 settings 文件中设置 `<localRepository>`，默认在 `%USUER_HOME%/.m2/localRepository`
