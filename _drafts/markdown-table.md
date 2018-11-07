---
layout: "post"
title: "markdown table"
tags: "markdown"
date: "2018-10-24 11:30"
---

# markdown 中 table 标签

> 在markdown文档中， <table> 这个标签的使用。分别使用 table tr th 三个标签可以实现复杂表格的绘制。


示例如下：

<table>
  <tr>
    <th>月份</th>
    <th>车辆总数</th>
    <th>事故次数</th>
    <th>事故率 %</th>
    <th>
      事故分类（次数）
      <table>
        <tr>
          <th>死亡事故</th>
          <th>重大事故</th>
          <th>一般事故</th>
          <th>轻微事故</th>
        </tr>
      </table>
    </th>
    <th>事故损失（万元）</th>
    <th>备注</th>
  </tr>
  <tr>
    <th>

    </th>
  </tr>
</table>
