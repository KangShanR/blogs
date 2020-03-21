---
title: 科学思维看世间百态
date: 2018-08-23 08:02:42
tags: project
categories: 心得笔记
description: 项目规范
---

# java项目规范

@(陈涛)[java项目规范 2018-04-13]

[toc]

## 1、命名规范

1. 类名全部采用驼峰式，（领域模型的相关命名除外， DO / BO / DTO / VO 等）
		  如：UserService / UserDO
<br>

2. 方法名、参数名、成员变量、局部变量都统一使用 lowerCamelCase 风格，必须遵从驼峰形式。
<br>

3. 常量命名全部大写，单词间用下划线隔开，力求语义表达完整清楚，不要嫌名字长。
	如：MAX_RETRY_NUM
<br>

4. 数据库生成的对象以DO结尾，返回给前端的对象以VO，数据传输层中的对象以DTO结尾
   如：UserDO,  XXXDO（XXX是数据库表名）UserVO（返回给前端的对象）

5. 各层命名规约
	1. Service/DAO 层方法命名规约
		1. 获取单个对象的方法用 get 做前缀。
		2. 获取多个对象的方法用 list 做前缀。
		3. 获取统计值的方法用 count 做前缀。
		4. 插入的方法用 save（推荐） 或 insert 做前缀。
		5. 删除的方法用 remove（推荐） 或 delete 做前缀。
		6. 修改的方法用 update 做前缀。
	2. 领域模型命名规约
		1. 数据对象： xxxDO， xxx 即为数据表名。
		2. 数据传输对象： xxxDTO， xxx 为业务领域相关的名称。
		3. 展示对象： xxxVO， xxx 一般为网页名称。
		4. POJO 是 DO/DTO/BO/VO 的统称，禁止命名成 xxxPOJO。

6. 	扩展mybatis的xml文件以Extend开头，如com.yzbus.dao.mysql.UserMapper扩展为com.yzbus.dao.mysql.extend.ExtendUserMapper
<br>
7. DAO类以DAO结尾，实现类在DAO后面加Impl，Service类以Service结尾，实现类为XXXServiceImpl
9. 枚举类以Enum结尾，枚举成员名称需要全大写，单词间用下划线隔开
	如：ResultEnum，枚举中的参数，成员名称： SUCCESS / NO_RESULT

## 2、代码规范
1. 缩进采用 4 个空格，禁止使用 tab 字符
	示例：

    public static void main(String args[]) {
		// 缩进 4 个空格
        String say = "hello";
		// 运算符的左右必须有一个空格
		int flag = 0;
		// 关键词 if 与括号之间必须有一个空格，括号内的 f 与左括号， 0 与右括号不需要空格
		if (flag == 0) {
		    System.out.println(say);
		}
		// 左大括号前加空格且不换行；左大括号后换行
		if (flag == 1) {
		    System.out.println("world");
		    // 右大括号前换行，右大括号后有 else，不用换行阿里巴巴 Java 开发手册
		} else {
		    System.out.println("ok");
		    // 在右大括号后直接结束，则必须换行
		}
	}

2. 每行代码的长度不要超过一个屏幕（尽量控制在80个字符），超出时需要换行，换行时遵循如下原则：
	1） 第二行相对第一行缩进 4 个空格，从第三行开始，不再继续缩进，参考示例。
	2） 运算符与下文一起换行。
	3） 方法调用的点符号与下文一起换行。
	4） 在多个参数超长，逗号后进行换行。
	5） 在括号前不要换行

	示例：
		StringBuffer sb = new StringBuffer();
		//超过 80 个字符的情况下，换行缩进 4 个空格，并且方法前的点符号一起换行
		sb.append("ab").append("c")...
		    .append("yzbus")...
		    .append("market")...
		    .append("fgh");
3. 方法参数在定义和传入时，多个参数逗号后边必须加空格。

	示例：
	public List<UserDO> listUser(String name, String other){}
	listUser("abc", "asd")

4. 方法体内的执行语句组、变量的定义语句组、不同的业务逻辑之间或者不同的语义
之间插入一个空行。相同业务逻辑和语义之间不需要插入空行。

## 3、项目结构规范

### 3.1 应用分层

*  ```WEB层```
	主要是对访问控制进行转发，各类基本参数校验，或者不复用的业务简单处理等，提供http接口

* ```Service 层```
	相对具体的业务逻辑服务层

* ```DAO层```
	数据访问层，与底层 MySQL、 Oracle、 Hbase 进行数据交互（写DAO层时考虑其他人使用，尽量不要出现类似功能的接口）。

* ```外部接口或第三方平台```
	包括项目 RPC 开放接口，基础平台，其它公司的 HTTP 接口。如融云、高德、有座系统的接口

PS： service层中不能注入Mapper来访问数据库，必须通过dao层访问数据库
	如：在UserServiceImpl中，只能注入一个UserDAO，不能在Service中注入一个UserMapper

### 3.2工具类存放目录
1. 常用工具类统一放到tools包下面
2. 定义的枚举类统一放到enums包下面
