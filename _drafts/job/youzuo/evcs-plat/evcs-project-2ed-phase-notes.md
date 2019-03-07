---
layout: "post"
title: "evcs_project_2ed_phase_notes"
date: "2018-12-13 16:52"
---


# 电桩项目二期

## 消息推送

android 推送请求参数：
```
{
    "appkey":"xx",        // 必填，应用唯一标识
    "timestamp":"xx",    // 必填，时间戳，10位或者13位均可，时间戳有效期为10分钟
    "type":"xx",        // 必填，消息发送类型,其值可以为:
                        //   unicast-单播
                        //   listcast-列播，要求不超过500个device_token
                        //   filecast-文件播，多个device_token可通过文件形式批量发送
                        //   broadcast-广播
                        //   groupcast-组播，按照filter筛选用户群, 请参照filter参数
                        //   customizedcast，通过alias进行推送，包括以下两种case:
                        //     - alias: 对单个或者多个alias进行推送
                        //     - file_id: 将alias存放到文件后，根据file_id来推送
    "device_tokens":"xx",    // 当type=unicast时, 必填, 表示指定的单个设备
                            // 当type=listcast时, 必填, 要求不超过500个, 以英文逗号分隔
    "alias_type": "xx",    // 当type=customizedcast时, 必填
                        // alias的类型, alias_type可由开发者自定义, 开发者在SDK中
                        // 调用setAlias(alias, alias_type)时所设置的alias_type
    "alias":"xx",        // 当type=customizedcast时, 选填(此参数和file_id二选一)
                        // 开发者填写自己的alias, 要求不超过500个alias, 多个alias以英文逗号间隔
                        // 在SDK中调用setAlias(alias, alias_type)时所设置的alias
    "file_id":"xx",    // 当type=filecast时，必填，file内容为多条device_token，以回车符分割
                    // 当type=customizedcast时，选填(此参数和alias二选一)
                    //   file内容为多条alias，以回车符分隔。注意同一个文件内的alias所对应
                    //   的alias_type必须和接口参数alias_type一致。
                    // 使用文件播需要先调用文件上传接口获取file_id，参照"文件上传"
    "filter":{},    // 当type=groupcast时，必填，用户筛选条件，如用户标签、渠道等，参考附录G。



    "payload": {    // 必填，JSON格式，具体消息内容(Android最大为1840B)
        "display_type":"xx",    // 必填，消息类型: notification(通知)、message(消息)
        "body": {    // 必填，消息体。
                // 当display_type=message时，body的内容只需填写custom字段。
                // 当display_type=notification时，body包含如下参数:
            // 通知展现内容:
            "ticker":"xx",    // 必填，通知栏提示文字
            "title":"xx",    // 必填，通知标题
            "text":"xx",    // 必填，通知文字描述

            // 自定义通知图标:
            "icon":"xx",    // 可选，状态栏图标ID，R.drawable.[smallIcon]，
            // 如果没有，默认使用应用图标。
            // 图片要求为24*24dp的图标，或24*24px放在drawable-mdpi下。
            // 注意四周各留1个dp的空白像素
            "largeIcon":"xx",    // 可选，通知栏拉开后左侧图标ID，R.drawable.[largeIcon]，
            // 图片要求为64*64dp的图标，
            // 可设计一张64*64px放在drawable-mdpi下，
            // 注意图片四周留空，不至于显示太拥挤
            "img": "xx",    // 可选，通知栏大图标的URL链接。该字段的优先级大于largeIcon。
                            // 该字段要求以http或者https开头。

            // 自定义通知声音:
            "sound": "xx",    // 可选，通知声音，R.raw.[sound]。
                            // 如果该字段为空，采用SDK默认的声音，即res/raw/下的
                            // umeng_push_notification_default_sound声音文件。如果
                            // SDK默认声音文件不存在，则使用系统默认Notification提示音。

            // 自定义通知样式:
            "builder_id": xx,    // 可选，默认为0，用于标识该通知采用的样式。使用该参数时，
                                // 开发者必须在SDK里面实现自定义通知栏样式。

            // 通知到达设备后的提醒方式，注意，"true/false"为字符串
            "play_vibrate":"true/false",    // 可选，收到通知是否震动，默认为"true"
            "play_lights":"true/false",        // 可选，收到通知是否闪灯，默认为"true"
            "play_sound":"true/false",        // 可选，收到通知是否发出声音，默认为"true"

            // 点击"通知"的后续行为，默认为打开app。
            "after_open": "xx",    // 可选，默认为"go_app"，值可以为:
                                //   "go_app": 打开应用
                                //   "go_url": 跳转到URL
                                //   "go_activity": 打开特定的activity
                                //   "go_custom": 用户自定义内容。
            "url": "xx",    // 当after_open=go_url时，必填。
                            // 通知栏点击后跳转的URL，要求以http或者https开头
            "activity":"xx",    // 当after_open=go_activity时，必填。
                                // 通知栏点击后打开的Activity
            "custom":"xx"/{}    // 当display_type=message时, 必填
                                // 当display_type=notification且
                                // after_open=go_custom时，必填
                                // 用户自定义内容，可以为字符串或者JSON格式。
        },
        extra:{    // 可选，JSON格式，用户自定义key-value。只对"通知"
                // (display_type=notification)生效。
                // 可以配合通知到达后，打开App/URL/Activity使用。
            "key1": "value1",
            "key2": "value2",
            ...
        }
    },
    "policy":{    // 可选，发送策略
        "start_time":"xx",    // 可选，定时发送时，若不填写表示立即发送。
                            // 定时发送时间不能小于当前时间
                            // 格式: "yyyy-MM-dd HH:mm:ss"。
                            // 注意，start_time只对任务类消息生效。
        "expire_time":"xx",    // 可选，消息过期时间，其值不可小于发送时间或者
                            // start_time(如果填写了的话)，
                            // 如果不填写此参数，默认为3天后过期。格式同start_time
        "max_send_num": xx,    // 可选，发送限速，每秒发送的最大条数。最小值1000
                            // 开发者发送的消息如果有请求自己服务器的资源，可以考虑此参数。
        "out_biz_no": "xx"    // 可选，开发者对消息的唯一标识，服务器会根据这个标识避免重复发送。
                            // 有些情况下（例如网络异常）开发者可能会重复调用API导致
                            // 消息多次下发到客户端。如果需要处理这种情况，可以考虑此参数。
                            // 注意, out_biz_no只对任务类消息生效。
    },
    "production_mode":"true/false",    // 可选，正式/测试模式。默认为true
                                    // 测试模式只会将消息发给测试设备。测试设备需要到web上添加。
                                    // Android: 测试设备属于正式设备的一个子集。
    "description": "xx",    // 可选，发送消息描述，建议填写。
    "mipush": "true/false",    // 可选，默认为false。当为true时，表示MIUI、EMUI、Flyme系统设备离线转为系统下发
    "mi_activity": "xx",    // 可选，mipush值为true时生效，表示走系统通道时打开指定页面acitivity的完整包路径。
}
```

ios 推送请求参数：
```
{
  "appkey":"xx",    // 必填，应用唯一标识
  "timestamp":"xx", // 必填，时间戳，10位或者13位均可，时间戳有效期为10分钟
  "type":"xx",      // 必填，消息发送类型,其值可以为:
                    //   unicast-单播
                    //   listcast-列播，要求不超过500个device_token
                    //   filecast-文件播，多个device_token可通过文件形式批量发送
                    //   broadcast-广播
                    //   groupcast-组播，按照filter筛选用户群, 请参照filter参数
                    //   customizedcast，通过alias进行推送，包括以下两种case:
                    //     - alias: 对单个或者多个alias进行推送
                    //     - file_id: 将alias存放到文件后，根据file_id来推送
  "device_tokens":"xx", // 当type=unicast时, 必填, 表示指定的单个设备
                        // 当type=listcast时, 必填, 要求不超过500个, 以英文逗号分隔
  "alias_type": "xx", // 当type=customizedcast时, 必填
                      // alias的类型, alias_type可由开发者自定义, 开发者在SDK中
                      // 调用setAlias(alias, alias_type)时所设置的alias_type
  "alias":"xx", // 当type=customizedcast时, 选填(此参数和file_id二选一)
                // 开发者填写自己的alias, 要求不超过500个alias, 多个alias以英文逗号间隔
                // 在SDK中调用setAlias(alias, alias_type)时所设置的alias
  "file_id":"xx", // 当type=filecast时，必填，file内容为多条device_token，以回车符分割
                  // 当type=customizedcast时，选填(此参数和alias二选一)
                  //   file内容为多条alias，以回车符分隔。注意同一个文件内的alias所对应
                  //   的alias_type必须和接口参数alias_type一致。
                  // 使用文件播需要先调用文件上传接口获取file_id，参照"2.4文件上传接口"
  "filter":{}, // 当type=groupcast时，必填，用户筛选条件，如用户标签、渠道等，参考附录G。



  "payload":   // 必填，JSON格式，具体消息内容(iOS最大为2012B)
  {
    "aps":      // 必填，严格按照APNs定义来填写
    {
        "alert":""/{ // 当content-available=1时(静默推送)，可选; 否则必填。
                     // 可为JSON类型和字符串类型
            "title":"title",
            "subtitle":"subtitle",
            "body":"body"
        }
        "badge": xx,           // 可选
        "sound": "xx",         // 可选
        "content-available":1  // 可选，代表静默推送
        "category": "xx",      // 可选，注意: ios8才支持该字段。
    },
    "key1":"value1",       // 可选，用户自定义内容, "d","p"为友盟保留字段，
                           // key不可以是"d","p"
                           // value 不能用 json
    "key2":"value2",
    ...
  },
  "policy":               // 可选，发送策略
  {
      "start_time":"xx",   // 可选，定时发送时间，若不填写表示立即发送。
                           // 定时发送时间不能小于当前时间
                           // 格式: "yyyy-MM-dd HH:mm:ss"。
                           // 注意，start_time只对任务生效。
      "expire_time":"xx",  // 可选，消息过期时间，其值不可小于发送时间或者
                           // start_time(如果填写了的话),
                           // 如果不填写此参数，默认为3天后过期。格式同start_time
      "out_biz_no": "xx"   // 可选，开发者对消息的唯一标识，服务器会根据这个标识避免重复发送。
                           // 有些情况下（例如网络异常）开发者可能会重复调用API导致
                           // 消息多次下发到客户端。如果需要处理这种情况，可以考虑此参数。
                           // 注意，out_biz_no只对任务生效。
      "apns_collapse_id": "xx" // 可选，多条带有相同apns_collapse_id的消息，iOS设备仅展示
                               // 最新的一条，字段长度不得超过64bytes
  },
  "production_mode":"true/false" // 可选，正式/测试模式。默认为true
                                 // 测试模式只会将消息发给测试设备。测试设备需要到web上添加。
  "description": "xx"      // 可选，发送消息描述，建议填写。
}
```

**两端请求参数有所不同**

当前确定的：
- Android 确定使用 display_type 为 message
  - 内容使用 custom key 定制消息内容
- IOS 使用 静默方式 推送消息
  - 内容使用 custom 作为 key


新接口：
- 充满预提醒消息
  - 充满前 10% 时提醒
  - 如果使用心跳包提醒则需要作好标记，是否已经提醒过如果 没有提醒过则发出消息，有过则不再 提醒，必须要给 一个检查。


Umeng token:
- mine 0e51e7da7ee83d1f0a7d5e234af7431c714d8d6aa57eea19239db600e78585ff
- mine_production 1c1092eda73ffbba46ae4d74bd4f8dd1ee64bf5460215e401864e0db2cae5772
- zw 1f39e88eca11d92cdeb62f1b8a615fb274befe61b585e2454d208bc22bfc24ca
- zw_production bdfe8bc00266949aac6b1c3f2188c02ce30a37771b4a185618e4c0f489684e2f
- wg ApHwpA4XJtVQoctYV8fS5C9LvhTeiL_SyXeKMMovEC7y


token:
APP

ASmdlx/7aMHZ3LXmhViQKIc2KsBpaQX9EWR+oJDeYWZ0Kbdw8j7OilUk8OE0q1zSPROh1H5RbwzKyIGZIKRmQkQnexKi9X0JLf7JJI7fE8pE

web

ASmdlx9KAKAHXDp29R5t+ZHZtwJ7eb2svg7VttWNPkHDT6UFyblvGwaCi3Ty3VmetAkC/lis8iVsOptl6OJOGU0=

ASmdlx9Lhj5Ow0CFbCTdW1TR0QbwaSH9Q1VxhZhRM2Adu5bS2R1HbDHb7+pG4R7Ce/6Gf4YEkEPM73DUYgyS86A=


[Umeng](https://www.umeng.com)
- account：uz@uzbus.com
- password：@n9lsX5K^Jge!O92

友盟消息推送 key：
  - title             string   标题
  - body              string   消息内容
  - time              long     消息发送时间
  - receiveUid        long     接收用户ID
  - event             string   事件类型（比如 充电完成"CHARGE_COMPLETE"）
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
    - [ ] 验证码 单独服务，保留字段：时间/ip（从 request 获取）/code
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
  - [ ] 企业管理员目前没有删除企业充电用户逻辑
  - [x] 等待充电接口 - 国标字段未处理
  - [ ] 消息推送加数据库记录
  - [ ] 移动端企业充电权限使用字典表数据
    - [ ] 不限制


## new idea

- 项目模块设计为：单个模块为一整个模块，而不是整个 dao service 层成为一个模块
- 最好不要多表查询，使用 JOIN ，就算有分页查询也先查出所有的数据再在内存来 lambda 来实现分页


## 企业端/平台端 测试用例评审

> Jan 17th, 2019

初始化企业管理账号，超管与二级角色同时给。
- 开始充电：枪 -> 用户权限状态 -> 钱包状态


## 企业端相关 数据库设计

待处理：
- [ ] 生成 model 需要加 en 在前吗？
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
