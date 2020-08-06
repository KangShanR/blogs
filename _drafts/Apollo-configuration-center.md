---
layout: "post"
date: "2018-10-31 17:15"
tag: "Apollo"
---

# Apollo configuration center

> Apollo 配置中心的使用 [quick-start wiki](https://github.com/ctripcorp/apollo/wiki/Quick-Start)

## java 项目中使用 Apollo 客户端列表信息

> 在使用中，客户端会将远程配置数据拉到本地来，防止断网类异常情况时不能获取到配置数据。本地路径：

- Mac/Linux: /opt/data/{appId}/config-cache
- Windows: C:\opt\data\{appId}\config-cache

**note:**

- 在本地配置并启动好 apollo 需要的数据库后，启动 demo.sh 脚本来启动 apollo service 与 config 服务。使用 命令行 `/path/demo.sh start` 后，控制台窗口很快消失，不知道是否已经启动好。再使用 `netstat -ano|findstr 8070|8090|8080` 检查，发现端口并未使用，说明并未启动好服务。这时 _解决办法_ 是：先 cd 到脚本所在目录再进行执行脚本命令。再检查，执行成功。

## 分布式部署 apollo 服务

[部署指南]( https://github.com/ctripcorp/apollo/wiki/%E5%88%86%E5%B8%83%E5%BC%8F%E9%83%A8%E7%BD%B2%E6%8C%87%E5%8D%97#2121-%E4%BB%8E%E5%88%AB%E7%9A%84%E7%8E%AF%E5%A2%83%E5%AF%BC%E5%85%A5apolloconfigdb%E7%9A%84%E9%A1%B9%E7%9B%AE%E6%95%B0%E6%8D%AE)

使用源码编译、打包:

1. 使用 mvn 命令打包在 target 文件夹中后，将压缩包拷到服务器上解压，再使用脚本命令依次执行相关命令而启动服务。

`./build.sh` 脚本会将会依次打包 apollo-configservice, apollo-adminservice, apollo-portal

注：由于ApolloConfigDB在每个环境都有部署，所以对不同环境的 config-service 和 admin-service 需要使用不同的数据库连接信息打不同的包， **portal只需要打一次包即可**

## Apollo 配置中心设计

[reference](https://github.com/ctripcorp/apollo/wiki/Apollo%E9%85%8D%E7%BD%AE%E4%B8%AD%E5%BF%83%E8%AE%BE%E8%AE%A1)

### 与spring 集成

spring 中若其配置文件 application.properties 对同一个key 有多个 value 的配置，将会取用第一个生效，所以在集成 apollo 时，其实现是在 spring 启动时只需要将其 application.properties 文件前面追加各个拉取的配置信息，从而达到覆盖原有的配置文件。

### 灰度发布

> 可以实现将部分配置发布于部分 ip ，观察确认程序正常运行后再切换到全量发布于所有服务机器。

## namespace

> 在 apollo 中使用 namespace 实现共用配置

namespace 有三种类型：public private inheritance。顾名思义，public 可以为外部应用所用，而 private 只能为自己应用所用，而 inheritance 可以实现继承其他 public namespace 中配置并覆盖其中不同的配置。

所以可以利用 inheritance 特性实现设置公有 namespace 再配以不同的继承 namespace 差异化定制配置。

## 利用 Apollo 配置实现动态配置定时任务 cron 表达式

理论上，只需要在 Spring 启动阶段将定义任务注入到指定容器里，在应用运行期间监听相关的配置，发生变化则将容器中的任务实时更新即可。

## Apollo 实现原理

大致流程：从 @EnableApolloConfig 注解开始，为兼容老版本 Spring 不能 Import BeanDefinitionPostProcessor 使用 `ImportBeanDefinitionRegistrar` 将各个核心处理器注入到 Spring 容器。其中这些处理器在 Spring 启动阶段，将所有使用点位符注解 `@Value` 的 bean 都统一构造一个 SpringValue 对象（其中写入了 bean/(method|field)/key/ 数据）写入一个 map 容器中 `com.ctrip.framework.apollo.spring.property.SpringValueRegistry#registry`。此后每次更新都对此容器中数据进行更新。

**问题** ：每次更新 Apollo 配置后，其取到了相应的数据后，只是更新的了 SpringValue 值，而其 Spring Bean 是怎么做到实时更新属性值的？直接容器 refresh 不现实。

answer: 在每个 SpringValue 更新时，其所做的工作除更新一个 value  值外，还会调用此 SpringValue 中的 bean 的 Method 或 Field.set(bean, newVal) 。这一步所做的事就是将新的值放在其所在的 bean 中的 Field 或 Method 中去。`com.ctrip.framework.apollo.spring.property.SpringValue#update`

`AutoUpdateConfigChangeListener` 中 `com.ctrip.framework.apollo.spring.property.AutoUpdateConfigChangeListener#shouldTriggerAutoUpdate` 方法(line 85)判定逻辑也没有看明白，为什么会出现 Environment 中的值与新的配置值相等时会是触发更新的结果？
