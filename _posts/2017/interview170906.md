---
title: 面试心得
date: 2017-09-06 12:54:30
categories: job
tags: [programming,java,job,oracle]
keywords: 
description: 
---

> 今天参加了一场面试，面试过程中，发现的问题：当表达时会不自觉的手部动作幅度过大，表现得过于浮夸。这一点在今后的面试中需要注意。另外，面试前过于紧张，以致于自我介绍时没有把特色表现出来，整个过程需要前面一段时间表达才能进入状态。进入状态后表现得更好，但这样找感觉的做法是不妥的，因为不知道会遇到什么样的面试过程，这个感觉不能够很准确地把握好，这个得多练习这种中长时间的发言，平常应该多把握这样发言的机会。

<!--more-->

- 另外一点，在面试过程中特别是在发言过程中有点过于逃避面试官的眼神，这一点在进入状态时，应该要微笑面对面试官，很真诚地给出自己的最聪明的答案。
- 同时一起群面的人说我在面试时，没有尽量地多说，有一点像挤牙膏的感觉，面试官问一点我说一点，没有把面试官当作一个很尊重的人。从这一点我想到了，当时面试的时候面试官在主动给我挖坑，我并没有很积极地配合他绕过这些坑，在看到明显地坑在面前时，内心是慌乱的，没有冷静地绕过去，给他一个漂亮地回答。但我感觉在面试官面前不应该说过多的话，应该把最精准的话表达出来，达到表现自己精练准确的目的就够了，但这也要当时当地保持冷静的头脑，积极地思考。

## 面试题

- 递归求6！。
- 截取字符串：用户输入一个字符串和需要截取的字节数，返回截取后的字符串，注意中文一定要截取完整，如:（"人ABC",3）,截取的结果是："人A"，又如:（"人abc们de"，6），结果应该是："人abc"。

```java
public static void main(String[] args) {
    int byteNumber =0, strIndex =0,endIndex = 5;
    String str = "要df地sd dsfg/.在家 城",strRes = "";
    char charStr;
    for(strIndex=0;byteNumber<=endIndex;strIndex++){
        charStr = str.charAt(strIndex);
        if((int)charStr<128)
            byteNumber++;
        else
            byteNumber+=2;
        if(byteNumber<=endIndex)
            strRes+=charStr;
    }
    System.out.println(strRes);
}
```

- Oracle 数据库的事务隔离级别：
  - 先引入三个概念：
    - 幻读：事务 T1 读取一条 where 条件语句，T2 插入一行也符合的，这时 T1 再次查询可以看到新数据，这叫幻想读
    - 不可重复读：T1 读取，T2 修改了数据，T1 再次读时就讲到修改过的数据，这叫不可重复读
    - 脏读：T1 更新了数据，但未提交，这时 T2 读取更新后的数据，但 T1 回滚操作，T2 读取就无效，这就叫脏读；
  - 事务隔离级别：
    - READ UNCOMMITED,允许以上三种
    - READ COMMITED,允许幻想读、不可重复读，不允许脏读
    - REPEATABLE READ:允许幻想读，不允许不可重复读与脏读
    - SERIALIAZABLE,三者都不允许
  - Oracle不支持脏读，默认使用READ COMMITED,支持READ COMMITED/SERIALIZABLE
  - SQL标准定义的默认事务隔离级别是SERIALIZABLE
- 数据库索引：（参照其他博客）
- 数据库引擎：（参照其他博客）
  - ISAM，读取速度很快且不占用大量的内在和储存资源。不支持事务处理也不容错。
  - MyISAM，上者的扩展，也是缺省的数据库引擎。表损坏后不能恢复数据。
  - HEAP，只允许驻留在内在里的临时表格，这也让其读取速度最快，但也是缺陷来源，没有保存就关机也就丢失所有数据。
  - InnoDB，支持事务与外键，也就意味着慢。
- CSS常用选择器并说明：
  - id选择器,选择指定唯一标识id属性的元素
  - 类选择器，选择同一类属性的元素
  - 属性选择器，选择同一属性值的元素
  - 元素选择器，直接选择指定文档元素
  - 派生选择器，依赖上下文来应用或避免某种规则
- Conllection 与 Conllections 的区别:
  - Collection：
    - java集合框架（Java Collections Framework)中的一个接口，它为具体的集合规定了最大化统一操作方式；
  - Collections：
    - 一个java集合的包装类，java集合框架中的一个成员；`public class Collections extends Object{...}`
    - 此类中除继承而来的成员外，大部分是静态成员，而且此类不能实例化出对象来；
    - 其中有一个很有用的方法：`sort(List<T> list, Comparator<? super T> c)`，将指定的list进行排序，有重载方法不指定比较器则按默认的从小到大的方式进行排序；
