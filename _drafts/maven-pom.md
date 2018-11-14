---
layout: "post"
title: "maven POM"
date: "2018-09-27 17:22"
---

# Maven POM

> maven 基于 项目对象模型构建（POM Project Object Model）。

> 后台项目 maven 构建

### super POM

> 所有的 pom 文件都由一个父 POM 继承而来。此 POM 称为 super POM 。super POM 指定了所有的构建需要的信息，当 POM 文件未指一些必要的信息时，就会从 super POM 中去获取默认的信息。

- 要查看 super POM 信息，使用命令： mvn help:effective-pom （需要保证当前命令目录存在一个基础的 POM 文件，其中包含了一个 POM 文件所需要的四个元素，参下）

### 标签意义

> 所有的 POM 文件必须包括的元素：
- <project> 所有项目构建 POM 文件的根标签
    - <groupId>
    - <artifactId>
    - <version>

- <project>
    - <build> 构建项目需要信息
        - <sourceDirectory> 源码目录
        - <scriptSourceDirectory> 脚本源码目录
        - <testSourceDirectory> 单元测试源码目录
        - <outputDirectory> 被编译过的 class 文件存放目录
        - <testOutputDirectory> 被编译过的单元测试 class 文件目录
        - <extensions> 构建扩展
        - <resources> 项目相关资源路径列表（如：项目相关的属性文件），这资源将被打包
            - <resource>
                - <targetPath> 资源的目标路径相对于 target/classes 目录
                - <filtering> 是否使用参数值代替参数名
                - <directory> 描述存放资源的目录，该路径相对于 POM 文件
                - <includes> 包含的模式列表
                - <excludes> 排除的模式列表
        - <testResources> 单元测试相关资源路径列表
            - <testResource> 参照上面 resource 的解释对应到 单元测试相关
                - <targetPath> 资源的目标路径相对于 target/classes 目录
                - <filtering> 是否使用参数值代替参数名
                - <directory> 描述存放资源的目录，该路径相对于 POM 文件
                - <includes> 包含的模式列表
                - <excludes> 排除的模式列表
        - <pluginManagement> 子项目可以使用的插件。该配置只有在使用时才会被解析或被绑定到生命周期。给定插件的本地配置会覆盖此处的配置
            - <plugins> 插件列表
                - <plugin>
                    - <groupId> 插件在仓库里的 groupId
                    - <artifactId> 插件在仓库里的 artifactId
                    - <version> 插件版本
                    - <extensions> 是否从该插件下载 maven 扩展（例如打包和类型处理器），由于性能原因，只有在真需要下载时，该元素才被设置成 enabled。
                    - <executions>
                        - <execution> 插件执行需要的信息
                            - <id> 执行目标的 id
                            - <phase> 绑定了目标的构建生命周期阶段，如果省略，目标会被绑定到源数据里配置的默认阶段
                            - <goals> 配置执行目标
                            - <inherited> 配置是否传送到子 POM
                            - <comfiguration> 作为 DOM 对象的配置
                    - <dependencies> 插件所需要的额外依赖
                        - <dependency>
                    - <inherited>  任何配置是否要直播到子项目
                    - <configuration>
        - <plugins> 插件列表与上面 <pluginManagement> 子元素一样
            - <plugin>
        - </plugins>
    - <profiles> 在列的项目构建profile，如果被激活，会修改构建处理
        - <profile> _need update_ 根据环境参数或命令行参数激活某个构建处理： 其中的元素与外部的 project 一致
            - <activation> 自动触发profile的条件逻辑。Activation是profile的开启钥匙。profile的力量来自于它 能够在某些特定的环境中自动使用某些特定的值；这些环境通过activation元素指定。activation元素并不是激活profile的唯一方式。 _什么情况下需要激活这个 profile 呢？_
                - <activeByDefault> 默认激活
                - <jdk> 匹配的 jdk 被检测到则激活 profiles
                - <os> 匹配的 操作系统 属性被检查到则激活 profile 。属性如下：
                    - <name>
                    - <family>
                    - <arch>
                    - <version>
                - <property>  如果Maven检测到某一个属性（其值可以在POM中通过${名称}引用），其拥有对应的名称和值， Profile 就会被激活。如果值 字段 是 **空** 的，那么存在属性名称字段就会激活 profile ，否则按区分大小写方式匹配属性值字段
                    - <name>
                    - <value>
                - <file> 提供一个文件名，通过检测该文件的存在或不存在来激活profile。missing检查文件是否存在，如果不存在则激活 profile。另一方面，exists则会检查文件是否存在，如果存在则激活profile。
                    - <exists>
                    - <missing>
                - </file>
            - </activation>
            - <build> 参照外部 build 标签，所有元素一致。表示激活时使用的 build
                - <defaultGoal>
                - <resources>
                    - <resource>
                        - <targetPath />
                        - <filtering />
                        - <directory />
                        - <includes />
                        - <excludes />
                - </resources>
                - <testResources>
                    - <testResource>
                - </testResources>
                - <directory />
                - <finalName />
                - <filters />
                - <pluginManagement>
                    - <plugins>
                        - <plugin>
                            - <groupId />
                            <artifactId />
                            <version />
                            <extensions />
                            <executions>
                                <execution>
                                    <id />
                                    <phase />
                                    <goals />
                                    <inherited />
                                    <configuration />
                                </execution>
                            </executions>
                            <dependencies>
                                <!--参见dependencies/dependency元素 -->
                                <dependency>
                                    ......
                                </dependency>
                            </dependencies>
                            <goals />
                            <inherited />
                            <configuration />
                        - </plugin>
                    - </plugins>
                - </pluginManagement>
                - <plugins>
                    - <plugin>
                        - _and so on_
                    - </plugin>
                - </plugins>
            - </build>
            - <modulds>
            - <repositories>
                - <repository>
            - </repositories>
            - <dependencies>
            - <reporting>
            - <dependencyManagement>
            - <distributionManagement>
            - <properties>
        - </profile>
    - </profiles>
    - <modules> 被构建成项目的一部分
        - <module> 子模块
    - <dependencies> 项目依赖
        - <dependency>
            - <groupId> 依赖的 groupId
            - <artifactId> 依赖的 artifactId
            - <version> 依赖的 版本
            - <type> 依赖类型，默认为 jar
            - <scope> 依赖范围，项目发布过程中地帮助决定哪些构件被包括进来
            - <exclusions> 计算传递依赖时，从依赖构件列表里，列出被排除的依赖构建集。告诉 maven 只依赖指定的项目不依赖项目的依赖。 **可用于解决版本冲突问题**
                - <exclusion>
    - <dependencyManagement> 子项目默认依赖信息， **所谓默认依赖信息是指：当子项目中声明了依赖（明确指定了其 groupId 与 artifactId），而依赖其他没有指定的信息就会从这里获取（根据依赖的 groupId 与 artifactId）**
        - <dependencies>
            - <dependency>
    - <destributionManagement> 项目发布信息，有这些信息才能将网站部署到远程服务器或构件部署到远程仓库
        - <repository> 部署项目产生的构件到远程仓库需要的信息
        - <snapshotRepository> 构件快照部署信息
        - <site> 部署项目网站需要的信息
        - <downloadUrl> 项目下载 url
    - <properties> 在整个 POM 中使用，使用其名称引用起值。eg： `<java.source.version>1.8</java.source.version>`

## skills

- 新加了 maven 依赖，并依赖于内网的仓库，拉不下来包
  1. 将内网仓库 添加 到 host 中
  2. 使用 命令行 `mvn clean install` 重新构建
  3. 重启 IDE
