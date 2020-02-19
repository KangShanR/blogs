---
layout: "post"
title: "c 端"
date: "2019-03-07 14:35"
---

# 电桩项目 c 端

## 评审会上确认

- 消息推送：
  - 目前暂定让后端来实现退出登录后不推送消息
  - 问题：ios 能否像 android 一样验证 id 后发送消息
    - 想到的解决方案：登出后再加一个接口，将 umengToken 清除
    - 推送前判断是否登出，
      - 拿登陆的 token 存入本地判断时再用此 token 去访问登录服务接口判断此 token 是否过期或退出（已确认登录服务是否有更优雅的接口提供）

## DO replace regrex

```java
/\*\*\n.*\n.*\n.*\n.*\n.*\n.*\*\/$
```

## TODO list

- 产品相关
  - [x] 消息列表：
    - [x] 确认这一期是否做启用与停用账户功能
      - [x] 要做，企业用户的启用是否发送开户消息 @hy - 不发 25th Mar， 2019
    - [x] 消息删除功能，跟移动端确认怎么实现删除， 支付后一样不能删除？ @hy - 按原型，全都不用删除 25th Mar, 2019
      - [x] 未支付消息，点击跳转到 充电费用明细 页面 如上，已确定
  - [x] 我的资料
    - [x] 所有都单独修改，不用一起提交？ @hy 与移动端同事确认
    - [x] 修改手机号： @hy 如果已注册于系统业务如何处理 - 不允许修改 25th Mar, 2019
    - [x] 产品： @hy 姓名 与 昵称是否放一个概念，是否让其随意改
      - [x] 如果 不是，注册时是否放真实姓名？
    - [x] 常用地址：显示时是给哪个地址？ @hy - 第一个家庭地址 @hd @hj 25th Mar,2019
  - [ ] 企业页面
    - [ ] 以企业支付方式来判定是否显示分组信息，还是以是否有分组信息来定是否显示分组信息 @hy
    - [ ] 结算中订单状态算在未支付吗？ @hy
- 开发相关
  - [x] 企业展示页面 增加组信息
  - [x] 等待充电接口更新 - @ct 做
  - [x] 我的资料
    - [x] 获取
    - [x] 修改 - 不修改项为 null, 修改的
  - [x] oss 上传图片接口 - 解决
    - [x] 启动不了
  - [x] customer_info 中 avatar 字段删除，使用 img_dict 存取 - 单个数据就放原表
    - [x] 改存，改取
  - [x] 图片字典表：只能定位到表行，不能定位到列数据。如：要增加用户另一个维度的图片，将无法区分开 - 已反馈，未解决 Mar 29th, 2019
  - [ ] 测试环境偶发：模拟器异常停止，同一订单收到 10 多条推送 Mar 29th, 2019
  - [ ] ent web 端 - 分表后，从ent_cus 表 取 ent manager  都得改成新表数据源
  - [ ] 验证码使用之后 - 置为无效
  - [ ] 开发环境 - 获取 nickname / autoPay  为 null(本地不存在，暂定开发日志上线后再调)
  - [ ] 修改手机号后，不能再注册也不能登录
  - [ ] 注册系统错误（郑定明 - 修改手机号后，再用原来的号码注册，发验证码表示已存在于系统中）
  - [x] 额度明细接口
  - [ ] dao 的 bean 扫描为两个，解决方法：未做任何修改，只用了原来 core 配置中错误的扫描为 dao.mapper
  - [x] 完成充电后数据 增加
  - [x] 付款接口（目前只走余额）
  - [ ] 离开企业后 - 刷新缓存中的企业信息（删除）
  - [ ] 结束订单 h5 接口 增加支付方式字段
  - [ ] 确定使用余额支付 是否需要更新远程 redis 账单数据
  - [x] my_page 拉回 portrait_address
  - [ ] 已完成订单 h5 页面 + 新的出租车字段
  - [x] 订单状态的变化 ：订单状态，READY:准备充电状态, FAULT:异常 CHARGEING:充电中，INSETTLEMENT:结算中 UNPAID:待支付 ALIPAYFAIL:支付宝预授权扣款失败 AUTOPAYFAIL:自动扣款失败 PAYFAIL:支付失败，FINISH：完成
    - [x] 移动端确认数据： 个人|企业 |钱包自动支付成功|钱包手动支付成功|芝麻信用成功|芝麻信用失败 April 3rd, 2019
    - [ ] 后台支付时需要将此状态写入 @ct
  - [x] 预充状态判定条件：状态如果不在 charging 就不用再取 redis 数据来判定 @lb 确定是否改动
  - [x] 增加是否有未支付订单接口 ： 返回 billingId 与 boolean
  - [x] 组成员列表 - 加上 customerId
  - [x] 充电完成订单接口
    - [x] 加字段 额度支付/剩余额度 - 不用加，已跟移动端确认
    - [x] 总金额返回 单位为 分 的数据 integer
  - [x] 未支付订单返回所有的未支付 billingId
  - [x] 将字典数据查询接口 copy 到 app 服务来 - 避免引起相互
    - [x] 将场站标签相关已加
  - [x] 待支付消息 的推送
    - [x] 增加 resId 与 receiveUid 字段
  - [ ] service 不能引用 modules 包引起现不能找到 企业充电权限创建者名
  - [x] 初始昵称长度 与 电话号码 - 长度改成 15 位

### debug

- [x] 测试环境 - 注册发送验证码 系统错误
- [x] 从缓存获取用户信息的全部修改从数据库获取
- [x] 从 enterprsie_customer 获取 enterprsie_manager 的方法，全部改写  从新表获取企业管理员数据
- [x] 其他企业管理员添加到其他企业为充电人员 - 系统错误2128
- [x] unpaid 状态，无金额数据
- [x] 充电中 - 增加 unpaid 状态
- [x] 登录验证码错误
- [x] 手动支付 - 返回自动支付
- [x] 手动支付不成功 - finish 接口
- [x] 原接口的兼容
- [x] 充电中详情的 空指针异常
- [x] 注册用户 autopay false
- [x] 原版本订单状态 是归一到原有的状态 - 不用处理，老版本不会使用到新的订单状态
- [x] sendVrfCodeToUnregisteredPhone 被改错 - 改为登录注册专用发送验证码 service，返回是否需要注册。只是命名不合理
- [x] 预充状态 加上 READY
- [x] 订单未支付提醒消息
- [x] - 重复发两条
- [x] app notification 的 event_id 写成了 customerId

- TODO list
  - [x] 分发组额度 - 推送消息 - 未找到 deviceData 依然调用其推消息接口

## 出租车

### 需求

- [x] 充电完成界面的数据增加
- [x] 原发送消息 - 平台端 - 使用新的数据表
- [x] 核对 - 订单详情h5 接口 - 核对出租车数据是否满足显示
- [x] my_page 接口 添加企业类型字段 - 使用 billing_type 字符串

### debug list

- [x] 字典表字段删除确认 @ct - 不同 family
- [x] 充电完成页面接口 - 错误的 status - AUTOPAY_FAIL
- [x] notification push 缺少 title - 本地测试出现
- [x] 离开企业充电人员再去支付，同样需要显示组余额
- [x] ready 状态不算 充电中 - 离开企业 有无正在充电订单验证
- [x] 平台端 - 订单显示 4 位小数
- [x] 企业端与移动端名称限制一致为 15 位
- [x] 获取 用户token的验证其是否有效

## May 24th, 2019 压测后

### TODO list

- [ ] 场站详情接口响应时间过长 - 检查其是否不合理
- [ ] 消息列表接口
  - [ ] 更新消息状态接口
  - [ ] 列表接口更改字段中与原消息一致
- [x] 增加查询订单数量接口

## DB

- dev fake data
  - customerId 29
  - enterpriseId 1
  - driverGroupId 1
  - carId 1
  - brandTypeId 1
  - brandId 1
  - dev 账号 id：
    - 郑定明 179
    - 唐文飙 36 18328597432
    ASmdlx9HkHF7fTRLic19CqFZyw9+CQ2wadQ8AYRgCenEhhwgPnT/s84FXwN+2H9dn1HeS+D9Vqfd2zeR/OGQIklakya5V2YYJcCSW0tcOaUU
    - 韩冬 20
    - 黄佳 8
    - 扈丽霞  107
    - 康珊 29
      - evcs_client accountId:166
      - 获取 redis token: hget uz:account:166:0 token
      - 最后 0 表示channel ,0 表示entpoint_channel, 1 表示 browser_channel
      - evcs_plat accountId 19
ASmdlx/touaprrqds04w8ZtwGNTYyvYvJQ5v5uE31YKz8L2h0YLTeWg/qrjCZLvxyPz7P4Wd9DCM822JlhjzsF/H5JR97GI13V15eBOcNJ+y


708 "{"deviceType":"ios","deviceToken":"4ce12648177ac1d49d45cfa45361c7ce1b166ae0cd34a23f658d3880c21899b2"}"
687  "{"deviceType":"ios","deviceToken":"4ce12648177ac1d49d45cfa45361c7ce1b166ae0cd34a23f658d3880c21899b2"}"
683 "{"deviceType":"ios","deviceToken":"4ce12648177ac1d49d45cfa45361c7ce1b166ae0cd34a23f658d3880c21899b2"}"

- message_record
- customer
  - name 产品那边当作 nickname 可以随意编辑的
  - email
  - vehicle 相关车辆信息 ： 是否可以有多辆车

### TODO list

- [ ] @sy get usable coupons without any coupon value

### app 换肤

> 广告图片

- 测试环境新图片 https://pwpd-public-test.oss-cn-shenzhen.aliyuncs.com/public/happynewyear.png
  - 原： http://evcs-public-test.oss-cn-shenzhen.aliyuncs.com/public/020bca8e3375dd4cd50c05120c307e20.jpg
- 预发环境 https://pwpd-public-test.oss-cn-shenzhen.aliyuncs.com/public/happynewyear.png
  - 原： http://snxia-public.oss-cn-shenzhen.aliyuncs.com/public/3d5060a9-1826-4e79-85f0-458d62781843.png
- 生产： https://pwpd-public-test.oss-cn-shenzhen.aliyuncs.com/public/happynewyear.png
  - 原： bgimg_url = http://pwpd-public.oss-cn-shenzhen.aliyuncs.com/public/3d5060a9-1826-4e79-85f0-458d62781843.png    advert_type_int = 16

#### sql

```sql
update `mkt_advert` set `bgimg_url` = 'https://pwpd-public.oss-cn-shenzhen.aliyuncs.com/public/happynewyear.png', `advert_type_int` = 1
where id =2
-- 生产上原 advert_type_int = 16
```
