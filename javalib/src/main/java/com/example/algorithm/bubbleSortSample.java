package com.example.algorithm;

/**
 * 冒泡排序。
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
