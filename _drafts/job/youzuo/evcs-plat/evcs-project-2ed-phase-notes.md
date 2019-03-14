---
layout: "post"
title: "evcs_project_2ed_phase_notes"
date: "2018-12-13 16:52"
---


# 电桩项目二期


新接口：
- 充满预提醒消息 （此功能取消）
  - 充满前 10% 时提醒
  - 如果使用心跳包提醒则需要作好标记，是否已经提醒过如果 没有提醒过则发出消息，有过则不再 提醒，必须要给 一个检查。


token:
APP

ASmdlx88eJCVaKgd4itfqK7oMVQB3ZYloaj+m0CE1TXnhXVCfZz1/7RZRSCwp8688U9ilrI6+9CizZhdIAO52oD/EzDxCzVc9mG9Jaob0P39

web
ASmdlx+cE3eERejbuN0ytreqAkCl3OzBIBl905UPUWDj12b862jMkcyPAMliFhjXbOgqT+DYMdGpYmSu3qKhfOc=

ent_web
ASmdlx+gHSgKLeMZowFoPy0FTB+vowjApqUtbyCBS1TS0cotGuM0IZth+XHKvSnM2tmlgutmGT4QTzIhQ6ZO0pM=



[Umeng](https://www.umeng.com)
- account：uz@uzbus.com
- password：@n9lsX5K^Jge!O92

友盟消息推送 key：
  - title             string   标题
  - body              string   消息内容
  - time              long     消息发送时间
  - receiveUid        long     接收用户ID
  - event             string   事件类型（比如 充电完成"CHARGE_FINISH"）
  - type              string   消息类型（原生"NATIVE"、网页"H5"、无操作"NOTHING"）
  - resId             long     对应事件类型，只有 NATIVE 类型才需要使用到（比如充电订单ID "123"）
  - url               string   网页跳转的 url（type=H5时需要）

TODO List：
- [x] ios 正式模式下推送消息（重新获取 token 需要重新上传证书）
  - [x] 正式模式下使用正式环境证书的 token 测试通过


## 二期 移动端 功能更新


## Jan 2ed, 2018 移动端评审会议

- [x] 最低价 改为 当前时间段 的价格
  - [x] 场站列表
    - [x] 当前时段的字段的获取
  - [x] 场站详情：暂缓，等待确定是否让移动端来显示
  - [x] 排序也变为 按当前时间段的价格排序
  - [x] 新增场站末尾时间为 00：00：00 导致时间比较出现 bug：当结束时间比开始时间小时，应该将结束时间中 day +1 再进行比较
  - [x] 首页地图同样给出进行 **价格非空** 筛选后的场站列表
  - [x] 查找场站接口同样进行筛选
- [ ] 使用企业钱包扣除金额
- [x] 企业权限 给用户的权限
  - 目前用户权限只给一套，不存在单项取最大值。同时，权限 只有 soc 与 枪数量限制，不存在下面的问题
      - [x] 单项取最大值 - shutdown
      - [x] 不同项取最小值（电量/**soc**/电费/时长）- shutdown
  - [x] 用户解除与企业关系
    - [x] 确定解除关系的数据库 数据逻辑
    - [x] 加验证码
    - [x] 查看用户充电权限
    - [x] 验证是否有使用企业钱包的正在充电订单
- [ ] 企业平台添加用户
  - [ ] 如果 用户存在加入到企业关联中来
  - [ ] 用户不在企业中时，不使用企业钱包时，充电提示
  - [ ] 是否有充电权限的接口
- [ ] 解除关系：有未完成订单（正在充电订单）不让解除
  - [x] 发送短信 确认解除 - 使用基础接口 RPC 发送短信 @陈佳 http://misc-uz.uzbus.local/swagger-ui.html#/
    - [x] 项目 redis 中存放短信信息进行验证
    - [x] 验证码 单独服务，保留字段：时间/ip（从 request 获取）/code  - 在 我的 页面接口中上传此参数
      - [ ] 同一 ip 一分钟内取过验证码不再请求成功抛出异常
      - [ ] redis 事务的实现
  - [x] 查看充电权限
- [x] 消息推送
  - [x] 短信推送
  - [x] soc max 从 redis 是取 实际 soc
- [x] 充电接口
  - [x] 检查当前用户是否被 企业禁用
  - [x] 检查充电权限是否满足
- [x] 充电订单
  - [x] 已完成订单接口
  - [x] 充电详情 增加字段 ： 功率 outputPower
  - [x] 增加订单状态 预充 PREHEATING
  - [x] 充电中查看费率 - 查看该订单的使用的费率（没找到充电中的费率入口）
  - [x] 各个地方：**金额单位保留两位小数，电量保留整数**
- [x] 充电完成接口
- [ ] 开始充电 - 交 @陈涛
  - [ ] 权限验证
  - [ ] 兼容 c 端用户请求
- [x] 充电完成订单列表
  - [x] 开发环境分页的 bug
    - [x] total bug
- [x] 接口修改
  - [x] 带单位的数据 全取消
    - [x] 计费规则弹窗
    - [x] 正在充电订单列表
    - [x] 场站详情
    - [x] 正在充电订单
    - [x] 场站列表
- [x] evcsApollo 配置未能正常获取并启动 项目
  - 配置未发布引起
- [x] 添加 h5 域名配置并在代码中获取 ping 成 url
- [ ] 企业充电用户管理接口
  - [x] 添加充电用户
  - [x] 修改充电用户
  - [x] 充电用户列表
  - [x] 查看充电用户 - 将列表信息带入到弹窗，增加一个企业充电权限枚举接口
  - [x] 获取企业充电权限
  - [x] 添加充电用户接口/启用充电用户接口验证用户当前没有 **注册** 在其他企业
  - [x] 产品确认，企业管理人员不需要有使用企业钱包充电功能 Mar 1st,2019
  - [x] 修改充电用户注册电话号码
  - [x] 企业管理员目前没有删除企业充电用户逻辑
    - [x] Mar 7th, 2019 产品确认可无
  - [x] 等待充电接口 - 国标字段未处理
  - [x] 消息推送加数据库记录
  - [x] 企业充电权限 不限制 NOT_LIMIT 改为 null
    - [x] 不限制 NOT_LIMIT 可能引起的 bug ，不限制改为 null - 开始充电接口 @ct
    - [x] APP 端展示
    - [x] web 端添加/详情接口
  - [x] APP 我的企业接口 - 添加 电话号码字段
  - [ ] @肥头大耳 初始的充电权限 名称/描述/创建人 约定成啥？
    - [ ] 肥头大耳:初始充电权限 /这是一条初始的充电权限/（超级管理员名称）Mar 12th, 2019
- [x] 正在充电订单 - 查看订单计算相关电费/服务费 等，使用快照表数据，而不是价格表数据
- [ ] 开始充电接口
  - [ ] 企业充电时的权限验证 + 错误提示
- [x] 我的页面 - 无电话号码
- [ ] 添加企业充电人员
  - [x] 原离开人员再次添加的 bug - fixed
  - [ ] 原存在人员，此时修改了其姓名要保留吗？ - 目前履盖，需要跟产品确认
  - [ ] 企业充电用户名称另加，如果初始没有姓名则添加到个人信息中 看加到哪张表：ent_cus?
- [x] 充电人员启停接口修复 - dev fixed
- [x] 充电权限启停接口修复 - 前端请求参数错误
- [ ] app 企业界面 增加字段



## new idea

- 项目模块设计为：单个模块为一整个模块，而不是整个 dao service 层成为一个模块
- 最好不要多表查询，使用 JOIN ，就算有分页查询也先查出所有的数据再在内存来 lambda 来实现分页


## 企业端/平台端 测试用例评审

> Jan 17th, 2019

初始化企业管理账号，超管与二级角色同时给。
- 开始充电：枪 -> 用户权限状态 -> 钱包状态


## 企业端相关 数据库设计

TODO list：
- [ ] en_customer_permission 新加 enable 字段来确认些司机是否开启权限 是否不妥？
- [x] 是否需要 将 enable 换成 enabled?
  - 不需要，此字段不是关键字，而 delete 改成 deleted




## Jan 24th, 2019 产品定档会议

@hy
- [x] 之前充电管理员的角色还要？保留无限充电枪
- [x] （不保留，统一到充电权限里去）
- [x] soc 停止 ，发消息？文本要区别于其他停止消息推送
- [x] 模拟器能不能按照已经规定的 stopCode 发送，能不能模拟电桩故障
  - [x] 由测试处理
- [x] 充电列表 预充的状态加上
- [x] 停止充电，预充状态可以停止
- [x] 正在充电 添加 endReason
  - [x] 确定 soc 被停止 时可以从 billingRecord 拿到，拿到后去 redis 拿 soc 数据
- [x] 开始充电 权限 判定，获取权限充电 BillingRecordServiceImpl    line 400


## apollo configuration

h5：
- key:    evcs.h5.url
  - dev
    - billing_record http://mod-h5.evcs.uzbus.local/#/chargeEnd?billingId=
    - billing_detail http://mod-h5.evcs.uzbus.local/#/costCountDetail?billingId=
  - test http://test-h5-snxia.uzbus.local/#/
  - pre  http://pre-snx-h5.snxia.com/#/
  - prd  https://h5.snxia.com/#/

```
evcs.h5.url:
开发环境
[{"key":"billing_record","url":"http://mod-h5.evcs.uzbus.local/#/chargeEnd?billingId="},{"key":"billing_detail","url":"http://mod-h5.evcs.uzbus.local/#/costCountDetail?billingId="}]
测试环境
[{"key":"billing_record","url":"http://test-h5-snxia.uzbus.local/#/chargeEnd?billingId="},{"key":"billing_detail","url":"http://test-h5-snxia.uzbus.local/#/costCountDetail?billingId="}]
预发布环境
[{"key":"billing_record","url":"http://pre-snx-h5.snxia.com/#/chargeEnd?billingId="},{"key":"billing_detail","url":"http://pre-snx-h5.snxia.com/#/costCountDetail?billingId="}]
生产环境
[{"key":"billing_record","url":"https://h5.snxia.com/#/chargeEnd?billingId="},{"key":"billing_detail","url":"https://h5.snxia.com/#/costCountDetail?billingId="}]
```
