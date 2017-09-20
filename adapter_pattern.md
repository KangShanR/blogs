---
title: 设计模式之适配器
date: 2017-08-29 13:04:38
categories: programming
tags: [java,设计模式,programming]
keywords: 
---

#  适配器模式
> 
- 适配器是行为型模式，将原有的行为经过组合进行扩展；
- 顾名思义，适配器就是将原本不适的接口或功能适配到目标对象上。
- 最简单的理解类型是缺省型适配器：将各个接口实现在一个适配器上，再让目标继承这个适配器（可省，直接用这个适配器new出对象来也一样可以实现），想扩展什么接口功能就重写相应的方法，就实现了适配器的扩展功能；<!--more-->

## 适配器模式的两种常用类型 ##
- 而比起缺省型适配器，还有类的适配器和对象适配器：
	- 类适配器：将目标功能的接口执行并继承被适配者
		- 被适配者：想在此类上扩展目标接口的功能（适配器的设计初衷在于，被适配者并不能直接实现目标接口）
				public class Adaptee {
					void run(){
						System.out.println("Adaptee.run..........");
					}
				}
		- 目标接口：
				public interface Target {
					void fly();
				}
		- 适配器：
				public class Adapter extends Adaptee implements Target {
					@Override//继承父类被适配者，并能方便地扩展其方法
					public void run() {
						super.run();
						System.out.println("Adapter.run.......");
					}
					//实现目标接口的方法
					@Override
					public void fly() {
						System.out.println("Adapter.fly...........");
					}
				}
		- 测试：
				public static void main(String[] args) {
					Adapter adaptee = new Adapter();
					adaptee.fly();
					adaptee.run();
				}
		- 测试结果：
				Adapter.fly...........
				Adaptee.run..........
				Adapter.run.......
		- **note:**这样的一个适配器在某种程度上来说与缺省的适配器并没有什么本质上的区别，都是将各种功能行为通过继承或实现聚合到同一个适配器上；
	- 对象适配器模式：
		- 与类适配器不同在于，对象适配器将被适配的类注入到适配器，再让适配器实现想要实现的接口，而让适配器聚合了这两者的成员；
			- 被适配者与目标接口与上面例子一样，但适配器变成：
					public class Adapter implements Target {
						private Adaptee adaptee;
						public Adapter (Adaptee adaptee){
							this.adaptee = adaptee;
						}
						//扩展被适配者方法相对来说要被动一点
						public void run() {
							this.adaptee.run();
							System.out.println("Adapter.run.......");
						}
						//实现目标接口的方法
						@Override
						public void fly() {
							System.out.println("Adapter.fly...........");
						}
					}
			- 可以看出来，对象适配器是将被适配者作为一个对象注入到适配者中，实现聚合其方法；


----------
**Note:**
- 适配器的好处在于：将各个部门不同的功能聚合到同一个对象上，而实现各个不同的功能都可以通过一个对象实现，达到了高聚合的效果；
- 适配器的缺点：各个不同的接口聚合到同一个类中，而让这个对象的接口变得混乱，当这个对象实现某一个功能时，会不清楚实现这个功能到底来自于哪个接口；