---
title: 设计模式之代理模式
date: 2017-06-13 12:14:12
tags: [programming,设计模式,代理模式]
categories: 面向对象
keywords: 设计模式,单例模式,代理模式
description: 代理设计模式的理解与简单分析，文中使用开发语言：java
---
### 设计模式之代理模式 ###
**简介：**
- 代理(Proxy)**提供了对目标对象另外的访问方式**;即**通过代理对象访问目标对象**。**这样做的好处是:可以在目标对象实现的基础上,增强额外的功能操作,即扩展目标对象的功能.**
- *这里使用到编程中的一个思想:不要随意去修改别人已经写好的代码或者方法,如果需改修改,可以通过代理的方式来扩展该方法*

----------
下面就来简单介绍下代理模式的三种方式：

#### 静态代理 ####
- 可以让代理类同时实现真正想的对象的类实现的接口，而在代理类中有一个真正类的属性，当想要这个真正类时，对上述属性判空，再实例化，这样通过代理类执行接口中的方法时就是执行的真正想要的类的方法了；
- 简单地讲，代理就是将其他的类的各种实现方法，交由代理来实现。而要达到这种效果，就可以让代理与其他的类实现同一个接口，再在代理中实现接口方法中去实现这个类的方法。这也是代理的核心，一般代理中要注入一个代理对象，调用代理方法时，就对此对象初始化并调用其相关的方法；
	- 定义接口：
			public interface Draw {
				void draw(String name);
			}
	- 实现类：
			public class RealDraw implements Draw {
				@Override
				public void draw(String name) {
					System.out.println(name+".RealDraw...........");
				}
			}
	- 代理：
			public class ProxyDraw implements Draw {
				RealDraw realDraw;
				public ProxyDraw (RealDraw draw){
					this.realDraw = draw;
				}
				@Override
				public void draw(String name) {
					realDraw.draw(name);
				}
			}
	- 测试：
			public static void main(String[] args) {
				RealDraw realDraw = new RealDraw();
				ProxyDraw proxyDraw = new ProxyDraw(realDraw);
				proxyDraw.draw("test");
			}
	- 测试结果：
			test.RealDraw...........
	- 静态代理的总结：
		- 做到了代理的基本功能，不对所代理对象的更改实现对象的功能，也可以在代理中对功能进行扩展；
		- 局限在于：代理与被代理都得继承接口，耦合过高，扩展不便；
		- 也正因为有了静态代理的局限，所以才有下面的动态代理：

#### 动态代理 ####
- 动态代理相对于静态代理的优势：
	- 代理不用继承接口，代理由java内置的java.lang.reflect包中的Proxy类生成：
		- 其中生成代理静态的方法：static Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler handler)