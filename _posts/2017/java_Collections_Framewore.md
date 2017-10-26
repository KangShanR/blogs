---
title: java集合框架
date: 2017-10-09 00:23:30
categories: programming
tags: [java,programming,数据结构]
keywords: 
---
# java中的数据结构 #

> java集合框架是java编程中最常用到的底层框架，对于平常的编程中，可能使用到的集合并不会考虑太多，但随着一个程序的扩张，数据量的增加，所选择的集合类型决定了这个程序的健壮性。

<!--more-->

## java集合框架各个接口 ##
> JCF：java collections frameword，三个核心接口：Set List Map，其中，Set与List都继承基接口Collection，而Map是映射关系的接口。在C#中，还有一个常用到的集合字典：Dictionary<K,V>，在java中，这个集合被Map接口下的各个类所取代。

### List ###

> 最常用到的集合类型，其中实现这个接口的集合类有：ArrayList<T>、LinkedList<T>、Vector<T>。

#### List中各集合的异同点 ####

1. ArrayList<T>的底层实现就是将数组实现**动态变化**，当数据增加到数组的边界时重新声明一个数组（其大小为原数组的`3/2+1`，数组初始大小为`0`）。同时将原数组中元素遍历存入到新数组中。
2. Vector<T>基本与ArrayList<T>一致，但其实现了**多线程环境的安全性**。*自我猜想：实现线程安全就是在这个类中使用一个访问下标属性（简单的int类型即可），当一个线程访问到该对象的数组中的某个下标的元素进行修改或删除时，这个属性标记下来，这时另外一个线程再来访问这个Vector对象中的数组元素时会检查其访问的下标是否与之前标记的下标一致，一致则进入到`synchronized`同步代码块中等待上一个线程释放资源。
*
1. LinkedList，链表。其与前两者的不同之处在于，其存入数据通过一个个**节点**来实现。每个节点（节点使用内部类）拥有数据本身与指向下一个节点的属性（如果是双向链表，则还拥有指向上一个节点的属性），这样一个个节点通过节点本身的属性来访问到相邻的节点信息。这样相对于前两种集合实现数据的删除与增加就方便快捷得多（直接修改相邻节点的相邻节点属性，而不用像前两者删除一个下标的元素需要将其后所有的元素遍历存入到对应下标中），而在修改指定下标元素值时，就劣势了，道理相通。

### Set集合 ###
> java集合框架中最简单的集合类型，也是最符合数学领域中的集合概念：**无序不重复**。
>** Set与List两者的根本区别**：List是在Array数组的基础上实现，而Set是在HashMap的基础上实现。

#### Set集合中各个集合类型的异同 ####
> 主要包括了HashSet<>、TreeSet<>、LinkedHashSet<>三种集合；

- HashSet按照Hash算法来存取集合中的对象，存取速度比较快。Hash算法的设计目的就是减少对集合内元素的遍历比较。HashSet的存储方式是将HashMap的key作为Set的对应存储项。
- TreeSet实现了SortedSet接口，也就可以对集合中对象进行排序。同时这也要求存入TreeSet的对象必须实现`Comparable`接口。通过TreeMap实现，使用的是Map的key。其支持自然排序与定制排序两种排序方式：
	- **自然排序**要求元素实现`Comparable`接口，根据其`ComparaTo()`方法的结果（返回int值大于0则对象大于被比较的参数，等于0则相等，小于0则小于被比较的参数）来进行**升序排列**；
	- **定制排序：**实现`Comparetor`接口，实现其`int compare(T o1,To2)`方法来保证元素的唯一性。其底层数据结构是：二叉树
- SortedSet，此接口继承自Set接口，其作用主要是唯元素的有序性。这就要求加入到SortedSet中的元素必须实现`Comparable`接口；
- LinkedHashSet，是HashSet的子类，同时实现了使用Hash算法来存放与链表算法来存放插入的元素，所以遍历LinkedHashSet时，结果会按插入的顺序显示。通过使用链表的算法来保证插入元素的插入顺序。

### Map集合类
> 作为键值映射类集合，其中包括了：HashMap<K,V>、TreeMap<K,V>，HashTable（非泛型），Propertyes（继承自HashTable)
> 作为键值映射集合，其中的key的的唯一性必须要得到保证。不然通过key来查询value的终极意义就失去了。

- **HashMap:**顾名思义，此类同样使用了Hash算法来保证查找键的快速性。允许使用空键与空值，但键的唯一性不可更改。
- **TreeMap：**对键按序存入，所以TreeMap就有一些与排序键相关的方法：`firstKey(),lastKey()`。
- **HashTable:**非泛型集合，继承自`Dictionary<K,V>`（已废弃，推荐使用Map），key与value都不允许使用null。
- **Properties：**继承自HashTable，主要用于读取java的配置文件，在Spring中常常用来写数据源的信息。
	- Properties中不如果有了相同的key，新的信息会将旧的信息覆盖。
- **HashMap与HashTable的区别：**
	- HashMap初始大小为：16，允许存入一个空键与多个null值，线程不同步；Hash算法使用求模，Hash数组增加方式:2的指数
	- HashTable初始大小为：11，不允许存入空键与空值，线程安全，Hash算法使用按位与；Hash数组增加方式：`old*2+1`


----------
*感觉上：tree的算法是通过二叉树实现了排序，而Hash算法对集合中元素进行了分桶装而实现更快速的查找；*