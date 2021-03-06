---
title: 项目开发流程
date: 2017-04-29 04:33:30
tags: programming
categories: programming
keywords: 需求挖掘,需求分析,概要设计,数据库设计,详细设计,类图

---

# 项目开发的流程 #


> 在项目开发中，每个项目都有自己的开展流程，整个流程的大致步骤为：<!--more-->


1. **需求挖掘**（核心就是用户痛点）：
	1. 作为一个商业项目的程序员，一定要搞明白项目的赢利点在哪里；
	2. 要解决用户痛点；
	3. 用户分为：直接用户、最终用户
		1. 直接用户就是项目开发中，直接对接的用户，通俗的可以理解为给钱的；
		2. 最终用户是产品开发出来后使用产品的用户；
			3. *比如给一个公司开发的无纸化办公软件，这个产品的直接用户是这个公司的管理层，他们把项目交到我们手上来开发。但最终用户就是这个公司的各类普通职员，是他们在使用这个成型的产品；*
	4. 在外包项目中，在需求挖掘的过程中往往涉及到：
		1. 用户访谈
		2. 需求探索
		3. 卖家会议
		4. 领导意见
			1. *而在这整个过程中，作为开发人员不能刻意引导对方，使用引导性对话往往会带出客户的伪需求，这就会需求分析带来干扰*
2. **需求分析：**在作需求分析的对象只有两个：测试人员，普通使用者；这就决定了：写需求分析说明书时，措词要易懂清晰，少用专业词汇；
	1. 整个过程是开发人员、测试人员、客户各方不断讨论的过程
	2. 一旦有新的决定性需求确定下来，比如定稿时，一定要客户签字确认，各方责任要清晰；
	3. 当定稿之后客户要求修改需求，或修改需求成本太高，一定要出需求变更说明书，同样要求客户签字；
3. **概要设计**（数据库设计E-R图可同步进行）
	1. 比如一个简单的邮箱功能，其中的数据应该包括：
		1. 发件人列表
		2. 收件人列表
		3. 主题
		4. 内容
		5. 是否有附件
		6. 抄送人列表
7. **详细设计**
	1. 数据库设计（模型图）
	2. 用例图（PowerDesigner、Visio)：描述参与者与用例之间的关系，不能用crud之类的语言，比如：开发票与销毁发票，而不是用新建发票、删除发票；注意参与者可以是人但也可以是其他角色；
	3. 时序图（PowerDesigner)：操作到哪个对象，操作后返回什么对象
	4. 流程图（Visio)：
		1. 跨职能流程图中：每一个泳道代表一个职能；
		1. 菱形：判定，出来两根线，一个是，一个否；
		2. 矩形：流程
		3. 圆角矩形：开始或结束
		4. 平行四边形：输入数据
	5. 包图（Powerdesigner)
	6. 类图（Powerdesigner)
7. **编码**
8. **测试**
