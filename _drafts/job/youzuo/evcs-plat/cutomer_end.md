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





## TODO
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



## DB

- message_record
- customer
  - name 产品那边当作 nickname 可以随意编辑的
  - email
  - vehical 相关车辆信息 ： 是否可以有多辆车

### sql

```
-- custome_account 添加邮箱字段
ALTER TABLE `ev_customer_account`
ADD COLUMN `email`  varchar(64) NULL COMMENT '用户邮箱' AFTER `mobile_phone_no_secret`;

```




## 项目规范

 1. 所有Web调用Module当中Service参数必须为RequestDto,命名强制为RequestDto结尾
 2. 所有Module当中Service返回必须为ResponseDto,命名强制要求为ResponseDto
 3. Web层当中返回前端数据必须为Vo，命名强制为Vo结尾
 4. Module当中Service方法必须做参数强验证

 代码实例如下

```
public class OrderRequest {
    private Integer orderId;
    private Double price;

    public OrderRequestDto toRequestDto() {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        return orderRequestDto;
    }
}

public class OrderVo {
    private Integer orderId;
    private Double price;

    public OrderVo fromResponseDto(OrderResponseDto orderResponseDto) {
        return this;
    }
}

public class Demo {
    private OrderService orderService;

    public UzResult<OrderVo> getOrderList(OrderRequest orderRequest) {
        OrderVo orderVo = new OrderVo();
        OrderRequestDto orderRequestDto = orderRequest.toRequestDto();
        OrderResponseDto orderResponDto = orderService.getOrderList(orderRequestDto);
        return new UzResult<>(orderVo.fromResponseDto(orderResponDto););
    }

}

public class OrderService {
    OrderResponseDto getOrderList(OrderRequestDto orderRequestDto) {
        Assert.NotNull(orderRequestDto.getOrderId());
        return new OrderResponseDto();
    }
}

public class OrderRequestDto {
    private Integer orderId;
    private Double price;
    private Integer userId;
}

public class OrderResponseDto {
    private Integer orderId;
    private Double price;
    private Integer userId;
}
```
