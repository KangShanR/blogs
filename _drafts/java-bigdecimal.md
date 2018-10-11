---
layout: "post"
title: "java_Bigdecimal"
date: "2018-10-11 20:11"
---

# Bigdecimal

## 四舍五入

- Bigdecimal.ROUND_UP 进位，保留后的位数往上增，如果是负数，则相反成为保留为更小的数。
- Bigdecimal.ROUND_DOWN 下降位，保留后的位数后往下减，如果是负数，则保留为更大的数。
- Bigdecimal.ROUND_CEILING 进位，保留后的位数往上增，不管是正数还是负数。
- Bigdecimal.ROUND_FLOOR ，保留后的位数往下减，不管是正数还是负数。

**note**:UP 模式与 CEILING 模式的区别：看英文单词意思，可以看出来， CEILING 只会往大了加，不管是正数还是负数，而 UP 会读取到 数的正负方向，朝着数的方向增加。如：
```
new Bigdecimal(4.03).setScale(1, Bigdecimal.ROUND_UP) 结果是： 4.1
new Bigdecimal(-4.03).setScale(1, Bigdecimal.ROUND_UP) 结果是： -4.1

new Bigdecimal(4.03).setScale(1, Bigdecimal.ROUND_CEILING) 结果是： 4.1
new Bigdecimal(-4.03).setScale(1, Bigdecimal.ROUND_CEILING) 结果是： -4.0
```
DOWN 模式 与 FLOOR 模式的区别则同理可得。
