---
layout: "post"
title: "flock 相关"
date: "2019-06-11 11:25"
---


## 电枪状态

```
    public static final int                         IDLE                                =1;                     //空闲
    public static final int                         LINKED                              =2;                     //已连接
    public static final int                         CHARGING                            =3;                     //充电中
    public static final int                         RETURNING                           =4;                     //返回充电枪中
    public static final int                         EXCEPTION                           =5;                     //异常
    public static final int                         OFFLINE                             =6;                     //离线
```

统一一下：
DEV = 开发、测试人员用的开发测试环境 develop
FAT = 测试人员用的测试环境 Feature acceptance testing
UAT = 预发环境 user acceptance testing
PRO = 线上环境 product



"{"evcsId":"1234740","chargHeaderId":"123474001","orderId":"1906146716009500","needCurrenct":-359.1,"needVoltage":0.8,"batteryType":1,"batteryGroupVoltage":2200.0,"batteryGroupChargeVoltage":18.0,"batteryGroupChargeCurrent":20.0,"batteryGroupChargePower":342.0,"batteryMaxTemperature":-25,"batterySerialId":0,"batteryChargeVoltage":10.2,"batteryChargeCurrent":10.2,"realBatteryMaxTemperature":69,"realBatteryMinTemperature":68,"singleMaxVoltage":0.0,"singleMaxVoltageSerialId":0,"outputVoltage":10.2,"outputCurrent":10.2,"soc":19.0,"initialSoc":17.0,"extension":{}}"


  "evcsId": "1235048",
  "chargHeaderId": "123504802",
  "orderId": "19051421127101",
  "needCurrenct": -359.1,
  "needVoltage": 0.8,
  "batteryType": 1,
  "batteryGroupVoltage": 2200.0,
  "batteryGroupChargeVoltage": 6.0,
  "batteryGroupChargeCurrent": 20.0,
  "batteryGroupChargePower": 898.0,
  "batteryMaxTemperature": 46,
  "batterySerialId": 0,
  "batteryChargeVoltage": 10.2,
  "batteryChargeCurrent": 10.2,
  "realBatteryMaxTemperature": 65,
  "realBatteryMinTemperature": 64,
  "singleMaxVoltage": 0.18,
  "singleMaxVoltageSerialId": 2,
  "outputVoltage": 10.2,
  "outputCurrent": 10.2,
  "soc": 20.0,
  "timeLeftForChargeByMin": 7,
  "initialSoc": 20.0,
  "extension": {

  }
}

## commond

D:\projects\evcs\压测\
java -Devcs.sim.rpc.server.host=10.28.16.68 -Devcs.sim.client.mac=D89C673E7D41 -jar evcs-sim-standard-gui-1.4.9-SNAPSHOT.jar
java -Devcs.sim.rpc.server.host=10.28.16.68 -Devcs.sim.client.mac=D89C673E7D42 -jar evcs-sim-standard-gui-1.4.9-SNAPSHOT.jar
java -Devcs.sim.rpc.server.host=10.28.16.68 -Devcs.sim.client.mac=D89C673E7D43 -jar evcs-sim-standard-gui-1.4.9-SNAPSHOT.jar
java -Devcs.sim.rpc.server.host=10.28.16.68 -Devcs.sim.client.mac=D89C673E7D44 -jar evcs-sim-standard-gui-1.4.9-SNAPSHOT.jar
java -Devcs.sim.rpc.server.host=10.28.16.68 -Devcs.sim.client.mac=D89C673E7D45 -jar evcs-sim-standard-gui-1.4.9-SNAPSHOT.jar

- 模拟器启动命令：siteId: 1 siteName:蚂蚁园区充电站 309 （开发环境）
java -Devcs.sim.rpc.server.host=10.28.32.205 -Devcs.sim.client.mac=075BCD15FFFF -jar evcs-sim-standard-gui-1.4.9-SNAPSHOT.jar
java -Devcs.sim.rpc.server.host=10.28.18.85 -Devcs.sim.client.mac=075BCD15FFFF -jar D:/projects/evcsevcs-sim-standard-gui-1.4.5-SNAPSHOT.jar

- siteId: 1 siteName:蚂蚁园区充电站 309 （test 环境）
java -Devcs.sim.rpc.server.host=10.28.6.50 -Devcs.sim.client.mac=075BCD15FFFF -jar evcs-sim-jiedian-gui-1.7.2-SNAPSHOT.jar
- 连接测试环境日志信息机器 查看日志
  - path       /home/admin/logs/java/snxia/snxia-api/2019-03-12
  - host 10.28.6.50  port 22  loginname loguser   password log123
  - commond ```tailf [error-log.log]```
  - cd commod ```cd admin/logs/java/snxia/snxia-api/```


## redis 查询命令：

-  查询电枪
HEXISTS EVCS:EP:HEAD_STATIONS headHardwareCode
HGET EVCS:EP:HEAD_STATIONS 201
- 查询电桩登录信息
hget EVCS:EP:AUTH stationId
- 查询正在充电实时账单信息
hget EVCS:EP:ACCOUNTING id
- 查询 BMS 数据
hget EVCS:EP:BMS billingId
