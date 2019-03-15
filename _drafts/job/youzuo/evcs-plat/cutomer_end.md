---
layout: "post"
title: "c 端"
date: "2019-03-07 14:35"
---

# 电桩项目 c 端


```

/***
 1.所有Web调用Module当中Service参数必须为RequestDto,命名强制为RequestDto结尾
 2.所有Module当中Service返回必须为ResponseDto,命名强制要求为ResponseDto
 3.Web层当中返回前端数据必须为Vo，命名强制为Vo结尾
 4.Module当中Service方法必须做参数强验证
 代码实例如下
 ***/

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
