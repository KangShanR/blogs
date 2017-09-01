---
title: java内部类
date: 2017-04-29 13:04:38
categories: java
tags: [java,java内部类,访问修辞符,迭代器,匿名内部类,programming]
keywords: 内部类,匿名内部类,迭代器,私有化内部类
description: java内部类的使用与迭代器的实现
---
### java内部类 ###


- java的内部类指定义在类之中的类，这个类中的范围可以是是类成员也可以在局部方法中，还可以是作用域中；
- 内部类只是在编译时的概念，在本质上，当经过编译后，外部类会生成outer.class文件，而其中的内部类会生成outer$inner.class类文件，也就是就算定义成内部类，其本质依然是个类；
- 那么内部类有什么存在的必要呢？
	- 内部类是外部类的内容，在内部类之外加上一层外部类，就意味着：
		- 内部类可为public、default之外的访问修辞符所修辞，这一点能很大程度上区别开外部类；
		- 有了以上一点，那在类中定义内部类就会在很多时候有用了，比如：静态内部类的单例模式中，将单例通过静态内部私有类的方法来获取，这样利用外部其它的类不能访问这个私有的内部类而达到控制内部类的初始化类加载，这样只有在这个相关外部类中调用时才会调用到内部类，在这样一个单例模式中就实现了单例的懒加载；
	- 在迭代器模式中，在迭代器容器中创建了个内部类：
			- 容器实现类：
				public class Repository implements Container {
					String elements[] = {"kfc","jfk","mcd"};
					/**
					 * 返回内部类的对象，注意这儿的依赖倒置
					 */
					@Override
					public Iterator getIterator() {
						return new DoIterator();
					}
					/**
					 * 内部类，实现迭代器接口
					 * @author Administrator
					 *
					 */
					public class DoIterator implements Iterator{
						int index;
						/**
						 * 判定索引是否指向在集合之中，如果还在就返加true
						 */
						@Override
						public boolean hasNext() {
							if(index < elements.length)
								return true;
							return false;
						}
						/**
						 * 实现迭代器核心算法，如果后面还有元素就返回这个元素，最后的结果就是index会与集合的长度相等，索引指向最后的一个的后面
						 */
						@Override
						public Object next() {
							if(this.hasNext()){
								Object obj = elements[index++];
								return obj;
							}
							return null;
						}
					}
			- 最后在外部其他的类中来构造一个此内部类：
				- 先引入此内部类的包：`import designpatrern.iteratorpattern.Repository.DoIterator;`
				- 先构造一个外部类的对象：`	Repository rep = new Repository();`
				- 再来构造内部类对象：`DoIterator ite2 = rep.new DoIterator();`
		- 当这个内部类的修辞符为private时，这时在其他非相关的外部类中就不能导入此类，更名别说构造对象了；


### 匿名内部类： ###
- 这种情况多数用于一个抽象的类或者接口想要生成一个对象，这时不得不用一个子类来实现其抽象方法或新产品才能实现，但如果这个子类只用一次的话，专门创建一个子类来装载时会造成系统资源的浪费，那么这时使用匿名内部类会更合理：
	- 接口定义
		public interface Draw {
			void draw(String name);
		}
	- 这时我们只想实现一次特殊的draw()方法，而且只用得到一次，如果创建一个类实现这个接口（或者抽象父类），就会造成系统资源的浪费，所以就使用匿名内部类实现：
	- main方法中测试：
			public static void main(String[] args) {
				Draw draw = new Draw(){
					@Override
					public void draw(String name) {
						System.out.println("Draw.Draw...."+name);
					}
				};
				draw.draw("test");
			}
		- 测试结果：
				Draw.Draw....test
		- 其中先用接口new一个对象出来，再在这个对象中创建一个匿名的内部类并实现其draw()方法，因为是匿名的类，所以只能看到其方法在对象体`{}`中。
		- 同时要实现这个匿名内部类的效果，不一定要在接口中或抽象类的抽象方法中，普通类的普通方法也一样可以实现这样的重写方法，再一次调用：
				//实现类的匿名内部类，这个类不一定抽象类方法也不一定是抽象方法，一样都可以实现
				ProxyDraw proxy = new ProxyDraw(draw){
					@Override
					public void draw(String name){
						System.out.println("anonymity"+name);
					}
				};
				proxy.draw("");
			- 测试结果：
					anonymity
