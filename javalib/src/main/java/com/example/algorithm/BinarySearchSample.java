package com.example.algorithm;

import java.util.Arrays;
import java.util.Random;

/**
 * 二分法。
 * 二分法的实现需要 两个下标变量（start和end）来控制查询数组的范围，默认是从0到数组的最后一个元素，
 * 然后需要获取数组的中间元素（下标为(start+end)/2的元素）与要查找的值作比较，
 * 如果这个元素大于查找的值，说明 要查找的值在数组下标0到(start+end)/2 之间，
 * 否则在下标(start+end)/2到数组最后一个元素之间。
 * 所以 每次比较之前都是将查询的范围缩短一半，然后控制这个查询的范围依据就是根据上一次数组中间的元素与查找的值比较之后
 * 数组的元素的下标来重新分配的，以便提高效率。
 *
 * @author VanceKing
 * @since 2018/4/13.
 */
public class BinarySearchSample {
    public static void main(String[] args) {
        int[] numbers = {1, 2, 3, 4, 50};//fakeNumbers(20);
        //        numbers[9] = 50;
        Arrays.sort(numbers);
        printArray(numbers);

        forExample(numbers, 50);


        binarySearch(numbers, 50);
    }

    private static int[] fakeNumbers(int size) {
        int[] numbers = new int[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            numbers[i] = random.nextInt(100);
        }
        return numbers;
    }

    private static void printArray(int[] numbers) {
        for (int number : numbers) {
            System.out.print(number + " ");
        }
        System.out.println();
    }

    private static int forExample(int[] array, int number) {
        System.out.println("使用 for 循环的方式。");
        int count = 0;
        boolean result = false;
        for (int i = 0, length = array.length; i < length; i++) {
            count++;
            if (number == array[i]) {
                System.out.println("在数组下标为 " + i + " 的地方找到了 " + number + " ,共查找了" + count + "次。");
                result = true;
                break;
            }
        }
        if (!result) {
            System.out.println("没有找到" + number);
        }
        return count;
    }

    /**
     * 二分法查询指定数字 的例子
     */
    private static int binarySearch(int[] array, int number) {
        System.out.println("使用二分法查找");
        int index = 0; // 检索的时候
        int start = 0;  //用start和end两个索引控制它的查询范围
        int end = array.length - 1;
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            count++;
            index = (start + end) / 2;
            if (array[index] < number) {
                start = index;
            } else if (array[index] > number) {
                end = index;
            } else if (array.length - 1 == i) {
                System.out.println("抱歉，没有找到");
            } else {
                System.out.println(array[index] + "找到了，在数组下标为" + index + "的地方,查找了" + count + "次。");
                break;
            }


        }
        return count;
    }
}
