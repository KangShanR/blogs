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
