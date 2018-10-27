package com.example;

import java.io.Externalizable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author VanceKing
 * @since 2018/4/23.
 */

public class TransientSample {
    public static void main(String[] args) throws Exception {
        testExternalizable();
    }

    private static void testExternalizable() {
        String path = "c://user.txt";
        Person person = new Person(100, "Vance");
        System.out.println("序列化：" + person.toString());

        serialize(person, path);
        Person person2 = (Person) unSerialize(path);
        System.out.println("反序列化：" + person2.toString());
        System.out.println("前后对象相同（==）吗？： " + (person == person2));

    }

    private static void testSerializable() throws Exception {
        User user = new User("Vance", "111");
        System.out.println("序列化：" + user.toString());

        String path = "c://user.txt";
        serialize(user, path);
        User user2 = (User) unSerialize(path);

        System.out.println("反序列化：" + user2.toString());
        System.out.println("前后对象相同（==）吗？： " + (user == user2));
    }

    private static boolean serialize(Object object, String filePath) {
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(filePath));
            outputStream.writeObject(object);
            outputStream.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static Object unSerialize(String filePath) {
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(new FileInputStream(filePath));
            Object object = inputStream.readObject();
            inputStream.close();
            return object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //实现 Serializable 接口的类可以自动序列化。此时 transient 才有意义。
    private static class User implements Serializable {
        private String name;

        //为了安全性，密码字段不序列化，使用 transient 指定。
        private transient String passWord;

        public User() {
        }

        public User(String name, String passWord) {
            this.name = name;
            this.passWord = passWord;
        }

        @Override public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", passWord='" + passWord + '\'' +
                    '}';
        }
    }

    //需要手动实现序列化，此时 transient 已经失去了意义。
    private static class Person implements Externalizable {
        private int id;
        private transient String name;

        public Person() {
        }

        public Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override public void writeExternal(ObjectOutput objectOutput) throws IOException {
            objectOutput.writeObject(name);
            objectOutput.writeObject(id);
        }

        @Override public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
            name = (String) objectInput.readObject();
            id = (Integer) objectInput.readObject();
        }

        @Override public String toString() {
            return "Person{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
