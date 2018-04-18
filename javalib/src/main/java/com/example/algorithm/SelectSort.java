package com.example.algorithm;

import com.example.util.Utility;

import java.util.Arrays;
import java.util.Random;

/**
 * 选择排序(Selection sort)是一种简单直观的排序算法。
 * 它的工作原理如下。
 * 首先在未排序序列中找到最小（大）元素，存放到排序序列的起始位置，
 * 然后，再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。
 * 以此类推，直到所有元素均排序完毕。
 *
 * @author VanceKing
 * @since 2018/4/18.
 */
public class SelectSort {
    public static void main(String[] args) {
        Random random = new Random();

        int[] ints = Utility.generateNumbers(random, 5, -5, 100);
        System.out.println("before: " + Arrays.toString(ints));
        selectSort(ints);
        System.out.println("after: " + Arrays.toString(ints));
    }

    public static void selectSort(int[] data) {
        if (data == null || data.length == 0) {
            return;
        }

        for (int i = 0; i < data.length - 1; i++) {
            int min = i;// 将当前下标定为最小值下标
            for (int j = i + 1; j < data.length; j++) {
                if (data[j] < data[min]) {
                    min = j;
                }
            }

            if (i != min) {
                int tmp = data[i];
                data[i] = data[min];
                data[min] = tmp;
            }
        }
    }

    public static void selectSort2(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            for (int j = i + 1; j < data.length; j++) {
                if (data[j] < data[i]) {
                    int temp = data[j];
                    data[j] = data[i];
                    data[i] = temp;
                }
            }
        }
    }
}
