package com.example.algorithm;

import java.util.Arrays;

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
        int number = 88;
        int[] numbers = {1, 2, 3, 4, 50, 52, 53, number, 90};//fakeNumbers(20);
//        numbers[9] = 50;
//        Arrays.sort(numbers);
        printArray(numbers);

        System.out.println("使用 for 循环的方式。index == "+forExample(numbers, number));
        System.out.println();

        System.out.println("自定义二分搜索。index == " + myBinarySearch(numbers, 0, numbers.length - 1, number));
        System.out.println();

        System.out.println("Arrays.binarySearch。index == " + Arrays.binarySearch(numbers, number));
    }

    private static void printArray(int[] numbers) {
        for (int number : numbers) {
            System.out.print(number + " ");
        }
        System.out.println();
    }

    private static int forExample(int[] array, int number) {
        int count = 0;
        for (int i = 0, length = array.length; i < length; i++) {
            count++;
            if (number == array[i]) {
                System.out.println("在数组下标为 " + i + " 的地方找到了 " + number + " ,共查找了" + count + "次。");
                return i;
            }
        }
        System.out.println("没有找到" + number);
        return -1;
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
            if (array.length - 1 == i) {
                System.out.println("抱歉，没有找到");
            } else if (array[index] < number) {
                start = index;
            } else if (array[index] > number) {
                end = index;
            } else {
                System.out.println(array[index] + "找到了，在数组下标为" + index + "的地方,查找了" + count + "次。");
                break;
            }


        }
        return count;
    }

    //java.util.Arrays.binarySearch0(int[], int, int, int)
    private static int binarySearch(int[] a, int fromIndex, int toIndex, int key) {
        int low = fromIndex;
        int high = toIndex;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = a[mid];

            if (midVal < key)
                low = mid + 1;
            else if (midVal > key)
                high = mid - 1;
            else
                return mid; // key found
        }
        return -(low + 1);  // key not found.
    }

    private static int myBinarySearch(int[] array, int startIndex, int endIndex, int number) {
        int count = 0;
        while (startIndex <= endIndex) {
            count++;
            int middleIndex = (startIndex + endIndex) >>> 1;
            int middleValue = array[middleIndex];
            if (middleValue < number) {
                startIndex = middleIndex + 1;
            } else if (middleValue > number) {
                endIndex = middleIndex - 1;
            }else {
                System.out.println("myBinarySearch() 一共查找的次数为：" + count);
                return middleIndex;
            }
        }
        System.out.println("没找到值");
        return -1;
    }
}
