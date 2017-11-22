---
title: 快速排序算法
date: 2016-12-03 12:54:38
categories: programming
tags: [programming,算法,java]
keywords: 快速排序
description: 用递归实现最高效的排序算法
---

# 快速排序算法
> 复习了之前学习C#时接触到的快速排序算法

<!-- TOC depthFrom:1 depthTo:3 withLinks:1 updateOnSave:1 orderedList:1 -->

1. [快速排序算法](#快速排序算法)
	1. [今日所得](#今日所得)

<!-- /TOC -->
<!--more-->
## 今日所得

- 今天复习了下**快速排序算法**（使用递归），据说是效率最高的一种排序算法（相比于冒泡排序、选择排序）；
- 之前学习C#时领教过，当想把这个算法迁移到java中来写一个时发现久了不练都手生了；
1. 前面多次使用递归的算法写一些方法（诸如二分法查找已经排好序数组元素的索引、递归生成满足条件（非纯数字或字母）的字符串；使用多次但没有想到过使用递归时可以无返回值；
2. 二分排序时，必须使用无返回值的递归方法。因为二分之后返回的是数组的一部分，最后把这一部分返回给递归前方法，这时要接收这一部分的元素需要对这一部分进行遍历；
3. 二分法排序(升序，若要降序则相反）流程：
	1. 把数组第一个元素作为中间值，从最大索引(这个还必须从后往前找，因为我们用的第一个元素作为中间值，如果从前往后找，第一个较大值会直接覆盖最大索引的值；所以，如果想实现从前往后找就得把最大索引元素值作为中间值)开始往前找，找到比这个间值小的元素就把这个值赋予给最小索引的元素；
	2. 再从最小索引元素开始往后找，找到这中间值大的元素，把这个元素值赋予给最大索引处的元素（找的过程不满足条件就把索引依次自增或自减）；
	3. 依次这样后面往前找一趟，前面往后找一趟，直到最大与最小索引相等。这时把中间值赋予给这个索引，这样就完成了一次把数组分成两个部分，前面部分值比中间值小，后面部分比中间值大；
	4. 这用上面一套流程对中间值两边的元素进行递归操作，递归缺口就是数组操作部分只有一个元素；
	5. 代码：
```
			public int getPivotIndex(int[] arr, int minIndex, int maxIndex){
				int pivot = arr[minIndex];
				while (minIndex < maxIndex) {
					while (arr[maxIndex] >= pivot && minIndex < maxIndex) {
						maxIndex--;
					}
					if(minIndex != maxIndex){
						arr[minIndex] = arr[maxIndex];
					}
					if(minIndex != maxIndex){
						arr[minIndex] = arr[maxIndex];
					}
					while (arr[minIndex] <= pivot && minIndex < maxIndex) {
						minIndex++;
					}
					arr[maxIndex] = arr[minIndex];
				}
				arr[minIndex] = pivot;
				return minIndex;
			}
			 *二分法排序的流程
			 * @param arr
			 * @param minIndex
			 * @param maxIndex
			 */
		    public void dichotomySort(int[] arr, int minIndex, int maxIndex){
				if(minIndex < maxIndex){
					int index = this.getPivotIndex(arr, minIndex, maxIndex);
					dichotomySort(arr, minIndex, index - 1);
					dichotomySort(arr, index + 1, maxIndex);
				}else{
					return;
				}
			}
```

4.  二分法排序算法核心在于每一次查找把中间那一个值给定下来，并对中间值两边的数组分别再次排序,再对两边的数组分别进行同样的操作，直到不能再分割；

------------
_note:_ **注意算法的流程**
