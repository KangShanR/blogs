# 项目笔记

## 场站相关

- 场站 id 判定是否有空闲
- 空闲、非空闲、暂停服务 场站三种状态
- 场站列表筛选条件：充电方式只选一个，直流 交流
- 联系电话字段 没有 从leader 找
- 可充电车型 不做先
- 枪的列表考虑做分页处理 先不做
- 电枪列表
    - 大部分数据从电桩 type 或 series 相关表中取，状态除外，SOC 从 redis 取
    - 编号直接取 桩系统编号 与枪的 code
    - SiteDTO 继承自 SiteDO ，重写了其 position 字段，用了 @Data 标签 @EqualsAndHashcode 标签
- [x] 场站状态字段的逻辑 当为 维护中时，个数怎么显示（APP)
    - [x] 以 web 为优先，维护中状态存在，不存在告警
- [x] 场站详情中的 营业时间没处理：包括 SiteDTO 中字段已经被注释
- [x] 场站详情: 联系电话通用，配入字典表？
- [x] 搜索 API 不分页，+ 地址
- [x] 场站电枪数量 快充与 慢充数量放反 : 格式化代码出错
- [x] 查找两个接口 将 快充慢充单拎出来
- [x] 场站详情 枪 数量 与 列表 数据不匹配:service 接口先取了桩的状态再取了枪的状态导致两个接口不一致的数据源
- [x] 格式化 distance 单位、小数保留 、soc 小数保留
- [x] 提醒 app 同事：场站详情 页面接口更改数据结构:改回来
    - [x] 场站列表接口（包括地图）正常状态下场站无电枪，则直接当作创建中的状态给屏蔽掉

## 设置

- [x] 电话，从后端取
- [ ] 二维码确定协议格式

## 充电订单

- [x] 分页作好不分页参数的适配

## 分公司与城市

- [x] 目前默认了分公司与城市之间是一对一的关系，如果实际业务不是这样，那么后面将导致一系列问题。
    - 实际业务两者之间没有必然联系，且地图中显示只显示城市的不关乎分公司。
- [x] getCompanyByCityId()

## 电桩

- 相关的 series 与 type 字段 vendor 字段 在几张表之间有 id 冗余
- [ ] vendor 的 qr_path 还未生成相关的 mg
- [x] ExtendSIteDOMapper 中station result map 映射的 status 两个  枚举字段的 jdbcType 写的 ENUM 不是 varchar
    - [x] Enum 的 jdbcType不能被 识别，不能启动项目，已更改为 VARCHAR
- [ ] haveParkingLock 的 jdbcType 写的 TINYINT  本来是个 boolean 值 ，其中的 javaType 写的 byte（自动生成)

## 电枪相关

- [x] 充电桩与电枪状态之间有必然的联系,电桩在离线状态，其相关的电枪是不是也应该被忽略
    - [x] 状态只取枪的状态，而且枪的状态从 redis 取
- [x] 电枪 状态 未放回时被当作是有空闲的元素。是可以让用户继续操作的。
- [x] 电枪状态一直从 redisData 中获取吗？
    - yes 所有的都从这 redis 里去取
- [ ] 电流类型当前只有两种，如果出现 交流直流一体，怎么决定电枪的快慢格式化
- [x] SOC 类型是 double 还是 int： double

## 分公司相关接口

- [x] 保存分公司 获取登记人信息并保存
- [x] 加解密运行报错，
    - [x] 非必要数据 的加解密的应该被 try catch 掉，不应该直接被抛出异常影响主流程的通畅。

## 请求路径

- 其中加上 v 就访问不到但返回为 200 状态码
    - [x] config 中的文件配置实现了没有 token 被拦截
    - [x] 但是和 `v` 字没有关系啊？
        - webconfig 中拦截器的实现

## 大改

- [x] redis 数据取真实值
    - [x] 正在充电列表接口
        - [x] 数据格式化保留两位小数
    - [x] 充电详情接口
        - [x] DTO 中数据 改 string 为 double
        - [x] 电量消费使用 redis 数据
        - [x] 数据格式化保留两位小数
        - [x] 停车费 0 改为免费
        - [x] 计价详情页 同上
    - [x] 没拿到数据时的 格式化检查
    - [x] 所有涉及到电枪状态的接口
        - [x] 场站详情
        - [x] 场站列表
        - [x] 地图场站
    - [x] SOC 相关的接口
        - [x] 电枪列表
        - [x] 等待充电
            - [x] kw 格式化
    - [ ] 确认获取不到 redis 数据时，业务系统如何处理
        - [x] 当枪获取不到时，确认为离线
        - [ ] 当正在充电订单获取不到时，日志记录并确认 bug
    - [x] 获取枪的状态切换
        - siteServiceImpl.convertStationToHeads() line 507

## TODO list

- [x] 查找场站，状态筛选
- [x] 充电 详情 redis 数据 电费 获取错误
- [x] 充电详情  充电时长
- [x] 充电结束怎么返回？
    - 返回状态让客户端进行判断
- [x] @hy 确定 7 日内成功充电次数统计是否包括当天？
    - [x] 修改 7 日内成功充电 h5 接口 Mapper 查询语句

## debug

- [x] 场站相关 - 二期已处理完
    - [x] 场站无计费规则时和处理：过滤掉该场站并日志记录
        - [x] 场站列表
        - [x] 首页地图场站显示
        - [x] 搜索场站
- [ ] 充电相关
    - [ ] 开始充电后，偶发场站全部的电枪状态为 故障
- [x] 计费相关
    - [x] 丢失电费计价详情。
- [x] 正在充电中的功率取 ACCOUNTING 中的 outputPower 单元为 KW
    - [x] 使用移动端版本号来区分
- [x] 确定正在充电的数据取新的字段
- [x] 结束充电消息根据订单是否支付推送不同信息
- [x] event_id 未给到引起的脏数据 - 软删除 BILLING_PAYMENT 类型的 notification

### v1.2.4 TODO list

> v1.2.4 版本

- [x] 企业充值记录列表
- [ ] 1、新增财务管理模块、账户明细页面；2、账户明细页面显示平台所有用户的账户明细信息，分为普通用户[个人用户和普通企业用户]和出租车企业用户

### app v1.2.4 api TODO list

> July 9th, 2019

- [ ] 删除 ev_site_elec_meter_reading 表相关代码
- [x] 兼容第三方硬件数据
    - [x] 正在充电详情
        - [x] 同时加上场站来源字段
    - [x] 订单详情

### 移动端分享版本 TODO list

- [x] debug - siteServiceImpl line 852 bug IndexOutOfBoundException
- [x] IBillingRecordService line 240  will always return false @ct
- [x] 登录时确定是否如果未带 umengToken 将其设备关系转为无效
    - [x] 已确定，需要无效
- [x] 场站列表 - 收藏是否需要筛选，如果要，确定没有收藏与筛选后结果区分策略 - 不需要
- [x] 消息列表订单相关
    - [x] 订单的推送弹窗
    - [x] 列表都跳转到原生结算页面
    - [x] 跳转使用 schema 格式放到 url 字段 - evcs://snxia.com/orderDetail?orderId=1
    - [x] 订单未支付消息缺少 eventId 的脏数据
- [x] 充电中页面 - 增加状态 Charging_offline - 兼容老版本
- [x] 场站计费规则新接口
- [x] 场站是否收藏接口
- [x] 场站详情 h5 接口
- [x] 修改充电完成消息方案 - 去掉场站两个字

application 中添加：

- key assign.freeSite.ids
- value [68, 77, 62, 60, 53, 64]

- key assign.sites.data
- value [{"id":13,"dcHeadAmount":20,"dcFreeHeadAmount":20},{"id":14,"dcHeadAmount":36,"dcFreeHeadAmount":36},{"id":2,"dcHeadAmount":13,"dcFreeHeadAmount":13},{"id":3,"dcHeadAmount":10,"dcFreeHeadAmount":10}]
