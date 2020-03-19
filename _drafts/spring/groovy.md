# groovy

[reference](https://groovy-lang.org/differences.html)

## 概述

groovy 基于 JVM 运行，同样需要编译，但编译后与 java 编译后不一样。

### groovy 与 java 不同之处

1. groovy 不用显式 import 包；
2. groovy 方法调用在运行时决定变量类型，而 java 是静态信息类型，在编译时根据其定义的类型决定方法调用。
3. 数组初始化与闭包
   1. groovy 数组初始化： `new int[] ints = [1,2,3];`
   2. java 数组初始化： `new int[] ints = {1,2,3};`
   3. 在 groovy 中使用大括号是闭包：`Runnable r = {println "hello"}`
      1. groovy 取代 java8 中的 lambda 的就是闭包。
      2. java lambda expression: ``Runnable b = () -> System.out.println("run");`
4. to be continued
