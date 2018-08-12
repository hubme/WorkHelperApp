package com.example;

/**
 * 深拷贝和浅拷贝。原型模式。
 * 应用场景：
 * 1、解决构建复杂对象的资源消耗问题，提升创建对象的效率。
 * 2、保护对象不被修改。
 * 优点：
 * 二进制拷贝，比 new 对象性能高。
 * 缺点：
 * 对象的构造方法不会执行。
 *
 * @author VanceKing
 * @since 2018/4/23.
 */

public class Clone {
    public static void main(String[] args) throws CloneNotSupportedException {
        test2();
    }

    private static void test1() throws CloneNotSupportedException {
        Course course = new Course("chinese");
        System.out.println("course: " + course.toString());

        Course clone = (Course) course.clone();
        System.out.println("clone: " + clone.toString());

        //不影响原来的对象
        clone.setName("english");
        System.out.println(course.toString() + " " + clone.toString());
    }

    private static void test2() throws CloneNotSupportedException {
        Course course = new Course("chinese");
        Student student1 = new Student("aaa", 17, course);
        System.out.println("student1: " + student1.toString());

        Student student2 = (Student) student1.clone();
        System.out.println("student2: " + student2.toString());

        student2.setName("bbb");
        student2.setAge(18);
        student2.getCourse().setName("english");

        System.out.println("修改后");
        System.out.println("student1: " + student1.toString());
        System.out.println("student2: " + student2.toString());
    }

    private static class Student implements Cloneable {
        private String name;
        private int age;
        private Course course;

        public Student(String name, int age, Course course) {
            this.name = name;
            this.age = age;
            this.course = course;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public Course getCourse() {
            return course;
        }

        public void setCourse(Course course) {
            this.course = course;
        }

        @Override protected Object clone() throws CloneNotSupportedException {
            Student clone = (Student) super.clone();
            clone.setCourse((Course) this.course.clone());
            return clone;
        }

        @Override public String toString() {
            return "Student{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", course=" + course +
                    '}';
        }
    }

    private static class Course implements Cloneable {
        private String name;

        public Course(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override public String toString() {
            return "Course{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
