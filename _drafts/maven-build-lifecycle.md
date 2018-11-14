---
layout: "post"
title: "maven-lifecycle"
date: "2018-11-14 15:38"
---

# maven 构建生命周期

> maven 生命周期

## 标准生命周期

maven 有三个标准生命周期：
1. clean 项目清理的处理，包括三个阶段
    1. pre-clean:执行一些需要在 clean 之前完成的工作
    2. clean 移除上一次构建生成的文件
    3. post-clean 执行一些需要在 clean 之后完成的工作
2. default/build 项目部署的处理，在此个周期中，需要进行的阶段大致有：
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

包括三个阶段
    1. pre-clean:执行一些需要在 clean 之前完成的工作
    2. clean 移除上一次构建生成的文件
    3. post-clean 执行一些需要在 clean 之后完成的工作

**note：** 运行这三个阶段，其之前的阶段都会被被运行。也就是说：如果执行 post-clean 命令，则前面的 pre-clean 与 clean 都会被执行到。
