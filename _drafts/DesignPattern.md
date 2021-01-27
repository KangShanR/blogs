---
title: 设计模式
date: 2016-06-13 12:14:38
tags: [programming,设计模式,面向对象]
categories: programming
keywords: 设计模式,单例模式,代理模式,工厂模式,迭代器模式
description: 对于面向对象设计模式的理解与简单分析，文中使用开发语言：java
---
>Design Pattern，对于编程过程中遇到各种问题的总结与归纳，是编程（通常为面向对象）过程诸多问题的优秀解决方案；
>[菜鸟教程之设计模式](http://www.runoob.com/design-pattern/design-pattern-intro.html "菜鸟教程——设计模式")

Gang of Four，四人帮提出设计模式的设计原则：
- 对接口编程而不是对实现编程；
- 优先使用对象组合而不是继承；

- 简介：
    - 共23种设计模式，分为三类：
        - 创建型Creational Patterns：提供创建对象的同时隐藏创建的逻辑，而不是用new运算符直接创建对象；
        - 结构型Structural Patterns:关注类和对象的组合，继承的概念被用来组合接口和定义组合对象的新功能；
        - 行为型Behavioral Patterns:关注对象之间的通信

<!--more-->

### 单例模式

- 对于管理类，通常只需要一个实例对象即可达到管理的目的，这种时候如果实例多个对象只会造成系统资源上的浪费，所以通常我们使用单例模式来解决这种问题；

- 实现单例的思路：
    - 将构造函数私有化，并在类中实现静态单例的共享；
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
                public void updateUserInfo(){}
            ｝
    - 上面例子里有个线程上的缺陷，当多线程访问这个管理类时，会造成线程不安全，为解决这个问题，就得使用双重检查锁单例：
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
                public void updateUserInfo(){}
            ｝
    - 关于多线程安全的实现在单例模式中有6种不同的方式，具体参照单例模式页面；

### 代理模式

- 代理模式：与工厂模式不同在于，可能产生多个实例，同时会帮Action做事（工厂模式属于创建型模式，而代理模式属于结构型模式）；
- 代理(Proxy)**提供了对目标对象另外的访问方式**;即**通过代理对象访问目标对象**。**这样做的好处是:可以在目标对象实现的基础上,增强额外的功能操作,即扩展目标对象的功能.**
- *这里使用到编程中的一个思想:不要随意去修改别人已经写好的代码或者方法,如果需改修改,可以通过代理的方式来扩展该方法*
- 代理模式就是将要实例的对象通过代理类来实现，不通过原本的类来实例，具体实现有三种方式：
    - 静态代理：
        - 可以让代理类同时实现真正想的对象的类实现的接口，而在代理类中有一个真正类的属性，当想要这个真正类时，对上述属性判空，再实例化，这样通过代理类执行接口中的方法时就是执行的真正想要的类的方法了；
        - 简单地讲，代理就是将其他的类的各种实现方法，交由代理来实现。而要达到这种效果，就可以让代理与其他的类实现同一个接口，再在代理中实现接口方法中去实现这个类的方法。这也是代理的核心，一般代理中要注入一个代理对象，调用代理方法时，就对此对象初始化并调用其相关的方法；
    - 动态代理：
        - 代理不用继承接口，代理由java内置的java.lang.reflect包中的Proxy类生成：
        - 其中生成代理静态的方法：`static Object newProxyInstance(ClassLoader loader,Class<?>[] interfaces,InvocationHandler handler)`；
            - 此方法的三个参数：
                - loader：代理目标的类加载器；
                - interfaces：代理目标实现的接口；
                - handler：执行处理器，指调用此代理实现接口的处理器，此处理器必须实现InvocationHandler接口，并实现其`public Object invoke(Object proxy, Method method, Object[] args)`方法;
    - cglib代理（子类代理）：
        - - 此类型代理需要用到 spring 核心包推荐使用spring-core 3.2.5版本及以上；
        - 动态代理与静态代理都是向上抽取依赖，要求实现接口，而cglib代理则是通过目标对象子类来实现对目标对象功能的扩展；
        - 其实现方法：
            - 引入的功能包能够动态地在内在中构造子类；
            - cglib 的强大在于高效生成代码，在运行期扩展 java 类与接口，spring 赖以生存的 AOP 拦截的就是大量地使用了cglib 的强大功能；
            - cglib 的底层：使用字节码处理框架 ASM 转换字节码并生成新的类（并没有弄的懂的地方，涉及 JVM 中内部结构，诸如： class 文件的格式秘指令集）
        - 注意：cglib 的实现是依赖于代理的扩展子类，需要对代理目标进行扩展
            - 也就是说代理类不能为 final 修辞；
            - 同时，目标对象的方法不能为 final/static 所修辞；

### 工厂模式

- 生成一个类交由相关的工厂类实现，而不由直接的类来实现；
- 相关的类继承同一个接口，实现这个接口的类都可以由工厂类中同一个方法来实现实例化；
- 例如：

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
                return null;
            }
        }
- 模式理解：
    - 通过工厂来实现对各个类的实例化，都在工厂实现；
    - 通过反射机制实现，增加一个同一接口的类不需要改变工厂的代码，只需要增加一个实现这个实现对应接口的类：
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

### 抽象工厂模式

- 相比较于工厂模式，抽象工厂模式在工厂模式的基础上增加生成工厂的工厂类，那么生产出来的工厂都要继承一个接口或父类，没理解到的点就在于，这个类一定要是一个抽象类吗？可以是个接口或者非抽象类吗?
- 该模式的优点：把同一个产品类集成到同一个工厂内，这样使用时就直接调用工厂中的方法，而生成同一个工厂里预备好的类对象，也更方便管理；
- 它是为了解决子工厂的扩展性问题

### 迭代器模式

- 迭代器模式的设计目的只是，用自己写迭代器来实现迭代聚合数据，而将集体或聚合的数据的遍历分离出来来达到既能访问到集合的内部数据又不暴露集合的内部结构；
        - 迭代器接口：
                public interface Iterator {
                    public boolean hasNext();
                    public Object next();
                }
        - 容器接口：
                public interface Container {
                    public Iterator getIterator();
                }
        - 容器实现类：
                public class Repository implements Container {
                    String elements[] = {"kfc","jfk","mcd"};
                    /**
                     * 返回内部类的对象，注意这儿的依赖倒置
                     */
                    @Override
                    private Iterator getIterator() {
                        return new DoIterator();
                    }
                    /**
                     * 内部类，实现迭代器接口
                     * @author Administrator
                     */
                    private class DoIterator implements Iterator{
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
    - 最后在main方法测试中写for循环实现迭代：
            for(Iterator iter = new Repository().getIterator();iter.hasNext();){
                String ele = (String)iter.next();
                System.out.println(ele);
            }
    - 输出：
            kfc
            jfk
            mcd

### 适配器模式

- 适配器是行为型模式，将原有的行为经过组合进行扩展；
- 顾名思义，适配器就是将原本不适的接口或功能适配到目标对象上。
- 最简单的理解类型是缺省型适配器：将各个接口实现在一个适配器上，再让目标继承这个适配器（可省，直接用这个适配器new出对象来也一样可以实现），想扩展什么接口功能就重写相应的方法，就实现了适配器的扩展功能；
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



### 装饰器模式

----------

- *未完待续*
