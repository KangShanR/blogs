---
layout: "post"
date: "2018-10-11 20:11"
tag: "java, BigDecimal"
---

# BigDecimal

## methods

- 四舍五入
    - BigDecimal.ROUND_UP 进位，保留后的位数往上增，如果是负数，则相反成为保留为更小的数。
    - BigDecimal.ROUND_DOWN 下降位，保留后的位数后往下减，如果是负数，则保留为更大的数。
    - BigDecimal.ROUND_CEILING 进位，保留后的位数往上增，不管是正数还是负数。
    - BigDecimal.ROUND_FLOOR ，保留后的位数往下减，不管是正数还是负数。

**note**:UP 模式与 CEILING 模式的区别：看英文单词意思，可以看出来， CEILING 只会往大了加，不管是正数还是负数，而 UP 会读取到 数的正负方向，朝着数的方向增加。如：

```java
new BigDecimal(4.03).setScale(1, BigDecimal.ROUND_UP)//结果是： 4.1
new BigDecimal(-4.03).setScale(1, BigDecimal.ROUND_UP)//结果是： -4.1

new BigDecimal(4.03).setScale(1, BigDecimal.ROUND_CEILING)//结果是： 4.1
new BigDecimal(-4.03).setScale(1, BigDecimal.ROUND_CEILING)// 结果是： -4.0
```

DOWN 模式 与 FLOOR 模式的区别则同理可得。

- compare
    - 在使用中，从数据库取出来的  数据 其 stringCache 为四位小数 比如： 0 就会被记为 0.0000 ，这个时候如果使用 BigDecimal.ZERO 与 取出来的数据 `0` 进行 equals 方法比较，返回结果就是 false 。如果要实现值的正常比较，就应该使用 BigDecimal.compare() 方法 根据其返回 int 数据来进行判断比较。
- _已不推荐使用此种取小数位数方式，推荐使用 `java.math.RoundingMode`_  
