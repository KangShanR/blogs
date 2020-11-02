# java overview

关于 java 的概述。[官方文档](https://docs.oracle.com/javaee/6/firstcup/doc/gkhoy.html)

## java platform

- java SE：java standard edition。java 标准版本，包括了 java 虚拟机，各类基础的 API 。
- java EE：java enterprise edition。构建于 java SE之上，提供 API 与运行时环境，用来开发、运行大型复杂安全可靠的网络应用。
- java ME：java micro edition。提供小型虚拟机与 API，用以在小型设备（如：手机）开发应用。用于小型设备应用开发的 API 常为特定的类库，因而java ME 的 API 是 java SE 所提供的 API 的子集。java ME 应用请求的服务器一般为 java EE 开发的。
- javaFX： JavaFX 平台用于创建富网络应用，这些应用常用轻量级用户交互 API 开发。JavaFX 应用通过硬件加速绘制器与媒体引擎，达到高性能、现代外观的效果。同时也能够使用高级 APIs 连接网络数据。JavaFX 应用也可作为 Java EE 服务的客户端。

## shadowing

[tutorial](https://docs.oracle.com/javase/tutorial/java/javaOO/nested.html#shadowing)

定义到内部同名变量、方法参数名会 shadows 外部的变量或参数。方法中可以使用 `this.x` 调用对象的变量，而在内部类中可以使用 `Outer.this.x` 调用。
