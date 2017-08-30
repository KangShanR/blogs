---
title: Java多线程 
date: 2017-07-14 12:04:38
tags: [java,单例模式]
categories: 
description: java多线程的实现与单例模式中的理解
---

#### 线程安全 ####
>在一个运行程序中，当另外一个线程对这个程序发起请求时，相当于重新执行其中方法，但一个类中某些属性在程序执行过程中需要保持状态的统一性，这时就需要在一个线程执行完毕后再让另一个线程执行相关的方法。这就需要实现线程安全；

- 实现线程安全的方法：
	- 将方法修辞为线程安全的方法（加上修辞符synchronized)：
			public synchronized void doPay(int money){
				if(balance >= money){
					balance -= money;
				}else{
					system.out.println("你的余额不足！”）
				｝
				system.out.println("你的余额为：“+balance);
			}
	- 使用同步块：
			synchronized(Object object){
				//要同步的代码块
			｝

- 实现同步机制是基于”监视器“这一概念，java中所有对象都拥有自己的监视器，加上同步锁，就是让这个对象进入监视中；
- 在各个project中，对于各管理类使用单例模式时，常常容易忽略多线程做成伪单例。也就是做成单例只单单在单线程中可以实现，在多线程环境中实现不了单例。
			public class Manager{
				private static Manager ins;
				//私有化构造函数
				private Manager(){}
				//获取单例
				public static Manager getIns(){
					if(this.ins == null){
						this.ins = new Manager();
					}
					return this.ins;
				}
			}
	- 这种单例的实现就必须在依赖单线程的运行环境；
	- 但现实中往往是多线程环境，这时要实现单例就得双重校验锁：
			public class Manager{
				private static Manager ins;
				private Object lock;
				//私有化构造函数
				private Manager(){}
				//获取单例
				public static Manager getIns(){
					if(ins == null){    //第一次校验，当多个线程执行获取单例的方法时，先对单例是否为空进行判定，为空才进入同步状态
						synchronized(lock){
							if(this.ins == null){  //第二次校验，进入同步锁状态，对单例进行校验，为空的话才进行构造单例
								this.ins = new Manager();
							}
						}
					}	
					return this.ins;
				}
			}
		- **一定要理解到这儿使用的双重校验缺一不可**：两次校验才能实现同步线程对多线程操作的效率：
			- *如果单单全用一次校验在方法上，也就是所有访问到这个方法的线程就直接进入到同步锁状态，那么就会造成大多访问线程的滞留，后部都等到第一个线程执行方法完毕再依次进入到方法中执行，这样明显是不合理的；*
			- *如果在方法内部同步块之前只使用一次判定，就会造成多个线程同时执行这个获取单例方法时ins被判定为null，多个线程同同时等待同步块执行完，这样在第一个执行的线程构造单例后，虽然这时执行getIns()方法的线程不会进入到同步块等待了，但之前进入到同步块等待的线程还是会依次等待前面线程释放控制权后继续构造单例；所以才会在这同步块内再次校验ins是否为空，形成双重校验*