---
date: 2017-10-04 12:04:02
tags: [programming,设计模式,面向对象]
categories: programming
keywords: 设计模式,命令模式
---

# 设计模式之命令模式

> 设计模式中的命令模式，之前并没有接触过也没怎么用过。但上次在一次面试中，被一大牛问到命令模式相关的一个问题（篇末简单介绍下面试中这个问题），便将命令模式简单地理了一遍，并做了一个简单的demo用于理解命令模式；
> 设计模式通俗地理解是将命令封装成一个个对象，再让这个对象来执行命令。我们可以通过命令模式将一长串命令放入指定数据结构（比如：List），而形成命令链条，而通过遍历这个链条而对其中的对象依次执行相关的方法。这样可以将多个命令执行起来如同数据库的存储过程，也可以对各个命令实现日志记录，如果命令中出现异常利用命令链条与日志实现多个命令的回滚；

<!--more-->

## 个人通俗解读命令模式

> 通过对网上一些博客的解读，自己对命令模式的理解分为以下几点：

- 所有的命令都封装成一个对象；
- 怎么封装？
    - 相关的命令类都**实现一个命令接口`Order`**，通过依赖倒置将所有命令放一个集合中`List<Order>`，这样对这个集合进行遍历就可以将各个命令对象依次找出并执行；
    - 类似的命令通过命令类的属性直接new一个新的命令对象，而不用再造一个实现`Order`接口的类；
- 怎么实现命令的撤销与重做？
    - 要实现命令的撤销，必须将每一步命令的**操作进行备份**（可以简单用删除或剪切的命令来理解），一旦执行到撤销命令，就将备份拿出来。
    - 重做，就是直接再执行一遍被撤销的命令，执行`command`的`excute()`方法即可；
- 怎么实现多个命令的回滚？
    - 连续执行撤销命令就是实现回滚了，在具体实现中，将撤销与重做命令都写在一个命令管理类中`CommandManager`，其中撤销与重做的命令分别放在两个集合中：`undoList``redoList`，执行一次undo操作，则`remove`掉`undoList`最后一个命令对象，同时将这个remove掉的对象`add`到`redoList`中去，反之亦反；

## demo示例

> 用了股票的买卖操作简单地示例出命令模式。

- Stock股票类，这儿省去了失血模型的写法，只单纯地写了类中属性
		public class Stock {
			private int quantity;
			private String name;
			/**
			 * 重载构造函数
			 */
			public Stock(int quantity, String name) {
				// TODO Auto-generated constructor stub
				this.quantity = quantity;
				this.name = name;
			}
		}
- Order接口：
		public interface Order {
			void execute();
		}
- SellStock卖股票的类，实现Order接口：
		public class SellStock implements Order {
			private int quantity;
			private Stock stock;
			/**
			 * 构造函数
			 */
			public SellStock(int quantity, Stock stock) {
				this.quantity = quantity;
				this.stock = stock;
			}
			/* (non-Javadoc)
			 */
			@Override
			public void execute() {
				this.stock.setQuantity(this.stock.getQuantity()-this.quantity);
				System.out.println("SellStock:"+this.stock.getName()+"  quantity:"+ this.quantity +"  result:"+this.stock.getQuantity());
			}
		}
- BuyStock，买股票的类，实现Order接口：
		public class BuyStock implements Order {
		//	购买购票数量
			private int quantity;
			private Stock stock;
			/**
			 * 带参构造函数
			 */
			public BuyStock(int quantity,Stock stock) {
				this.quantity = quantity;
				this.stock = stock;
			}
			/* (non-Javadoc)
			 */
			@Override
			public void excute() {
				this.stock.setQuantity(this.stock.getQuantity()+this.quantity);
				System.out.println("BuyStock:"+this.stock.getName()+"  quantity:"+this.quantity+"  result:"+this.stock.getQuantity());
			}
		}
- Broker，经纪人，由他来操作各个命令，*简单起见，直接在构造函数中进行了操作，方便测试*：
		public class Broker {
			List<Order> orders = new ArrayList<Order>();
			Stock stock = new Stock(100,"Alibaba");
			/**
			 * 构造函数
			 */
			public Broker() {
		//		将买与卖股票的命令加入到命令链条中
				orders.add(new BuyStock(10,stock));
				orders.add(new SellStock(1,stock));
		//		遍历命令链，并依次执行各个命令
				for(Order order:orders){
					order.excute();
				}
				orders.clear();
			}
		}
- 测试：
		public static void main(String[] args) {
			Broker broker = new Broker();
		}
- 测试结果：
		BuyStock:Alibaba  quantity:10  result:110
		SellStock:Alibaba  quantity:1  result:109

### Note

	- demo只实现简单的命令模式，可以从中看出如何实现多个命令对象存入集合中，再对集合遍历操作其中的对象；
	- 没有实现撤销与重做这些功能，放在这个demo里不怎么合适是一个原因，更重要的原因是大晚上的想睡觉了，明天还得参加婚礼；

----------

## 面试经历

> 开篇说到的面试：

- 当时那位大牛是第一个面试我的官，问我最后一个问题：有没4个操作步骤，每个进行了不同的操作，怎么实现这4个步骤中有一步出现异常就回到开始的状态？
    - 我灵机一动：在一开始就copy一个对象，出现异常就将这个备份给放回去。
    - 事后看来，这个答案没什么毛病，但不是大牛想要的答案。他便再问我，这儿要是不是对对象进行操作，而是new了一个对象呢？你有没有其他的实现方法呢？
    - 想了二十秒，我说如果说对每一步进行备份也不怎么科学。大牛见我回答不上来，提示通过日志记录，但我还想不到怎么实现回滚，就放弃了。
    - 末了，我就问他在这次面试过程中，觉得我还有哪儿需要加强补习的。大牛说，自己面试了不少人，我算是答得不错的了。听了倍儿高兴，赶紧问刚才最后一题的实现方式，他就提示了：如果你熟悉设计模式的话，那么应该知道命令模式，通过这个可以实现。
- 现在把这一套弄懂后，觉得其实当时的思路还算正确，的确也对操作对象进行备份再操作，但没有见到过这些设计模式，之前也没用到过，能答成那样已经算超常发挥了。
