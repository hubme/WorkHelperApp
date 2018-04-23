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
        User user = new User();
        System.out.println("序列化前：" + user.toString());

        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("g://user.txt"));
        outputStream.writeObject(user);
        outputStream.flush();
        outputStream.close();

        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("g://user.txt"));
        User user2 = (User) inputStream.readObject();
        inputStream.close();

        System.out.println(user2.toString());
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
        private transient String content = "我能被序列化吗?";

        @Override public void writeExternal(ObjectOutput objectOutput) throws IOException {
            objectOutput.writeObject(content);
        }

        @Override public void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException {
            content = (String) objectInput.readObject();
        }
    }
}
