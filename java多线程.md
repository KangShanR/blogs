---
title: Java多线程 
date: 2017-07-14 12:04:38
tags: java
categories: 
description: 
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