---
layout: "post"
title: "maven-profile"
date: "2018-11-16 10:53"
---

# maven profile 的使用

> maven 项目构建中，profile 的使用

note: 在测试中一直使用 antrun 插件是因为此插件可以绑定各个生命周期的各个阶段，同时其中的标签可以不写代码实现输出信息/复制文件。

## 激活 profile 数据的方式

1. 直接使用命令行指定 profile 的 id `mvn test -Ppid` 指定生命周期后 `-P` 就指定了 profile 的 id 。
2. 能过设置 maven settings 文件来设置默认激活 profile。可以选择用户配置在用户目录中 ".m2/settings.xml" ，在其中增加
            <activeProfiles>
                <activeProfile>dev</activeProfile>
            </activeProfiles>
    1. 这种方法执行 maven 生命周期时就不用指定 profile id 了。直接执行 mvn test 即会被 antrun 插件检测到并执行自己 tasks。
    2. 同样可以直接在 maven 的配置文件 settings 中去配置此项，不同的是在 maven 中去配置时是 **全局配置**。
3. 通过 <activation> tag 来激活 profile
    1. 配置环境变量
                <activation>
                    <name>env</name>
                    <value>dev</value>
                </activation>
        1. 激活时依然要指定环境参数 `mvn test -Denv=dev`
    2. 配置操作系统
                <activation>
                    <os>
                        <name>Windows 10</name>
                        <family>Windows</family>
                        <arch>x86</arch>
                        <version>5.1</version>
                    </os>
                </actiovation>
4. 通过文件的存在或缺失来激活profile
            <activation>
                <file>
                    <missing>*.xml</missing>
                </file>
            </activation>
    note: 执行命令时直接 mvn test
