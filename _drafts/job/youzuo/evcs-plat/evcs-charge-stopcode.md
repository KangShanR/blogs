---
layout: "post"
title: "evcs_charge_stopCode"
date: "2019-01-26 15:19"
---

## 远程 stopCode 及对应的描述与归纳

errorCodeMap.putIfAbsent(0x00, "IC 卡内余额不足");
errorCodeMap.putIfAbsent(0x01, "账户余额不足");
errorCodeMap.putIfAbsent(0x02, "用户拔抢操作");
errorCodeMap.putIfAbsent(0x03, "电池充满电");
errorCodeMap.putIfAbsent(0x04, "用户第二次刷卡");
errorCodeMap.putIfAbsent(0x05, "停止按钮按下");
errorCodeMap.putIfAbsent(0x06, "桩端急停按钮按下");
errorCodeMap.putIfAbsent(0x07, "时钟故障停止");
errorCodeMap.putIfAbsent(0x08, "充电机柜门打开停止");
errorCodeMap.putIfAbsent(0x09, "BRM 报文超时停止");
errorCodeMap.putIfAbsent(0x0a, "BCP 报文超时停止");
errorCodeMap.putIfAbsent(0x0b, "BR0 报文超时停止");
errorCodeMap.putIfAbsent(0x0c, "BCL 报文超时停止");
errorCodeMap.putIfAbsent(0x0d, "BST 报文超时停止");
errorCodeMap.putIfAbsent(0x0e, "BSD 报文超时停止");
errorCodeMap.putIfAbsent(0x0f, "停电");
errorCodeMap.putIfAbsent(0x10, "绝缘未通过,请检修");
errorCodeMap.putIfAbsent(0x11, "电池电压过高");
errorCodeMap.putIfAbsent(0x12, "输入过压停止");
errorCodeMap.putIfAbsent(0x13, "输入欠压停止");
errorCodeMap.putIfAbsent(0x14, "电池反接");
errorCodeMap.putIfAbsent(0x15, "电池电压过低");
errorCodeMap.putIfAbsent(0x16, "输出电压过高");
errorCodeMap.putIfAbsent(0x17, "BCS 报文超时停止");
errorCodeMap.putIfAbsent(0x18, "电子锁未就绪");
errorCodeMap.putIfAbsent(0x19, "BHM 报文超时停止");
errorCodeMap.putIfAbsent(0x1a, "绝缘检测前外部电压异常");
errorCodeMap.putIfAbsent(0x1b, "输出短路保护");
errorCodeMap.putIfAbsent(0x1c, "反接保护");
errorCodeMap.putIfAbsent(0x1d, "充电机未就绪");
errorCodeMap.putIfAbsent(0x1e, "继电器粘连");
errorCodeMap.putIfAbsent(0x1f, "平台远程停止");

appErrorCodeMap.putIfAbsent(0x00, "");
appErrorCodeMap.putIfAbsent(0x01, "余额不足,自动结束");
appErrorCodeMap.putIfAbsent(0x02, "主动拔枪异常结束");
appErrorCodeMap.putIfAbsent(0x03, "电池充满,自动结束");
appErrorCodeMap.putIfAbsent(0x04, "");
appErrorCodeMap.putIfAbsent(0x05, "APP端主动结束");
appErrorCodeMap.putIfAbsent(0x06, "电桩急停按钮按下结束");
appErrorCodeMap.putIfAbsent(0x07, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x08, "电桩操作异常，自动结束");
appErrorCodeMap.putIfAbsent(0x09, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x0a, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x0b, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x0c, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x0d, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x0e, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x0f, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x10, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x11, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x12, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x13, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x14, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x15, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x16, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x17, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x18, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x19, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x1a, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x1b, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x1c, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x1d, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x1e, "电桩故障，自动结束");
appErrorCodeMap.putIfAbsent(0x1f, "平台远程停止");



  RemoteChargeEndType:{
    "insufficientFunds":["1"],
    "fullChargeAutoStop":["3"],
    "icCardReason":["0","4"],
    "terminatedOnApp":["5"],
    "stationException":["2", "6", "7", "8", "9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"]
  }
