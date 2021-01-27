---
title: "Tomcat"
date: "2017-11-17 00:09"
tag: [tomcat,WebProject,java]
categaries: programming
---

## 在 ide 上部署 web 项目

> server container 都是 apache tomcat

### idea 上部署与 eclipse 上部署的区别

> [参考](https://blog.csdn.net/Victor_Cindy1/article/details/72680553)
> [参考](https://blog.csdn.net/qq_33442160/article/details/81347319)

概要：

- idea 中 tomcat 部署的配置都被 copied 到了 {user.home}/.IntelliJIdea2018.2\system\tomcat\Unnamed_test\conf\Catalina\localhost/...ROOT.xml 中。eclipse 直接放到 tomcat conf 中
- idea 在部署时将包放在 out 文件夹中，而 eclipse 放在了 tomcat 的 webapps 中

### 将项目路径最简化

- eclipse ：在 Tomcat 页面选择 Modules 对 web modules 进行编辑，就可以定制在这个服务器上各个 Module 的访问路径了。可以将路径简化直没有，直接访问到 `ip:portNumber` 而到项目中。同时，在 Tomcat 页面中选择 Overview 选项，可以定制 Tomcat 的各个配置。比如：启动、终止时间、端口号、主机名、部署动作等；
- idea : Run/Debug configuration -> deployment -> application context 配置成最简单的 `/` 即可。如果配置成其他项目名，那么部署后需要在 hostnale:portnum 后中上此处配置的路径。

### 在 idea 上部署 web 项目

[reference](https://www.cnblogs.com/leap/p/6251576.html)

- 本地上将一个 web project 转成 maven 管理时出现了启动时 tomcat 找不到本地的 class 文件: ClassNotFoundException 。
    - 原因： idea 没有将项目文件复制到 tomcat 的 webapps 中（eclipse 是这么做的），默认是在各个项目的 out 文件夹中，各个项目的 tomcat 配置放在用户目录 .idea/../tomcat/ROOT.xml ，这个文件指定了项目文件部署的位置。
    - 解决办法：在 project structure/artifits/output layout 中将 maven 引入的包拖到 META-INF/lib 中。[reference](https://blog.csdn.net/iwts_24/article/details/84916867?depth_1-utm_source=distribute.pc_relevant.none-task&utm_source=distribute.pc_relevant.none-task)

### idea 上将项目文件夹识别为 web

[reference](https://stackoverflow.com/questions/11652162/how-in-intellij-idea-to-convert-simple-folder-to-web-folder)
