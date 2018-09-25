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
- 电桩：
  - 相关的 series 与 type 字段 vendor 字段 在几张表之间有  id 冗余
  - [ ] vendor 的 qr_path 还未生成相关的 mg
  - [X] ExtendSIteDOMapper 中station result map 映射的 status 两个  枚举字段的 jdbcType 写的 ENUM 不是 varchar
    - [ ] Enum 的 jdbcType不能被 识别，不能启动项目，已更改为 VARCHAR
  - [ ] haveParkingLock 的 jdbcType 写的 TINYINT  本来是个 boolean 值 ，其中的 javaType 写的 byte（自动生成)

## 电枪相关
- [ ] 充电桩与电枪状态之间有必然的联系,电桩在离线状态，其相关的电枪是不是也应该被忽略

## 请求路径

- 其中加上 v 就访问不到但返回为 200 状态码
  - [ ] config 中的文件配置实现了没有 token 被拦截
  - [ ] 但是和 `v` 字没有关系啊？
