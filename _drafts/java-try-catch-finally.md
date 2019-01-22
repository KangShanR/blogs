---
layout: "post"
title: "java-try-catch-finally"
date: "2019-01-22 12:45"
---

# java try

> 在 java 程序中，try catch finally 的使用执行顺序：

1. 只要有 finally 是不是其中的代码就一定会执行？
  1. 不一定，当在 try 的代码块中存在着让程序停止的代码，那么就不会执行到 finally 中去。比如： `System.exit();`
2. 当 try 代码块进行了 return 后，在 finally 中再进行 return 一个新的值，将会以 finally 中的值为最终值。
3. 当 try 块中进行了运算一个值，再在 finally 中去进行运算并返回，最终值通常理解为是两次运算都进行的结果，但如果这个值是一个基本类型的数据，结果是在 try 中进行的运算可能无效的。
  1. 原因：如果这个值是一个基本类型时，如果 **没有将这个值进行 return** ，那么运算结果并没有被赋予到之前方法的形参栈帧上去，而这个时候直接到 finally 代码块中进行运算，就直接取到原方法未被赋结果值的参数，这个时候就会出现 try 代码块不被执行的结果。
