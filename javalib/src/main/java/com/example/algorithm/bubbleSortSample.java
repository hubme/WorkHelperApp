package com.example.algorithm;

/**
 * 冒泡排序（Bubble Sort）是一种简单的排序算法。
 * 它重复地走访过要排序的数列，一次比较两个元素，如果他们的顺序错误就把他们交换过来。
 * 走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。
 * 这个算法的名字由来是因为越小的元素会经由交换慢慢“浮”到数列的顶端。
 *
 * @author VanceKing
 * @since 2018/4/14 0014.
 */
public class bubbleSortSample {
    public static void main(String[] args) {
        int[] array = {0, 10, 88, -20, 500, 88, 60};

        printArray(array);
        bubbleSort(array);

        System.out.println();
        printArray(array);
    }

    private static void bubbleSort(int[] array) {
        int temp;
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - 1 - i; j++) {
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }

    private static void printArray(int[] array) {
        for (int t : array) {
            System.out.print(t + ",");
        }

    }
}
