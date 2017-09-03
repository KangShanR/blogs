---
title: 设计模式之工厂模式
date: 2017-08-28 10:04:03
categories: programming
tags: [java,设计模式,programming]
keywords: 
description: 关于设计模式中工厂模式与抽象工厂模式创建对象的理解
---

### 工厂模式 ###
- 生成一个类交由相关的工厂类实现，而不由直接的类来实现；
- 相关的类继承同一个接口，实现这个接口的类都可以由工厂类中同一个方法来实现实例化；
- 例如**简单工厂模式**：

		//定义同一个类的抽象接口
		public interface Shape{
			void draw();
		}
		//实现shape接口的类有：
		//Circle类
		public class Circle implements Shape{
			@Override
			public void  draw(){
				system.out.println("circle.draw");
			}
		}
		//Square类
		public class Square implements Shape{
			@Override
			public void draw(){
				system.out.println("square.draw");
			}
		}
		//创建实例的工厂类
		public class ShapeFactory{
			//创建图形实例的方法
			public Shape createShape(String shape){
				if(shape.equalsIgnoreCase("CIRCLE”){
					return new Circle();
				}
				if(shape.equalsIgnoreCase("SQUARE”){
					return new Square();
				}
				reutnr null;
			}
		}
	- 模式理解：
		- 通过工厂来实现对各个类的实例化，都在工厂实现；
		- **通过反射机制实现，增加一个同一接口的类不需要改变工厂的代码,只需要增加一个实现这个实现对应接口的类**：
			- 其工厂的代码：
					public class ShapeFactory{
						//创建工厂的方法
						public Shape createShape(Class<? extends Shape> clazz){
							try{
								return Class.forName(clazz.getName()).newInstance();
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}

- **工厂方法模式**
	- 相较于简单工厂模式，工厂方法模式实现了**“用扩展取代修改”**，简单工厂中要增加减少实现产品的构造需要对工厂的代码进行修改，而工厂方法模式将这些都放在子类的实现中；
	- 示例：生产iphone与miphone两种手机，将手机与工厂的接口抽取出来，分别用不同的工厂生产不同的手机；
		- 手机接口：
				public abstract class Phone {
				}
		- 手机类：
				public class Iphone extends Phone {
					public Iphone(){
						System.out.println("Iphone.constracting.....");
					}
				}
				public class Miphone extends Phone {
					public Miphone(){
						System.out.println("Miphone.constracting......");
					}
				}
		- 工厂接口：
				public interface Factory {
					Phone createPhone();
				}
		- 工厂类：
				public class IphoneFactory implements Factory {
					@Override
					public Iphone createPhone() {
						return new Iphone();
					}
				}
				public class MiphoneFactory implements Factory {
					@Override
					public Miphone createPhone() {
						return new Miphone();
					}
				}
		- 测试：
				public static void main(String[] args) {
					//创建iphone过程，先建工厂，再在工厂中创建iphone
					IphoneFactory facIphone = new IphoneFactory();
					Iphone iphone = facIphone.createPhone();
					//创建miphone过程：
					MiphoneFactory facMiphone = new MiphoneFactory();
					Miphone miphone = facMiphone.createPhone();
				}
		- 测试结果：
				Iphone.constracting.....
				Miphone.constracting......
	- *工厂方法模式将对象的创建进行了很好的包装，但也正因此而每增加一个产品的类就不得不增加一个工厂来实现其构造，这在某种程度上来说是不科学的；*

### 抽象工厂模式 ###
- 相比较于工厂模式，抽象工厂模式在工厂模式的基础上增加生成工厂的工厂类，那么生产出来的工厂都要继承一个接口或父类；
- 该模式的优点：把同一个产品类集成到同一个工厂内，这样使用时就直接调用工厂中的方法，而生成同一个工厂里预备好的类对象，也更方便管理；
- 它是为了解决子工厂的扩展性问题，而将系列产品装配在同一个工厂中，也就实现了不同产品之间相互组合不同最终产品，而子工厂中不同产品抽取接口或父类方法出来成为抽象工厂；
	- 示例：生产手机需要手机引擎、手机音乐盒这两种产品，这两种产品分别有两种类型，那么，一个引擎加一个播放器组成一个手机产品集
		- 引擎接口：
				public interface Engine {
				}
		- 引擎产品的两个类：
				public class EngineAmd implements Engine {
					public EngineAmd(){
						System.out.println("EngineAmd.constracting....");
					}
				}
				public class EngineIntel implements Engine {
					public EngineIntel (){
						System.out.println("EngineIntel.constracting.......");
					}
				}
		- 音乐盒接口：
				public interface MusicBox {
				}
		- 音乐盒的两个不同产品类：
				public class NokiaMusic implements MusicBox {
					public NokiaMusic(){
						System.out.println("NokiaMusic.constracting...");
					}
				}
				public class SonyMusic implements MusicBox {
					public SonyMusic(){
						System.out.println("SonyMusic.constracting...");
					}
				}
		- 抽象工厂类，规定手机工厂要生产的两个产品：一个引擎一个音乐盒，这儿可以用接口也可以用抽象类
				public abstract class AbstractFactory {
					abstract Engine createEngine();
					abstract MusicBox createMusicBox();
				}
		- 两个工厂类，实现抽象工厂，实现装配两个产品到一个工厂中
				//苹果手机厂，使用intel引擎与sony的音乐盒
				public class AppleFactory extends AbstractFactory {
					@Override
					public EngineIntel createEngine() {
						return new EngineIntel();
					}
					@Override
					public SonyMusic createMusicBox() {
						return new SonyMusic();
					}
				}
				//联想手机，使用amd的内核与Nokia的音乐盒
				public class LenovoFactory extends AbstractFactory {
					@Override
					public EngineAmd createEngine() {
						return new EngineAmd();
					}
					@Override
					public NokiaMusic createMusicBox() {
						return new NokiaMusic();
					}
				}
		- 测试，将工厂创建出来由它们来创建手机的子产品
				public static void main(String[] args) {
					Apple apple4s = new Apple();
					Lenovo lenovo3 = new Lenovo();
					//apple生产过程
					AbstractFactory appleFactory = new AppleFactory();
					apple4s.engine=appleFactory.createEngine();
					apple4s.musicBox=appleFactory.createMusicBox();
					//Lenovo生产过程
					LenovoFactory lenovoFactory = new LenovoFactory();
					lenovo3.engine=lenovoFactory.createEngine();
					lenovo3.musicBox=lenovoFactory.createMusicBox();
				}
		- 测试结果：
				EngineIntel.constracting.......
				SonyMusic.constracting...
				EngineAmd.constracting....
				NokiaMusic.constracting...
		

----------
**Note:**
- 从上面例子可以看出：
	- 抽象工厂可以实现组合产品集，一个工厂组合一个产品集，同时想要实现产品集的扩展也很方便。想要订制同一个产品集不同型号，直接实现一个工厂类。如果要不同产品集，则需要对抽象工厂进行扩展修改；