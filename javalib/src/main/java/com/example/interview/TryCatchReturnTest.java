package com.example.interview;

/**
 * try catch finally 含有 return 语句时执行顺序。
 * 总结：
 * finally块的语句在try或catch中的return语句执行之后返回之前执行，
 * 且finally里的修改语句可能影响也可能不影响try或catch中 return 已经确定的返回值（返回引用类型的变量会存在影响），
 * 若finally里也有return语句则覆盖try或catch中的return语句直接返回。
 *
 * @author VanceKing
 * @since 2018/2/2.
 */

public class TryCatchReturnTest {
    public static void main(String[] args) {

        System.out.println(test5());
    }

    private static int action1() {
        int result = 1;
        try {
            return result;
        } finally {
            ++result;
            //finally 块中的 return 会覆盖 try 块中的 return 语句。编译器会警告。
            return result;
        }
    }

    private static Person action2() {
        Person person = new Person("Hai");
        try {
            return person;
        } finally {
            //返回 person 之前调用
            person.setName("VanceKing");
        }
    }

    private static int action3() {
        try {
            if (true) {
                throw new NullPointerException("");
            }
            return 1;
        } catch (Exception e) {
            if (true) {
                //虚拟机退出，无返回值。
                System.exit(0);
            }
            return 2;
        } finally {
            System.out.println("finally");
        }
    }

    /*
    try block
    finally block
    b>25, b = 100
    100
     */
    public static int test1() {
        int b = 20;

        try {
            System.out.println("try block");

            //此时值已经确定是100，等执行完 finally 返回。
            return b += 80;
        } catch (Exception e) {
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }
        }

        return b;
    }

    /*
    try block
    finally block
    b>25, b = 100
    200
     */
    public static int test2() {
        int b = 20;
        try {
            System.out.println("try block");
            return b += 80;//此时值已经确定是100，等执行完 finally 返回。
        } catch (Exception e) {
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }

            return 200;//直接返回200,覆盖 try 中的语句。
        }

        // return b;
    }

    /*
    try block
    finally block
    b>25, b = 100
    100
     */
    public static int test3() {
        int b = 20;

        try {
            System.out.println("try block");

            return b += 80;//此时值已经确定是100，等执行完 finally 返回。
        } catch (Exception e) {

            System.out.println("catch block");
        } finally {

            System.out.println("finally block");

            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }

            b = 150;
        }
        //由于 try 正常执行，返回100。不会走到此处。
        return 2000;
    }

    /*
    try block
    catch block
    finally block
    b>25, b = 35
    85
     */
    public static int test4() {
        int b = 20;
        try {
            System.out.println("try block");
            b = b / 0;
            return b += 80;
        } catch (Exception e) {
            b += 15;
            System.out.println("catch block");
        } finally {
            System.out.println("finally block");
            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }

            b += 50;
        }

        return b;
    }

    /*
    try block
    catch block
    finally block
    b>25, b = 35
    35
     */
    public static int test5() {
        int b = 20;
        try {
            System.out.println("try block");
            b = b / 0;
            return b += 80;
        } catch (Exception e) {
            System.out.println("catch block");
            return b += 15;//此时值已经确定是35，等执行完 finally 返回。
        } finally {

            System.out.println("finally block");

            if (b > 25) {
                System.out.println("b>25, b = " + b);
            }

            b += 50;
        }

        //return b;
    }

    private static class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}

