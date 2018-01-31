---
title: "swagger"
date: "2017-12-14 16:00"
---

# swagger 的使用

## 示例

```
@ApiOperation(value = "获取服务器上可租用游戏列表",notes = "不需要登录")
@RequestMapping(value ="/{gameId}/lease/list",method = RequestMethod.GET,produces="application/json")
public Message<List<GameLeaseView>> getGameLeases(@ApiParam(required = true,value ="游戏id") @PathVariable Long gameId,
                                                    @ApiParam(required = true,value ="服务器名称")  String serverName,
                                                  @ApiParam(required = true,value ="区名称")  String quName,
                                                  @ApiParam(required = true,value ="当前页码")  Integer page) {
    List<GameLeaseView> gameLeaseViews = gameService.gameLeases(getUserId(),gameId,serverName,quName,page);
    return Message.success(gameLeaseViews);
}
```

## 总结

初级使用，注意这几个注解的使用：
- @ApiModel(required=boolean, notes="")
- @ApiModelProperty(required=boolean,notes="")
- @ApiOperation(value="",notes="")
- @ApiParam(required=boolean, value="参数说明")


游茜:
Hi, 后端的同学， 这里给大家分享下swagger的文档的一个Tip哈：
关于 ApiModel和ApiModelProperty 这两个标签的
游茜:
1. @ApiModel， 标注在Request/Response对象的class上， 表示这是一个模型对象(比如我们项目中的Vo/Dto)
游茜:
2. @ApiModelProperty， 标注在对象ApiModel的属性上， 它本身有几个重要的值需要考虑设置：
notes: String, 一般必须标上中文意义；
required: 布尔值， 表示是否必须， 尤其是Reqeust对象， 可以告知前端同学此项是否必填；
position: 整数值， 属性出现在文档中的位置排序， 针对大对象可以考虑使用， 方便文档查看；
游茜:
示例：
@ApiModel
public class BaseResult {
    /**
     * 返回信息
     * */
    @ApiModelProperty(required = false, position = 1, notes = "返回消息内容")
    protected String  message;

    /**
     * 结果提示，ok：代表成功
     * @see {ResultEnum}
     * */
    @ApiModelProperty(required = true, position = 0, notes = "返回消息代码")
    protected String result;
...
}
