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
- [ ] 场站状态字段的逻辑 当为 维护中时，个数怎么显示（APP)
  - [x] 以 web 为优先，维护中状态存在，不存在告警
- [ ] 场站详情中的 营业时间没处理：包括 SiteDTO 中字段已经被注释
- [x] 场站详情: 联系电话通用，配入字典表？
- [x] 搜索 API 不分页，+ 地址

## 设置

- [ ] 电话，从后端取

- [ ] 二维码确定协议格式

## 充电订单

- [ ] 分页作好不分页参数的适配


## 我


## 分公司与城市

- [ ] 目前默认了分公司与城市之间是一对一的关系，如果实际业务不是这样，那么后面将导致一系列问题。
- [ ] getCompanyByCityId()

## 电桩：
  - 相关的 series 与 type 字段 vendor 字段 在几张表之间有  id 冗余
  - [ ] vendor 的 qr_path 还未生成相关的 mg
  - [x] ExtendSIteDOMapper 中station result map 映射的 status 两个  枚举字段的 jdbcType 写的 ENUM 不是 varchar
    - [ ] Enum 的 jdbcType不能被 识别，不能启动项目，已更改为 VARCHAR
  - [ ] haveParkingLock 的 jdbcType 写的 TINYINT  本来是个 boolean 值 ，其中的 javaType 写的 byte（自动生成)

## 电枪相关
- [ ] 充电桩与电枪状态之间有必然的联系,电桩在离线状态，其相关的电枪是不是也应该被忽略
- [ ] 电枪 状态 未放回时被当作是有空闲的元素。是可以让用户继续操作的。
- [ ] 电枪状态一直从 redisData 中获取吗？

## 分公司相关接口

- [x] 保存分公司 获取登记人信息并保存
- [x] 加解密运行报错，
    - [x] 非必要数据 的加解密的应该被 try catch 掉，不应该直接被抛出异常影响注流程的通畅。


## 请求路径

- 其中加上 v 就访问不到但返回为 200 状态码
  - [x] config 中的文件配置实现了没有 token 被拦截
  - [x] 但是和 `v` 字没有关系啊？
      - webconfig 中拦截器的实现
