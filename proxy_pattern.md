---
title: 设计模式之代理模式
date: 2017-06-13 12:14:12
tags: [programming,设计模式,java]
categories: programming
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
		- 其中生成代理静态的方法：`static Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler handler)`；
		- 此方法的三个参数：
			- loader：代理目标的类加载器；
			- interfaces：代理目标实现的接口；
			- handler：执行处理器，指调用此代理实现接口的处理器，此处理器必须实现InvocationHandler接口，并实现其`public Object invoke(Object proxy, Method method, Object[] args)`方法;
		- 具体实现：
			- 接口：
					public interface Draw {
						void draw(String name);
					}
			- 被代理的类：
					public class RealDraw implements Draw {
						@Override
						public void draw(String name) {
							System.out.println("RealDraw.draw"+name);
						}
					}
			- 代理（使用了匿名内部类）：
					public class ProxyDraw {
						Object target;
						public ProxyDraw(Object target){
							this.target = target;
						}
						/* 
						 * 获取代理实例，动态代理实现的核心
						 * @see designpatrern.dynamicproxy.Draw#draw(java.lang.String)
						 */
						public Object getProxyIns() {
							return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
									new InvocationHandler() {
										@Override
										public Object invoke(Object proxy, Method method, Object[] args)
												throws Throwable {
											System.out.println("Porxy_start");
											Object returnValue = method.invoke(target, args);
											System.out.println("Porxy_end");
											return returnValue;
										}
									});
						}
					}
			- 测试：
					public static void main(String[] args) {
						//动态代理的实现
						Draw realDraw = new RealDraw();
						Draw proxyDraw = (Draw)new ProxyDraw(realDraw).getProxyIns();
						proxyDraw.draw("....test.end");
					}
			- 测试结果：
					Porxy_start
					RealDraw.draw....testend
					Porxy_end
		- 上面代理的实现中，new一个代理实例的handler参数使用了匿名内部类，具体参照匿名内部类。
			- 匿名内部类所做的是创建了一个InvocationHandler接口的实例，这个实例的invoke()方法是用代理目标作为对象（将代理方法分发到这个代理对象上去）；
			- Proxy.newProxyInstance()方法中指定了目标类的类加载器，目标执行的接口，方法调用时所用的方法调用处理器；

### cglib代理 /子类代理###
- 此类型代理需要用到spring核心包推荐使用spring-core 3.2.5版本及以上；
- 动态代理与静态代理都是向上抽取依赖，要求实现接口，而cglib代理则是通过目标对象子类来实现对目标对象功能的扩展；
- 其实现方法：
	- 引入的功能包能够动态地在内在中构造子类；
	- cglib的强大在于高效生成代码，在运行期扩展java类与接口，spring赖以生存的AOP拦截的就是大量地使用了cglib的强大功能；
	- cglib的底层：使用字节码处理框架ASM转换字节码并生成新的类（并没有弄的懂的地方，涉及JVM中内部结构，诸如：class文件的格式秘指令集）
- 注意：cglib的实现是依赖于代理的扩展子类，需要对代理目标进行扩展
	- 也就是说代理类不能为final修辞；
	- 同时，目标对象的方法不能为final/static所修辞；
- 实现例子：
	- 目标类：
			public class Target {
				public void targetMethod(){
					System.out.println("cglib.RealDraw.draw............");
				}
			}
	- 代理工厂类：
			public class ProxyFactory implements MethodInterceptor{
				private Object target;
				//构造函数中将目标确定
				public ProxyFactory(Object obj){
					this.target = obj;
				}
				@Override
				public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
					System.out.println("开始事务拦截。。。。。。。。");
					Object result = method.invoke(target, args);
					System.out.println("结束事务拦截。。。。。。。。");
					return result ;
				}
				/**
				 * 获取代理的方法
				 * @return
				 */
				Object getProxyIns(){
					//增强工具对象
					Enhancer en = new Enhancer();
					//设置父类
					en.setSuperclass(target.getClass());
					//设置回调函数（本实例）
					en.setCallback(this);
					//创建子类
					return en.create();
				}
			}
	- 测试：
			public static void main(String[] args) {
				target.targetMethod();
				target = (Target)new ProxyFactory(target).getProxyIns();
				target.targetMethod();
			}
	- 测试结果：
			开始事务拦截。。。。。。。。
			cglib.RealDraw.draw............
			结束事务拦截。。。。。。。。
	- 实现动态代理:新建一个Target2类，并测试将Target2实例传入代理工厂对象：
		- 目标类Target2：
			public class Target2 {
				void targetMethod(){
					System.out.println("Target2.target2Method...........");
				}
			}
		- 测试：
				public static void main(String[] args) {
					Target target = new Target();
					Target2 target2 = new Target2();
					target2 = (Target2)new ProxyFactory(target2).getProxyIns();
					target2.targetMethod();
				}
		- 测试结果：
				开始事务拦截。。。。。。。。
				Target2.target2Method...........
				结束事务拦截。。。。。。。。

----------
**note:**
- 静态代理所涉及到的知识点相对更容易理解；
- 动态代理与子类代理都用到了类反射中的Method方法，其核心是利用类反射来实现将扩展的方法体加在指定目标的方法的前后；
- 在Spring的AOP编程中:
	- 如果加入容器的目标对象有实现接口,用JDK代理
	- 如果目标对象没有实现接口,用Cglib代理
- 参考博客：[岑宇-java的三种代理模式](http://www.cnblogs.com/cenyu/p/6289209.html "三种代理模式")