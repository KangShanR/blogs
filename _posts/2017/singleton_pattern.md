---
title: 设计模式之单例模式
date: 2017-08-29 12:54:30
categories: programming
tags: [java,programming,设计模式]
keywords: 懒汉式,饿汉式,双检锁,静态内部类,枚举
---

# 设计模式之单例模式

> 单例模式是设计模式最常用到的模式之一，从字面上理解就是，该对象类只产生一个实例。这样做的理由在于开发过程中很多类（比如：管理类）都只用一个实例就够了，这样做也更节省资源。但是项目开发做会有一个多线程的问题，也就是当多个线程访问同一个类的单例时，这时会产生线程安全问题，为解决这个问题，单例模式分成了6种不同类型。

<!--more-->

- 单例模式的**设计目的**就在于：不想让一个全局使用的类频繁地被创建再销毁其实例，要达到这样的效果就让其构造函数不能被不停地调用，也就是**私有化其构造函数**；

## 单例模式的6种类型

### 非线程安全的懒汉式

			public class UserManager{
				//静态单例
				private static UserManager ins;
				//私有化构造函数
				private UserManager(){}
				//获取静态单例的方法
				public static UserManager getIns(){
					if(ins == null){
						ins = new UserManager();
					}
					return ins;
				}
				//其他的执行方法
			｝
		- 这种类型的单例就是没有考虑线程安全的单例，之所以叫“懒汉式”在于其实现了这个单例的懒加载，也就是第一次使用时才会实例化这个单例；
		- 其缺陷在于当多线程同时第一次访问这个单例时，会构造出多个单例来，也就是说严格上讲它并不单例模式；

### 线程安全的懒汉式
			public class UserManager{
				//静态单例
				private static UserManager ins;
				//私有化构造函数
				private UserManager(){}
				//获取静态单例的方法
				public static synchronized UserManager getIns(){
					if(ins == null){
						ins = new UserManager();
					}
					return ins;
				}
				//其他的执行方法
			｝
		- 这处线程安全的懒汉式只是简单地在获取单例的方法上加上synchronized同步锁，同时也实现了懒加载；
		- 这样做缺陷在于效率上的低下，想要获取这个类的单例所有线程都得等待上一个线程释放资源；

### 饿汉式

			public class UserManager{
				//静态单例定义为类成员
				private static UserManager ins = new UserManager();
				//私有化构造函数
				private UserManager(){}
				//获取静态单例的方法
				public static UserManager getIns(){
					return ins;
				}
				//其他的执行方法
			｝
		- 这种单例定义为类成员，也就是类加载时就将这个单例初始化完成，未实现懒加载，会造成资源的浪费；
		- 并没有使用同步锁而实现了多线程只访问同一个实例，效率上更高；

### 双检锁（双重校验锁），DCL（double-checked locking)

			public class UserManager{
				//静态单例
				private static UserManager ins;
				private Object lock = new Object();
				//私有化构造函数
				private UserManager(){}
				//获取静态单例的方法
				public static UserManager getIns(){
					if(ins == null){  //第一次检查
						synchronized(lock){  //保证不产生多余的实例
							if(ins == null){  //第二次检查
								ins = new UserManager();
							}
						}
					}
					return ins;
				}
				//其他的执行方法
			}
		- 这种类型就实现了高性能与懒加载，逻辑上也相对复杂一点；
		- 两次检验都放在getIns方法内，第一次校验确保只有ins实例第一次被创建时进入到同步锁块中，也就是多线程下会有多个线程进入到这一步，这时最先拿到控制权的线程构造实例后，其他的线程不能再构造实例了，所以就得在同步锁中再次进入第二次检验了；


### 静态内部类（登记式）

			public class UserManager{
				//定义私有的静态内部类
				private static class ManagerHolder{
					//定义私有的静态属性，其值为我们想要的实例
					private static final UserManager INSTANCE = new UserManager();
				}
				//私有化构造函数
				private UserManger(){}
				//获取单例的方法
				public static UserManager getIns(){
					return ManagerHolder.INSTANCE;
				}
			}
		- 这种类型的单例模式效果和双检锁效果差不多，同样实现了懒加载，且没有使用同步锁；


### 枚举

			public enum UserManager{
				//单例
				INSTANCE;
				//其他的方法
			}
		- 这种方式是目前实现单例的最佳方法，但目前还没推广（jdk1.5之后才加入enum)，更简洁，自动支持序列化机制，绝对防止多次实例化；

----------
> 6种单例模式中，前两种懒汉模式不推荐使用，第一种并非纯正的单例模式，第二种懒汉模式在多线程中效率低下，更不推荐，只要进入getIns方法就得等待上一个线程释放控制权，多线程环境下很容易造成线程的拥堵；
> 当对反序列化机制有严格要求时，就使用枚举方式；
> 当要明确使用lazy loading时，就使用静态内部类方式或者双检锁方式；
