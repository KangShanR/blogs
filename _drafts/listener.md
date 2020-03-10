# listener

listener 技术

## 概述

### 关于各个 listener 的注册

前 6 种 listener 注册都需要注册在 web.xml 中，另外两种在 javabean 实现相关接口后在相关事件进行时自动注册（绑定解绑事件、钝化活化事件与 javabean 息息相关，不需要在手动在 web.xml 配置中注册）。

### 分类

在 Tomcat listener 共有 6 + 2 种。其中 6 种与三个域（ServletContext，HttpSession，ServletRequest）相关，每个域都有两个listner: create destroy （包括了：创建销毁）/ attribute（包括了 add/replace/remove）。另外 2 种分别是：HttpSession 的钝化（序列化到磁盘，需要 javabean 实现 Initializable 接口）活化（被激活）事件 和 session value 的绑定解绑事件。

- 创建销毁事件类
  - ServletContextListener 监听 ServletContext 被创建与销毁事件
  - HttpSessionListener 监听 HttpSession 被创建与销毁事件
  - ServletRequestListener 监听 ServletRequestListener 创建与销毁事件
- 属性事件类
  - ServletContextAttributeListener 监听 ServletContext 属性的创建、修改、移除事件
  - HttpSessionAttributeListener 监听 HttpSession 属性的创建、修改、移除事件
  - ServletRequestAttributeListener 监听 ServletRequest 属性的创建、修改、移除事件
- javabean 类
  - 只需要 javabean 在被设置入 attribute 之前实现指定接口，在 Tomcat 容器进行 set 前会对操作的 bean 对象进行检查是否实现了 指定接口。
  - HttpSessionActivationListener 监听 HttpSession 钝化（序列化到磁盘，路径可通过 /META-INF/context.xml 指定）与活化（从磁盘激活到内存容器） bean 对象（bean 对象同时要实现 Serialiazable 接口）事件
  - HttpSessionBindingListener 监听 HttpSession 绑定与解绑 bean 对象事件

HttpSessionActivationListener 监听器配置 序列化参数：

```xml
<Context>
  <!-- maxIdleSwap:session中的对象多长时间不使用就钝化 -->
  <!-- directory:钝化后的对象的文件写到磁盘的哪个目录下  配置钝化的对象文件在 work/catalina/localhost/钝化文件 -->
  <Manager className="org.apache.catalina.session.PersistentManager" maxIdleSwap="1">
    <Store className="org.apache.catalina.session.FileStore" directory="project path" />
  </Manager>
</Context>
```
