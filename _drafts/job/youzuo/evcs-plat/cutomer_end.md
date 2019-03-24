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
